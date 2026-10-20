# infra

CDK v2 (TypeScript) for the site's hosting. It is live. The input is `frontend/dist/`, a directory of
static files; the output is those files over HTTPS at the owner's domain, cheaply and safely. What a
newcomer gets wrong first: a content change never needs `cdk deploy`, and a careless one can take the
site offline.

**Start here:** `bin/cvhost.ts`. Architecture: `README.md`. Publishing and rollback: `../DEPLOYMENT.md`.

```
cv.colarietitosti.info ─┐
colarietitosti.info ────┼─ Route 53 A/AAAA aliases ─ CloudFront ─ OAC ─ private, versioned S3
www.colarietitosti.info ┘                             └─ CloudFront Function (viewer-request):
                                                         apex, www → 301 to cv; SPA deep links
```

## Map

| What | Where |
|---|---|
| The two stacks: `CvHostDns` (zone, `RETAIN`, termination-protected) and `CvHostSite` (bucket, certificate, function, distribution, aliases) | `lib/dns-stack.ts`, `lib/site-stack.ts` |
| The `cutover` gate that keeps the live aliases | read at `bin/cvhost.ts`, used in `site-stack.ts` for the A/AAAA aliases |
| SPA rewrite and the apex/www 301 | `functions/spa-fallback.js` |
| Cache and security headers | `site-stack.ts`: `CACHING_OPTIMIZED` plus the managed security-headers policy (no CSP), `PRICE_CLASS_100` |
| Stack outputs that `deploy.sh` reads (`BucketName`, `DistributionId`, …) | `site-stack.ts` |
| Records beyond the two stacks | `zone-records.txt`; empty on purpose, its header says why |

## Deploying

- **Content** goes out with `scripts/deploy.sh --apply`. It reads the bucket and distribution from the
  `CvHostSite` stack outputs, builds, refuses to sync with `--delete` unless `index.html`, `static/` and a
  non-empty `data/` exist, syncs in three phases with their own `Cache-Control`, invalidates the mutable
  paths, and fetches the live site. Without `--apply` it is a dry run that **still makes read-only AWS
  calls**, so it needs credentials.
- **Infrastructure** changes are `cdk deploy` in `infra/` (`npm --prefix infra run deploy:site` or
  `deploy:dns`). Run `cdk diff` first and show it.
- Auth is `aws login`: interactive, browser-based, and it expires. Never read `~/.aws/credentials`. Both
  scripts export `AWS_CA_BUNDLE` from `~/.aws/corporate-ca.pem` when that file exists. Never use
  `--no-verify-ssl`.
- The domain is registered at Route 53 Domains (moved from Strato 2026-10-03): auto-renew on, transfer
  lock on, privacy on, expires 2028-08-03.
- Two older attempts are in git history, both abandoned: `9915c15` (2021, CDK v1, a VPC nobody needed) and
  a Java CDK stack for Amplify Hosting, deleted in `8dd7cd4`. The latter hardcoded a GitHub token, which is
  still in this public repo's history.

## Gotchas

The general rules for this hosting, such as the certificate region, deep links, cache tiers, private
bucket access and checks after a deploy, are in the `aws-static-hosting` skill. What is true of this
project:

- This account defaults to `eu-central-1`, but both stacks are pinned to `us-east-1`, so the bucket is
  there too, although `deploy.sh`'s `AWS_REGION` defaults to `eu-central-1`.
- `createWebHistory` makes `/news` a real URL that must return `index.html`. That is
  `functions/spa-fallback.js`: it rewrites extensionless paths only.
- The hashed assets are under `/static/` (`assetsDir` in `frontend/vue.config.js`). `index.html` is not
  hashed. The PDFs under `data/` render in the `/qualifications` iframe, so they need `application/pdf`.
- The bucket has `RemovalPolicy.DESTROY`.
- **Don't change a Route 53 record's construct id.** CloudFormation creates the new record before deleting
  the old one, and the create fails because the name is taken. Moving a name between the two stacks takes
  two ordered deploys: delete in one, then create in the other. See the `SiteAlias` comment in
  `site-stack.ts`.
- `infra/package.json` names `bin/cvhost.js` in `"bin"`, which never exists. It is harmless.

## Claude Code

Auto mode refuses `cdk deploy` as a production deploy. Run `cdk diff`, show the result, and hand the owner the
deploy command to run with `!`, piped through `tee ~/<name>.log` so you can read the outcome. A `!` command that
prints nothing may not have run at all: check the stack status before reporting success.
