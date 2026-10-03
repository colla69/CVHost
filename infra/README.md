# infra — AWS hosting for cv.colarietitosti.info

A CDK v2 app (TypeScript) that provisions the whole of the site's hosting. It is the
**only** thing that creates AWS infrastructure for this project. `scripts/aws-infra.sh`
reads and reports; it does not provision.

## What it builds

```
     cv.colarietitosti.info    colarietitosti.info    www.colarietitosti.info
                  \                    |                    /
                   Route 53 A + AAAA aliases, one pair each            CvHostSite
                                       |
                           CloudFront distribution
                           ├─ ACM certificate (us-east-1), all three names
                           ├─ CloudFront Function (viewer-request)
                           │    apex, www → 301 https://cv.colarietitosti.info/<path>
                           │    cv        → SPA deep links
                           ├─ CachingOptimized + SecurityHeaders policies
                           └─ Origin Access Control (SigV4)
                                       |
                           S3 bucket — private, versioned              CvHostSite
                           BlockPublicAccess: ALL

  Hosted zone, plus whatever zone-records.txt lists (nothing today)    CvHostDns
```

Everything on the domain is AWS. The zone used to mirror the Strato zone (apex, a
wildcard and eight subdomains on the box at `85.214.103.161`). Those records were removed
in the Strato exit (`STRATO-EXIT.md`, Phase 3), because the box goes away with the account
and its IP goes to someone else. Any other name under the domain now gets NXDOMAIN.

## Two stacks, and why

Both are pinned to **us-east-1**. CloudFront reads ACM certificates from that region and
nowhere else, and keeping both stacks there means no cross-region references — CDK
implements those with custom resources that complicate a `destroy`. For a static site
behind a CDN the origin's region only affects the rare cache miss.

| Stack | Contains | Deployed |
| --- | --- | --- |
| `CvHostDns` | Public hosted zone + every record mirrored from Strato | **first**, alone |
| `CvHostSite` | Bucket, certificate, CloudFront function, distribution, `cv` alias | after delegation |

They are split because **delegation is a manual step at the registrar that sits between
them**:

```
cdk deploy CvHostDns  →  set 4 NS records at Strato  →  cdk deploy CvHostSite
```

`CvHostSite` requests a DNS-validated certificate, and ACM resolves the validation record
over the public internet. Until Strato delegates to the Route 53 nameservers, that lookup
still lands on Strato and the deploy sits there until it times out. Splitting the stacks
turns that into an explicit gate rather than a mysterious hang.

Once delegation is live, CDK writes the validation record into the zone itself — there is
no CNAME to paste into the Strato panel, and no collision with the wildcard.

## Reproducibility

`infra/zone-records.txt` is the single source of truth for the mirrored DNS records. Both
`lib/dns-stack.ts` and `scripts/aws-infra.sh verify` read it, so there is no second list to
keep in step. An unsupported record type **throws at synth time** rather than being
skipped: a record silently dropped there is a subdomain that goes dark at the cutover.

Removal policies are deliberate and differ:

- **Hosted zone: `RETAIN`.** Once the registrar delegates to it, destroying the zone takes
  the entire domain down — mail, subdomains, everything, not just the CV — until someone
  repoints the nameservers. `cdk destroy` leaves it standing.
- **Bucket: `DESTROY` + `autoDeleteObjects`.** Everything in it is build output from
  `frontend/`, including the CV PDFs, which ship in git under `frontend/public/data/`.
  Nothing is authored in place, so the bucket is reproducible from a checkout plus one
  `scripts/deploy.sh --apply`. Versioning is on with a 30-day expiry so a bad deploy is
  recoverable without archiving years of the site.

`CvHostDns` has `terminationProtection: true` for the same reason as the zone's RETAIN.

## This stack owns infrastructure, not content

Publishing the built site stays with `scripts/deploy.sh`, which reads `BucketName` and
`DistributionId` from this stack's CloudFormation outputs. So no resource name is
duplicated into a config file where it can drift, a content change never needs a
`cdk deploy`, and `deploy.sh` keeps its three-pass ordering (immutable assets → data →
`index.html` last) plus its refusal to `--delete` against a partial build.

## Commands

```sh
npm --prefix infra install
npm --prefix infra run synth          # generate templates, no AWS calls
npm --prefix infra run diff           # what would change against what is deployed
npm --prefix infra run deploy:dns     # stack 1
npm --prefix infra run deploy:site    # stack 2, only after delegation
```

## Migration runbook

**Done.** Steps 1–8 were completed between 2026-09-14 and 2026-10-02. The Strato exit that followed
(domain transfer, removing the box's records, apex/www redirect) is recorded in
[`../STRATO-EXIT.md`](../STRATO-EXIT.md), which also covers what's left of step 9. The table stays
as the record of how a zero-downtime move was sequenced.

| # | Step | Visitor impact | Rollback |
| --- | --- | --- | --- |
| 1 | Fill in `zone-records.txt` from the Strato panel (**DNS-Einstellungen *and* Subdomains**) | none | n/a |
| 2 | `deploy:dns` | none — zone is inert until delegated | `cdk destroy CvHostDns` |
| 3 | `scripts/aws-infra.sh verify` — must show no unexplained differences | none | n/a |
| 4 | Set the 4 nameservers at Strato | **none** — every answer is byte-identical | restore Strato's NS, ~150 s |
| 5 | `deploy:site` — certificate self-validates in seconds | none — no DNS record is created | `cdk destroy CvHostSite` |
| 6 | `scripts/deploy.sh --apply` — fills the new bucket | none | n/a |
| 7 | Check `https://<DistributionDomainName>/` **and `/qualifications`** | none | n/a |
| 8 | `cdk deploy CvHostSite -c cutover=true` — **this is the switch** | the real cutover | redeploy without the flag, ~150 s |
| 9 | Retire the old public `cv-host` bucket (deleted 2026-10-03). The Strato box and its nginx go with the Strato account | none | n/a |

Steps 5 and 8 are separate on purpose. The bucket is empty the moment `CvHostSite` first
deploys, so creating the alias at the same time would point real visitors at a 404 until
someone ran `deploy.sh`. With `cutover` off — the default — the distribution is reachable
only on its own `*.cloudfront.net` name, for as long as it takes to fill the bucket and
look at it.

Every TTL in the zone is **150 seconds** and the domain has **no DNSSEC**, so every step
above reverses in about three minutes. That is the property that makes step 4 safe.

## What this fixes, beyond moving host

- **Deep links.** `https://cv.colarietitosti.info/qualifications` currently returns **404**:
  the Strato nginx passes S3's key-not-found straight through, so a refresh or a shared
  link dies. `functions/spa-fallback.js` rewrites extensionless paths to `/index.html` at
  the edge. It deliberately rewrites *only* extensionless paths, so a genuinely missing
  `/data/foo.pdf` still 404s instead of being answered with the SPA shell and a misleading
  200 — which is what a blanket 403-to-`index.html` error page would do.
- **A world-readable origin.** The bucket it replaces has
  `{"Principal": "*", "Action": "s3:GetObject"}` and no public-access block, so the raw S3
  URL serves the site to anyone, bypassing the domain entirely. The new bucket blocks all
  public access and grants read only to the CloudFront service principal.
- **TLS and HTTP.** TLS 1.2+ only, HTTP/2 and HTTP/3, IPv6, Brotli/gzip, HSTS and the rest
  of the managed `SecurityHeadersPolicy`, versus nginx 1.14 on Ubuntu with a per-host
  Let's Encrypt certificate renewed by hand.

## Troubleshooting

**`No bucket named 'cdk-hnb659fds-assets-…'. Is account … bootstrapped?`**

CDK uploads templates to a staging bucket created by a one-off `CDKToolkit` stack. This
account had one from an earlier, abandoned CDK project whose staging bucket *and* ECR
repository had since been deleted by hand, leaving CloudFormation managing resources that
no longer existed. `cdk bootstrap` then fails, and so does every deploy.

Repairing it in place does not work — each attempt surfaces the next missing resource. What
did work, on 2026-09-14:

```sh
# 1. get the stack out of UPDATE_ROLLBACK_FAILED. Only resources actually in
#    UPDATE_FAILED may be skipped; listing one that succeeded is rejected.
aws cloudformation continue-update-rollback --region us-east-1 --stack-name CDKToolkit \
  --resources-to-skip FilePublishingRole ImagePublishingRole LookupRole StagingBucket

# 2. delete and rebuild it. CDKToolkit holds no application state -- a scratch
#    bucket, an unused container registry and some IAM roles, all recreated with
#    identical names.
aws cloudformation delete-stack --region us-east-1 --stack-name CDKToolkit
npx cdk bootstrap aws://656847041140/us-east-1
```

Bootstrap went from a broken version 14 to a healthy 32. `AwsStack` in us-east-1 is a
separate leftover from 2022 containing only `CDKMetadata` — empty, free, harmless.

**The nameserver change has not taken effect.**

Resolver caches are not the thing to check; ask the registry. Strato quotes **up to 24
hours** for an NS change to reach the `.info` registry, though it is usually far quicker.
Until it lands, the old nameservers stay authoritative and nothing changes for anyone —
there is no half-migrated state.

## TLS

CloudFront terminates TLS with one ACM certificate for `cv`, the apex and `www`, renewed by ACM
automatically, since its DNS validation records sit in the zone CDK owns. CloudFront reaches S3 over
HTTPS with SigV4-signed requests via Origin Access Control, and the bucket refuses anything else
(`enforceSSL`). The Strato box's Let's Encrypt certificates and the plaintext proxy hop are gone with
the box.

## Cost

Hosted zone $0.50/month. The `.info` registration at Route 53 Domains is roughly $25–28 a year,
billed on renewal each August. CloudFront's perpetual free tier covers 1 TB/month and 10 M requests;
this site won't come close. ACM certificates are free. S3 storage for a ~10 MB site is cents. All in,
**about $32–35 a year**, nearly all of it the domain and the zone.
