# Deployment

How cv.colarietitosti.info is actually served, and how to publish to it.

## Architecture

```
cv.colarietitosti.info ─┐
colarietitosti.info ────┼─ Route 53 aliases ─ CloudFront ─ Origin Access Control ─ private S3 bucket
www.colarietitosti.info ┘                        └─ CloudFront Function: apex/www → 301 to cv,
                                                    extensionless paths → /index.html
```

All of it is CDK v2 in `infra/`. **[`infra/README.md`](infra/README.md) has the architecture, the
reasoning and the commands.** The domain is registered at Route 53 Domains, with auto-renew,
transfer lock and WHOIS privacy on. A live response carries `via: … cloudfront`.

This replaced, on 2026-10-02/03, an nginx reverse proxy on a Strato box (`85.214.103.161`) in front
of a public bucket. [`STRATO-EXIT.md`](STRATO-EXIT.md) is the step-by-step record of that move and
lists what's still to clean up. `scripts/aws-infra.sh` is read-only: `status` reports what exists
in AWS and what is live.

## Deploying

```sh
cp scripts/.env.deploy.example scripts/.env.deploy   # nothing to fill in; see below
./scripts/deploy.sh                                   # dry run — prints changes, uploads nothing
./scripts/deploy.sh --apply                           # publish
./scripts/deploy.sh --apply --no-build                # publish an existing dist/
```

`DEPLOY_BUCKET` and `CF_DISTRIBUTION_ID` both come from the `CvHostSite` stack outputs, so
neither needs setting. Setting `DEPLOY_BUCKET` would override that, and could publish to the old
`cv-host` bucket while it still exists. After uploading, the script issues a CloudFront
invalidation for `/`, `/index.html` and `/data/*`. The hashed assets under `static/` are immutable
for a year and never need one.

The script builds, then uploads in three passes, deliberately ordered so the live `index.html` never
references hashed assets that have not landed yet:

| Pass | What | Cache-Control |
| --- | --- | --- |
| 1 | `dist/static/*` — content-hashed JS/CSS/fonts | `public,max-age=31536000,immutable` |
| 2 | `dist/data/*` and everything else | `public,max-age=300` |
| 3 | `index.html`, last | `no-cache,must-revalidate` |

Before any `--delete` sync it refuses to continue unless `dist/index.html`, `dist/static/` and a
non-empty `dist/data/` all exist — a broken or partial build would otherwise delete the CV PDFs that
`/qualifications` links to. Afterwards it re-fetches the live site and prints the real response headers
rather than trusting an exit code.

Rollback: check out the previous commit and re-run `./scripts/deploy.sh --apply`. The bucket is
versioned, and old versions are kept for 30 days, so a single overwritten file can also be restored
from its previous version without a rebuild.

## Environment traps

**Zscaler TLS interception.** Corporate machines run traffic through Zscaler. macOS trusts its root but
the AWS CLI bundles its own CA store that does not, so every call dies with
`CERTIFICATE_VERIFY_FAILED`. Build a bundle containing the corporate root and point `AWS_CA_BUNDLE` at
it; `scripts/deploy.sh` auto-detects `~/.aws/corporate-ca.pem`:

```sh
security find-certificate -a -p /System/Library/Keychains/SystemRootCertificates.keychain \
  > ~/.aws/corporate-ca.pem
security find-certificate -a -c "Zscaler" -p /Library/Keychains/System.keychain \
  >> ~/.aws/corporate-ca.pem
```

Never work around this with `--no-verify-ssl`: that sends credentials over a connection you have not
verified.

**Temporary credentials.** Both AWS profiles (`default`, `automation`) authenticate with
`aws_session_token`, so they expire. `InvalidClientTokenId` means refresh them via your usual
interactive login — deploys use `default`, region `eu-central-1`.

## History

The live infrastructure is `infra/` (2026). Two earlier attempts are in git history; neither is live.

- `9915c15` (2021) — CDK v1 TypeScript: a VPC plus a private bucket with the deployment commented out.
  It never served anything. A static site needs no VPC.
- Deleted in `8dd7cd4` — a Java CDK stack provisioning **Amplify Hosting**, git-connected to
  `colla69/CVHost` with `appRoot: frontend`. That stack hardcoded a GitHub personal access token, which
  remains in this public repo's history and must be treated as compromised.
