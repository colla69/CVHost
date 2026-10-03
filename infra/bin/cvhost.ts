#!/usr/bin/env node
import * as cdk from 'aws-cdk-lib'
import { DnsStack } from '../lib/dns-stack'
import { SiteStack } from '../lib/site-stack'

const app = new cdk.App()

const zoneDomain: string = app.node.tryGetContext('zoneDomain') ?? 'colarietitosti.info'
const siteDomain: string = app.node.tryGetContext('siteDomain') ?? `cv.${zoneDomain}`

// Pinned, not taken from the ambient profile, so a synth always produces the
// same template no matter whose shell runs it. CloudFront certificates must be
// in us-east-1, and keeping both stacks there avoids cross-region references
// (which CDK implements with custom resources that complicate a destroy).
// The bucket lives here too -- for a static site behind a CDN the origin region
// only affects the rare cache miss.
const env: cdk.Environment = {
  account: app.node.tryGetContext('account') ?? process.env.CDK_DEFAULT_ACCOUNT ?? '656847041140',
  region: 'us-east-1'
}

const dns = new DnsStack(app, 'CvHostDns', {
  env,
  zoneDomain,
  description: 'CVHost - Route 53 hosted zone, mirrored from Strato',
  terminationProtection: true
})

// On since the cutover of 2026-10-02, via "cutover": true in cdk.json, so a
// plain `cdk deploy CvHostSite` can never quietly drop the live cv alias.
// Building the stack from scratch? Deploy with `-c cutover=false` first --
// see SiteStackProps.cutover for why pointing DNS at a freshly created,
// still-empty bucket is its own step.
const cutover = app.node.tryGetContext('cutover') === true ||
                app.node.tryGetContext('cutover') === 'true'

new SiteStack(app, 'CvHostSite', {
  env,
  siteDomain,
  // The apex and www have no content of their own; they 301 to the CV.
  redirectDomains: [zoneDomain, `www.${zoneDomain}`],
  hostedZone: dns.hostedZone,
  cutover,
  description: 'CVHost - S3 + CloudFront + ACM for the CV site'
})

cdk.Tags.of(app).add('Project', 'CVHost')
cdk.Tags.of(app).add('ManagedBy', 'cdk')

app.synth()
