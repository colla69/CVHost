import * as cdk from 'aws-cdk-lib'
import * as acm from 'aws-cdk-lib/aws-certificatemanager'
import * as cloudfront from 'aws-cdk-lib/aws-cloudfront'
import * as origins from 'aws-cdk-lib/aws-cloudfront-origins'
import * as route53 from 'aws-cdk-lib/aws-route53'
import * as targets from 'aws-cdk-lib/aws-route53-targets'
import * as s3 from 'aws-cdk-lib/aws-s3'
import { Construct } from 'constructs'
import * as fs from 'fs'
import * as path from 'path'

export interface SiteStackProps extends cdk.StackProps {
  siteDomain: string
  /**
   * Hostnames that answer with a 301 to siteDomain, keeping the path -- the
   * apex and www. They share the certificate and the distribution; the
   * viewer-request function does the redirecting.
   */
  redirectDomains: string[]
  hostedZone: route53.IPublicHostedZone
  /**
   * Create the Route 53 alias that points the live hostname at CloudFront.
   *
   * False by default, and that default is the whole point. Creating the
   * distribution and pointing DNS at it are separate events: the bucket is
   * empty the moment this stack first deploys, so an alias created at the same
   * time would send real visitors to a 404 until someone ran deploy.sh. With
   * the flag off, the distribution is reachable on its own *.cloudfront.net
   * name for as long as it takes to fill the bucket and check it.
   *
   *   cdk deploy CvHostSite                    # build it, DNS untouched
   *   scripts/deploy.sh --apply                # fill the bucket, check the CF domain
   *   cdk deploy CvHostSite -c cutover=true    # the actual switch
   *
   * Reverting is `cdk deploy` without the flag, and every TTL in the zone is
   * 150 seconds.
   */
  cutover: boolean
}

/**
 * Stack 2 of 2: the site itself.
 *
 *   Route 53 alias  ->  CloudFront  ->  Origin Access Control  ->  private S3
 *
 * Deploy this only after the registrar delegates to CvHostDns's nameservers --
 * see the comment on DnsStack for why.
 *
 * This stack owns infrastructure, not content. Publishing the built site stays
 * with scripts/deploy.sh, which reads the bucket name and distribution id from
 * this stack's outputs. That split means a content change is one `deploy.sh`
 * away and never needs a `cdk deploy`.
 */
export class SiteStack extends cdk.Stack {
  public readonly bucket: s3.Bucket
  public readonly distribution: cloudfront.Distribution

  constructor (scope: Construct, id: string, props: SiteStackProps) {
    super(scope, id, props)

    // Everything in this bucket is build output from frontend/ -- the hashed
    // assets, and the CV PDFs that live in frontend/public/data/ and ship in
    // git. Nothing here is authored in place, so DESTROY is honest: the bucket
    // is reproducible from a checkout plus one deploy.sh run.
    this.bucket = new s3.Bucket(this, 'SiteBucket', {
      blockPublicAccess: s3.BlockPublicAccess.BLOCK_ALL,
      encryption: s3.BucketEncryption.S3_MANAGED,
      enforceSSL: true,
      versioned: true,
      removalPolicy: cdk.RemovalPolicy.DESTROY,
      autoDeleteObjects: true,
      lifecycleRules: [{
        // Versioning is here to make a bad deploy recoverable, not to archive
        // three years of the site. Old versions age out after a month.
        id: 'expire-noncurrent-versions',
        noncurrentVersionExpiration: cdk.Duration.days(30),
        abortIncompleteMultipartUploadAfter: cdk.Duration.days(7)
      }]
    })

    // CloudFront reads certificates from us-east-1 only, which is why this whole
    // app is pinned there (see bin/cvhost.ts). Validation writes its CNAME into
    // the hosted zone automatically -- no manual record, no wildcard collision.
    const certificate = new acm.Certificate(this, 'Certificate', {
      domainName: props.siteDomain,
      subjectAlternativeNames: props.redirectDomains,
      validation: acm.CertificateValidation.fromDns(props.hostedZone)
    })

    // CloudFront Functions have no environment variables, so the hostnames are
    // written into the code here. A placeholder left behind would ship a function
    // that throws on every request, so refuse to synth instead.
    const functionCode = fs.readFileSync(path.join(__dirname, '..', 'functions', 'spa-fallback.js'), 'utf8')
      .replace('__SITE_DOMAIN__', props.siteDomain)
      .replace('__REDIRECT_HOSTS__', JSON.stringify(props.redirectDomains))
    if (functionCode.includes('__SITE_DOMAIN__') || functionCode.includes('__REDIRECT_HOSTS__')) {
      throw new Error('functions/spa-fallback.js: a placeholder was not substituted')
    }

    const spaFallback = new cloudfront.Function(this, 'SpaFallback', {
      code: cloudfront.FunctionCode.fromInline(functionCode),
      runtime: cloudfront.FunctionRuntime.JS_2_0,
      comment: 'Redirect apex and www to the CV host; rewrite extensionless paths to /index.html'
    })

    this.distribution = new cloudfront.Distribution(this, 'Distribution', {
      comment: `CVHost - ${props.siteDomain}`,
      domainNames: [props.siteDomain, ...props.redirectDomains],
      certificate,
      defaultRootObject: 'index.html',
      minimumProtocolVersion: cloudfront.SecurityPolicyProtocol.TLS_V1_2_2021,
      httpVersion: cloudfront.HttpVersion.HTTP2_AND_3,
      enableIpv6: true,
      // NA + EU. The audience for this site is European employers; the cheaper
      // class simply omits the edge locations none of them use.
      priceClass: cloudfront.PriceClass.PRICE_CLASS_100,
      defaultBehavior: {
        // withOriginAccessControl signs origin requests and writes the matching
        // bucket policy, so the bucket never needs to be public. The bucket it
        // replaces was world-readable.
        origin: origins.S3BucketOrigin.withOriginAccessControl(this.bucket),
        viewerProtocolPolicy: cloudfront.ViewerProtocolPolicy.REDIRECT_TO_HTTPS,
        allowedMethods: cloudfront.AllowedMethods.ALLOW_GET_HEAD,
        cachedMethods: cloudfront.CachedMethods.CACHE_GET_HEAD,
        cachePolicy: cloudfront.CachePolicy.CACHING_OPTIMIZED,
        responseHeadersPolicy: cloudfront.ResponseHeadersPolicy.SECURITY_HEADERS,
        compress: true,
        functionAssociations: [{
          function: spaFallback,
          eventType: cloudfront.FunctionEventType.VIEWER_REQUEST
        }]
      }
    })

    // A + AAAA aliases for every hostname the distribution answers. Route 53
    // takes a full hostname as the record name, apex included.
    //
    // The cv records keep their original construct ids: a new id for the same
    // name and type makes CloudFormation create the replacement before deleting
    // the old record, and that create fails because the name is taken.
    if (props.cutover) {
      const aliasTarget = route53.RecordTarget.fromAlias(new targets.CloudFrontTarget(this.distribution))
      const zoneName = props.hostedZone.zoneName
      const label = (domain: string): string => {
        if (domain === zoneName) return 'Apex'
        const name = domain.slice(0, -(zoneName.length + 1)).replace(/[^A-Za-z0-9]/g, '')
        return name.charAt(0).toUpperCase() + name.slice(1)
      }
      const aliases: Array<[string, string]> = [
        ['SiteAlias', props.siteDomain],
        ...props.redirectDomains.map((d): [string, string] => [`Redirect${label(d)}Alias`, d])
      ]

      aliases.forEach(([id, recordName]) => {
        new route53.ARecord(this, `${id}A`, {
          zone: props.hostedZone,
          recordName,
          target: aliasTarget,
          comment: 'CVHost - CloudFront'
        })

        new route53.AaaaRecord(this, `${id}AAAA`, {
          zone: props.hostedZone,
          recordName,
          target: aliasTarget,
          comment: 'CVHost - CloudFront'
        })
      })
    }

    new cdk.CfnOutput(this, 'BucketName', {
      value: this.bucket.bucketName,
      description: 'Origin bucket - set DEPLOY_BUCKET in scripts/.env.deploy to this'
    })

    new cdk.CfnOutput(this, 'DistributionId', {
      value: this.distribution.distributionId,
      description: 'Set CF_DISTRIBUTION_ID in scripts/.env.deploy to this'
    })

    new cdk.CfnOutput(this, 'DistributionDomainName', {
      value: this.distribution.distributionDomainName,
      description: 'Test the site here before DNS points at it'
    })

    new cdk.CfnOutput(this, 'SiteUrl', {
      value: `https://${props.siteDomain}/`,
      description: 'Live site once DNS has propagated'
    })

    new cdk.CfnOutput(this, 'CutoverState', {
      value: props.cutover
        ? `${props.siteDomain} points at this distribution`
        : `${props.siteDomain} still points at the Strato box - deploy with -c cutover=true to switch`,
      description: 'Whether the live hostname has been switched to CloudFront'
    })
  }
}
