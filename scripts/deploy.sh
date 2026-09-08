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

: "${DEPLOY_BUCKET:?set DEPLOY_BUCKET (in scripts/.env.deploy or the environment)}"
export AWS_PROFILE="${AWS_PROFILE:-default}"
export AWS_REGION="${AWS_REGION:-eu-central-1}"

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
    echo "      Expected until the SPA fallback is fixed. The nginx that proxies to this"
    echo "      bucket runs on the Strato host, not in this repo, and must rewrite unknown"
    echo "      paths to /index.html. Uploading files here cannot fix it."
  fi
else
  say "Dry run complete — no changes were made."
fi
