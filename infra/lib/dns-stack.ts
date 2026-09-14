import * as cdk from 'aws-cdk-lib'
import * as route53 from 'aws-cdk-lib/aws-route53'
import { Construct } from 'constructs'
import * as path from 'path'
import { readZoneRecords, ZoneRecord } from './zone-records'

export interface DnsStackProps extends cdk.StackProps {
  zoneDomain: string
}

/**
 * Stack 1 of 2: the DNS zone, and nothing that depends on DNS resolving.
 *
 * This is deployed first and on its own, because delegation is a manual step at
 * the registrar that sits in the middle of the migration:
 *
 *   cdk deploy CvHostDns   ->   set the four NS records at Strato   ->   cdk deploy CvHostSite
 *
 * CvHostSite requests an ACM certificate validated over DNS, and ACM resolves
 * the validation record over the *public* internet. Until Strato delegates to
 * these nameservers, that lookup still lands on Strato and the deploy would sit
 * there until it timed out. Splitting the stacks turns that into an explicit
 * gate instead of a mysterious hang.
 */
export class DnsStack extends cdk.Stack {
  public readonly hostedZone: route53.PublicHostedZone

  constructor (scope: Construct, id: string, props: DnsStackProps) {
    super(scope, id, props)

    this.hostedZone = new route53.PublicHostedZone(this, 'Zone', {
      zoneName: props.zoneDomain,
      comment: 'CVHost - migrated from Strato DNS'
    })

    // Once the registrar delegates to this zone, destroying it takes the whole
    // domain down until someone notices and repoints the nameservers -- mail,
    // subdomains, everything, not just the CV. RETAIN means `cdk destroy` leaves
    // it standing and you delete it deliberately or not at all.
    this.hostedZone.applyRemovalPolicy(cdk.RemovalPolicy.RETAIN)

    const recordsFile = path.join(__dirname, '..', 'zone-records.txt')
    const records = readZoneRecords(recordsFile)

    records.forEach(record => this.addRecord(record))

    new cdk.CfnOutput(this, 'HostedZoneId', {
      value: this.hostedZone.hostedZoneId,
      description: 'Route 53 hosted zone id for ' + props.zoneDomain
    })

    new cdk.CfnOutput(this, 'NameServers', {
      // Set these four at Strato, and only after `aws-infra.sh verify` is clean.
      value: cdk.Fn.join(' ', this.hostedZone.hostedZoneNameServers ?? []),
      description: 'Set these as the nameservers at the registrar'
    })

    new cdk.CfnOutput(this, 'MirroredRecordCount', {
      value: String(records.length),
      description: 'Records reproduced from zone-records.txt'
    })
  }

  /**
   * Route 53 models some record types as their own construct because the shape
   * of the value differs (MX has a priority, TXT needs quoting). Anything this
   * does not recognise throws at synth time on purpose: silently skipping a
   * record from the Strato export is how a subdomain goes dark at the cutover.
   */
  private addRecord (record: ZoneRecord): void {
    const id = `Rec${record.type}${record.name === '' ? 'Apex' : record.name}`
      .replace(/[^A-Za-z0-9]/g, '')
    const common = {
      zone: this.hostedZone,
      recordName: record.name,
      ttl: cdk.Duration.seconds(record.ttl)
    }

    switch (record.type) {
      case 'A':
        new route53.ARecord(this, id, { ...common, target: route53.RecordTarget.fromIpAddresses(...record.values) })
        break
      case 'AAAA':
        new route53.AaaaRecord(this, id, { ...common, target: route53.RecordTarget.fromIpAddresses(...record.values) })
        break
      case 'CNAME':
        if (record.values.length !== 1) {
          throw new Error(`CNAME ${record.name} must have exactly one value, got ${record.values.length}`)
        }
        new route53.CnameRecord(this, id, { ...common, domainName: record.values[0] })
        break
      case 'TXT':
        // TxtRecord handles the quoting and the 255-byte chunk splitting.
        new route53.TxtRecord(this, id, { ...common, values: record.values })
        break
      case 'MX':
        new route53.MxRecord(this, id, {
          ...common,
          values: record.values.map(v => {
            const [priority, ...host] = v.split(/\s+/)
            const hostName = host.join(' ')
            if (hostName === '' || Number.isNaN(Number(priority))) {
              throw new Error(`MX ${record.name}: expected "<priority> <host>", got: ${v}`)
            }
            return { priority: Number(priority), hostName }
          })
        })
        break
      case 'NS':
        new route53.NsRecord(this, id, { ...common, values: record.values })
        break
      case 'SRV':
        new route53.SrvRecord(this, id, {
          ...common,
          values: record.values.map(v => {
            const [priority, weight, port, ...host] = v.split(/\s+/)
            return {
              priority: Number(priority),
              weight: Number(weight),
              port: Number(port),
              hostName: host.join(' ')
            }
          })
        })
        break
      case 'CAA':
        new route53.CaaRecord(this, id, {
          ...common,
          values: record.values.map(v => {
            const [flag, tag, ...rest] = v.split(/\s+/)
            return {
              flag: Number(flag),
              tag: tag as route53.CaaTag,
              value: rest.join(' ').replace(/^"|"$/g, '')
            }
          })
        })
        break
      default:
        throw new Error(
          `zone-records.txt: unsupported record type "${record.type}" for "${record.name || '@'}". ` +
          'Add a case to DnsStack.addRecord rather than dropping the record.'
        )
    }
  }
}
