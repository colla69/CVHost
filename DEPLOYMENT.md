# Deployment

How cv.colarietitosti.info is actually served, and how to publish to it.

## Architecture

```
cv.colarietitosti.info
  └─ CNAME → colarietitosti.info → 85.214.103.161      (Strato box, nginx/1.14.0 Ubuntu)
       └─ reverse proxy → S3 bucket                    (the built site lives here)
```

No CloudFront, no Route 53, no Amplify. Responses come back with `Server: nginx` carrying S3's
`x-amz-id-2` / `x-amz-request-id` headers passed straight through, which is how you can tell the shape
of this setup from the outside.

DNS is registered and served by Strato (`ns3/ns4.stratoserver.net`). Nothing about a deploy touches DNS.

## Deploying

```sh
cp scripts/.env.deploy.example scripts/.env.deploy   # fill in DEPLOY_BUCKET
./scripts/deploy.sh                                   # dry run — prints changes, uploads nothing
./scripts/deploy.sh --apply                           # publish
./scripts/deploy.sh --apply --no-build                # publish an existing dist/
```

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

Rollback: there is no versioning on the bucket today, so the rollback is to check out the previous
commit and re-run `./scripts/deploy.sh --apply`. Enabling S3 bucket versioning would make this cheaper
and is worth doing.

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

## Known live defects

**Deep links 404.** `https://cv.colarietitosti.info/qualifications` returns 404. The router uses
`createWebHistory`, so that is a real URL that must return `index.html`, but the Strato nginx passes
S3's key-not-found straight through. Only in-app navigation from `/` works; a refresh or a shared link
fails.

This cannot be fixed from this repo. `frontend/public/.htaccess` is a leftover from the older
plain-Apache setup and `frontend/nginx.conf` belonged to the Docker image deleted in `8dd7cd4` —
neither is what serves production. The fix belongs in the nginx config **on the Strato host**, roughly:

```nginx
location / {
    proxy_pass https://BUCKET.s3.eu-central-1.amazonaws.com;
    proxy_set_header Host BUCKET.s3.eu-central-1.amazonaws.com;
    proxy_intercept_errors on;
    error_page 403 404 = @spa;
}

location @spa {
    proxy_pass https://BUCKET.s3.eu-central-1.amazonaws.com/index.html;
    proxy_set_header Host BUCKET.s3.eu-central-1.amazonaws.com;
}
```

Untested — written without sight of the real config on that host, and S3 returns 403 rather than 404
for missing keys when listing is denied, which is why both are intercepted.

**The live site is stale.** Live `index.html` is dated 2023-02-08. The first automated deploy will be a
large, visible jump, and will publish whatever is in the working tree — currently an unfinished
Vue 2 → Vue 3 / Vuetify 2 → 3 migration. Review before applying.

## History

Two abandoned infrastructure attempts are in git history; neither is live.

- `9915c15` (2021) — CDK v1 TypeScript: a VPC plus a private bucket with the deployment commented out.
  It never served anything. A static site needs no VPC.
- Deleted in `8dd7cd4` — a Java CDK stack provisioning **Amplify Hosting**, git-connected to
  `colla69/CVHost` with `appRoot: frontend`. That stack hardcoded a GitHub personal access token, which
  remains in this public repo's history and must be treated as compromised.
