#!/usr/bin/env bash
#
# Build CVHost and publish it to the S3 bucket that nginx (on the Strato box)
# reverse-proxies for cv.colarietitosti.info.
#
# Usage:
#   scripts/deploy.sh              # dry run — prints every change, uploads nothing
#   scripts/deploy.sh --apply      # perform the upload
#   scripts/deploy.sh --apply --no-build   # publish an already-built dist/
#
# Config, via environment or a git-ignored scripts/.env.deploy:
#   DEPLOY_BUCKET   (required)  target bucket, e.g. cv-colarietitosti-info
#   DEPLOY_PREFIX   (optional)  key prefix inside the bucket, default none
#   AWS_PROFILE     (optional)  default "default"
#   AWS_REGION      (optional)  default "eu-central-1"
#   CF_DISTRIBUTION_ID (optional) CloudFront distribution to invalidate after upload.
#
# DEPLOY_BUCKET and CF_DISTRIBUTION_ID both fall back to the outputs of the
# CvHostSite CDK stack (infra/), so after the migration neither needs setting.

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DIST="$ROOT/frontend/dist"

[ -f "$ROOT/scripts/.env.deploy" ] && . "$ROOT/scripts/.env.deploy"

# This machine sits behind Zscaler TLS interception. macOS trusts the proxy root
# but the AWS CLI ships its own CA bundle that does not, which surfaces as
# CERTIFICATE_VERIFY_FAILED on every call. Use the exported bundle if present.
if [ -z "${AWS_CA_BUNDLE:-}" ] && [ -f "$HOME/.aws/corporate-ca.pem" ]; then
  export AWS_CA_BUNDLE="$HOME/.aws/corporate-ca.pem"
fi

APPLY=0
BUILD=1
for arg in "$@"; do
  case "$arg" in
    --apply)    APPLY=1 ;;
    --no-build) BUILD=0 ;;
    *) echo "unknown argument: $arg" >&2; exit 2 ;;
  esac
done

export AWS_PROFILE="${AWS_PROFILE:-default}"
export AWS_REGION="${AWS_REGION:-eu-central-1}"

# The bucket and the distribution are outputs of the CDK stack in infra/.
# Reading them from CloudFormation means there is no resource name duplicated
# into a config file to drift, and a redeploy that replaces either one is picked
# up here without anyone editing anything. The stack is in us-east-1 because
# CloudFront reads its certificate from there; the bucket sync uses AWS_REGION.
stack_output() {
  aws cloudformation describe-stacks --region us-east-1 --stack-name CvHostSite \
    --query "Stacks[0].Outputs[?OutputKey=='$1'].OutputValue|[0]" --output text 2>/dev/null \
    | grep -v '^None$' || true
}

[ -n "${DEPLOY_BUCKET:-}" ]      || DEPLOY_BUCKET="$(stack_output BucketName)"
[ -n "${CF_DISTRIBUTION_ID:-}" ] || CF_DISTRIBUTION_ID="$(stack_output DistributionId)"

if [ -z "${DEPLOY_BUCKET:-}" ]; then
  echo "ERROR: no DEPLOY_BUCKET, and the CvHostSite stack has no BucketName output." >&2
  echo "       Either set DEPLOY_BUCKET in scripts/.env.deploy, or deploy the stack:" >&2
  echo "         npm --prefix infra run deploy:site" >&2
  echo "       If you expected the stack to exist, check your credentials first." >&2
  exit 1
fi

PREFIX="${DEPLOY_PREFIX:-}"
PREFIX="${PREFIX#/}"; PREFIX="${PREFIX%/}"
TARGET="s3://${DEPLOY_BUCKET}${PREFIX:+/$PREFIX}"

if [ "$APPLY" -eq 1 ]; then DRY=(); else DRY=(--dryrun); fi

say() { printf '\n\033[1m==> %s\033[0m\n' "$1"; }

say "Target ${TARGET}  (profile=${AWS_PROFILE} region=${AWS_REGION})"
[ "$APPLY" -eq 1 ] || echo "DRY RUN — nothing will be uploaded. Re-run with --apply to publish."

# --- identity check: fail early and loudly rather than mid-upload -------------
if ! aws sts get-caller-identity --output text --query Account >/dev/null 2>&1; then
  echo "ERROR: AWS credentials are not usable for profile '${AWS_PROFILE}'." >&2
  aws sts get-caller-identity >/dev/null 2>"$ROOT/.deploy-auth-error" || true
  sed 's/^/       /' "$ROOT/.deploy-auth-error" >&2; rm -f "$ROOT/.deploy-auth-error"
  echo "       InvalidClientTokenId usually means the temporary session token expired" >&2
  echo "       — both profiles here use aws_session_token. Refresh your credentials." >&2
  echo "       CERTIFICATE_VERIFY_FAILED means AWS_CA_BUNDLE is not pointing at the" >&2
  echo "       corporate root (see ~/.aws/corporate-ca.pem)." >&2
  exit 1
fi

# --- build -------------------------------------------------------------------
if [ "$BUILD" -eq 1 ]; then
  say "Building"
  npm --prefix "$ROOT/frontend" run build
fi

# --- sanity gates: never sync --delete against a broken or partial build ------
say "Checking build output"
[ -f "$DIST/index.html" ] || { echo "ERROR: $DIST/index.html missing — build did not produce a site." >&2; exit 1; }
[ -d "$DIST/static" ]     || { echo "ERROR: $DIST/static missing — assetsDir output not found." >&2; exit 1; }

data_count=$(find "$DIST/data" -type f 2>/dev/null | wc -l | tr -d ' ')
if [ "$data_count" -lt 1 ]; then
  echo "ERROR: $DIST/data is empty. Syncing with --delete would remove the CV PDFs" >&2
  echo "       that the /qualifications page links to. Aborting." >&2
  exit 1
fi
echo "index.html + static/ present, ${data_count} files under data/"

# --- upload ------------------------------------------------------------------
# Order matters: assets first, index.html last, so the live index never points
# at hashed files that have not landed yet.

say "1/3  Hashed assets  ->  ${TARGET}/static  (immutable, 1 year)"
aws s3 sync "$DIST/static" "${TARGET}/static" \
  --cache-control 'public,max-age=31536000,immutable' \
  --delete "${DRY[@]}"

say "2/3  Documents and other files (5 min cache)"
aws s3 sync "$DIST" "$TARGET" \
  --exclude 'static/*' --exclude 'index.html' \
  --cache-control 'public,max-age=300' \
  --delete "${DRY[@]}"

say "3/3  index.html (never cached)"
aws s3 cp "$DIST/index.html" "${TARGET}/index.html" \
  --cache-control 'no-cache,must-revalidate' \
  --content-type 'text/html; charset=utf-8' "${DRY[@]}"

# --- invalidate CloudFront ---------------------------------------------------
# S3 is the origin, not the thing visitors hit, so a fresh upload is invisible
# until the edge caches are told. index.html is no-cache and data/ is 5 minutes,
# but the hashed assets under static/ are immutable-for-a-year, so a targeted
# invalidation of the two mutable prefixes is enough and stays inside the
# 1000-free-paths-per-month allowance.
if [ "$APPLY" -eq 1 ] && [ -n "${CF_DISTRIBUTION_ID:-}" ]; then
  say "Invalidating CloudFront ${CF_DISTRIBUTION_ID}"
  inv=$(aws cloudfront create-invalidation \
          --distribution-id "$CF_DISTRIBUTION_ID" \
          --paths '/' '/index.html' '/data/*' \
          --query 'Invalidation.Id' --output text)
  echo "invalidation ${inv} submitted; edges typically catch up within a minute"
elif [ "$APPLY" -eq 1 ]; then
  say "No CloudFront distribution configured"
  echo "While nginx on the Strato box still proxies the bucket directly there is no"
  echo "edge cache to invalidate. Once CvHostSite is deployed this resolves itself"
  echo "from the stack outputs -- see infra/README.md."
fi

# --- verify the live site, not the exit code ---------------------------------
if [ "$APPLY" -eq 1 ]; then
  say "Verifying https://cv.colarietitosti.info/"
  curl -sSI --max-time 20 https://cv.colarietitosti.info/ \
    | grep -iE '^(HTTP|Last-Modified|Cache-Control|ETag|Content-Type)' || true

  deep=$(curl -sS -o /dev/null -w '%{http_code}' --max-time 20 https://cv.colarietitosti.info/qualifications || echo "000")
  echo
  if [ "$deep" = "200" ]; then
    echo "Deep link /qualifications -> 200"
  else
    echo "NOTE: deep link /qualifications -> ${deep}."
    if [ -n "${CF_DISTRIBUTION_ID:-}" ]; then
      echo "      CloudFront is serving this site, so the SpaFallback function should have"
      echo "      rewritten this to /index.html. Check it is still associated with the"
      echo "      default cache behaviour in infra/lib/site-stack.ts."
    else
      echo "      Expected while the Strato box still proxies the bucket: its nginx passes"
      echo "      S3's key-not-found straight through. CloudFront fixes this at the cutover;"
      echo "      uploading files here cannot."
    fi
  fi
else
  say "Dry run complete — no changes were made."
fi
