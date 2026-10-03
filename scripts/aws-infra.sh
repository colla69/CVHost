#!/usr/bin/env bash
#
# Diagnostics for the CVHost AWS hosting. This script READS; it provisions
# nothing.
#
# Provisioning lives in infra/ as a CDK app -- see infra/README.md. An earlier
# version of this script created the certificate, distribution and hosted zone
# imperatively; that was replaced so there is exactly one way to change
# infrastructure and one place that describes what exists.
#
# Usage:
#   scripts/aws-infra.sh status            # what exists in AWS and what is live
#   scripts/aws-infra.sh verify [file]     # diff the new Route 53 zone against live DNS
#                                          # default file: infra/zone-records.txt
#
# Config, from scripts/.env.deploy (git-ignored) or the environment:
#   SITE_DOMAIN   default cv.colarietitosti.info
#   ZONE_DOMAIN   default colarietitosti.info
#   AWS_PROFILE / AWS_REGION   default "default" / eu-central-1

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

[ -f "$ROOT/scripts/.env.deploy" ] && . "$ROOT/scripts/.env.deploy"

# Same Zscaler TLS-interception workaround as deploy.sh.
if [ -z "${AWS_CA_BUNDLE:-}" ] && [ -f "$HOME/.aws/corporate-ca.pem" ]; then
  export AWS_CA_BUNDLE="$HOME/.aws/corporate-ca.pem"
fi

SITE_DOMAIN="${SITE_DOMAIN:-cv.colarietitosti.info}"
ZONE_DOMAIN="${ZONE_DOMAIN:-colarietitosti.info}"
export AWS_PROFILE="${AWS_PROFILE:-default}"
export AWS_REGION="${AWS_REGION:-eu-central-1}"

# The CDK app is pinned to us-east-1: CloudFront reads certificates from there
# and nowhere else. Its stacks are queried in that region regardless of the
# region used for the bucket sync.
CDK_REGION="us-east-1"
SITE_STACK="CvHostSite"
DNS_STACK="CvHostDns"

say()  { printf '\n\033[1m==> %s\033[0m\n' "$1"; }
info() { printf '    %s\n' "$1"; }
warn() { printf '\033[33m    ! %s\033[0m\n' "$1"; }
die()  { printf '\033[31mERROR: %s\033[0m\n' "$1" >&2; exit 1; }

require_creds() {
  aws sts get-caller-identity >/dev/null 2>&1 || {
    aws sts get-caller-identity 2>&1 | sed 's/^/    /' >&2
    die "AWS credentials for profile '${AWS_PROFILE}' are not usable. Run: aws login"
  }
}

stack_output() {
  # stack_output <stack> <OutputKey> -> value, or empty if the stack/key is absent
  aws cloudformation describe-stacks --region "$CDK_REGION" --stack-name "$1" \
    --query "Stacks[0].Outputs[?OutputKey=='$2'].OutputValue|[0]" --output text 2>/dev/null \
    | grep -v '^None$' || true
}

find_zone() {
  aws route53 list-hosted-zones-by-name --dns-name "$ZONE_DOMAIN" \
    --query "HostedZones[?Name=='${ZONE_DOMAIN}.']|[0].Id" --output text 2>/dev/null \
    | grep -v '^None$' | sed 's|/hostedzone/||' || true
}

# ---------------------------------------------------------------- status ----
cmd_status() {
  require_creds

  say "Identity"
  aws sts get-caller-identity --output table

  say "CDK stacks (region ${CDK_REGION})"
  for s in "$DNS_STACK" "$SITE_STACK"; do
    st="$(aws cloudformation describe-stacks --region "$CDK_REGION" --stack-name "$s" \
          --query 'Stacks[0].StackStatus' --output text 2>/dev/null || echo 'NOT_DEPLOYED')"
    printf '    %-14s %s\n' "$s" "$st"
  done

  local bucket dist_id dist_domain
  bucket="$(stack_output "$SITE_STACK" BucketName)"
  dist_id="$(stack_output "$SITE_STACK" DistributionId)"
  dist_domain="$(stack_output "$SITE_STACK" DistributionDomainName)"
  [ -n "$bucket" ] && info "bucket:       $bucket"
  [ -n "$dist_id" ] && info "distribution: $dist_id  ($dist_domain)"

  say "Route 53 hosted zones for ${ZONE_DOMAIN}"
  aws route53 list-hosted-zones-by-name --dns-name "$ZONE_DOMAIN" \
    --query "HostedZones[?Name=='${ZONE_DOMAIN}.'].[Id,ResourceRecordSetCount]" \
    --output table 2>&1 | sed 's/^/    /'
  local zid; zid="$(find_zone)"
  if [ -n "$zid" ]; then
    info "nameservers to set at the registrar:"
    aws route53 get-hosted-zone --id "$zid" --query 'DelegationSet.NameServers' --output text \
      | tr '\t' '\n' | sed 's/^/      /'
  fi

  say "Nameservers the internet actually uses right now"
  curl -s -m 10 -H 'accept: application/dns-json' \
    "https://cloudflare-dns.com/dns-query?name=${ZONE_DOMAIN}&type=NS" \
    | python3 -c "import sys,json;d=json.load(sys.stdin);[print('      '+a['data']) for a in d.get('Answer',[])]" 2>/dev/null \
    || info "      (lookup failed)"

  say "ACM certificates in ${CDK_REGION} for ${SITE_DOMAIN}"
  aws acm list-certificates --region "$CDK_REGION" \
    --query "CertificateSummaryList[?DomainName=='${SITE_DOMAIN}'].[CertificateArn,Status]" \
    --output table 2>&1 | sed 's/^/    /'

  say "Live site"
  curl -sSI --max-time 15 "https://${SITE_DOMAIN}/" 2>&1 \
    | grep -iE '^(HTTP|server|last-modified|cache-control|x-amz-|x-cache|via|strict-transport)' \
    | sed 's/^/    /' || true
  local deep
  deep="$(curl -sS -o /dev/null -w '%{http_code}' --max-time 20 "https://${SITE_DOMAIN}/qualifications" || echo 000)"
  info "deep link /qualifications -> ${deep}$([ "$deep" = 200 ] && echo '' || echo '   (CloudFront fixes this at cutover)')"
}

# ---------------------------------------------------------------- verify ----
# The pre-cutover gate: for every name in the zone file, work out where it
# ACTUALLY lands in the new Route 53 zone and where it lands today through
# Strato, then compare those. Run it while Strato is still authoritative -- a
# mismatch here is a subdomain that would go dark at the nameserver change.
#
# Comparing raw record data is not good enough. A name served by the wildcard
# answers as a CNAME chain (name -> apex -> 85.214.103.161) while the same name
# as an explicit A record answers 85.214.103.161 directly. Same destination,
# different shape. Flagging those as differences would make the gate cry wolf on
# most of the zone, so both sides are followed to their final addresses first.
cmd_verify() {
  require_creds
  local file="${1:-$ROOT/infra/zone-records.txt}"
  [ -f "$file" ] || die "no such zone file: $file"
  local zid; zid="$(find_zone)"
  [ -n "$zid" ] || die "No Route 53 hosted zone for ${ZONE_DOMAIN}. Deploy it: npm --prefix infra run deploy:dns"

  say "Route 53 ${zid} (not yet live)  vs  Strato (live today)"
  ZONE_DOMAIN="$ZONE_DOMAIN" ZONE_ID="$zid" python3 - "$file" <<'PY_EOF'
import json, os, subprocess, sys, urllib.request

zone = os.environ['ZONE_DOMAIN']
zone_id = os.environ['ZONE_ID']

def route53_answer(name, rtype):
    """What the new zone would answer, before it is authoritative."""
    out = subprocess.run(
        ['aws', 'route53', 'test-dns-answer', '--hosted-zone-id', zone_id,
         '--record-name', name, '--record-type', rtype,
         '--query', 'RecordData', '--output', 'json'],
        capture_output=True, text=True, timeout=60)
    if out.returncode != 0:
        return []
    try:
        return json.loads(out.stdout or '[]')
    except json.JSONDecodeError:
        return []

def is_ipv4(value):
    parts = value.split('.')
    return len(parts) == 4 and all(p.isdigit() and 0 <= int(p) <= 255 for p in parts)

def route53_addresses(name, hops=4):
    """Follow CNAMEs inside the zone down to the A records they end at.

    test-dns-answer returns only the record type asked for -- querying A against
    a name served by a CNAME (the wildcard, for instance) comes back NOERROR
    with empty data rather than chasing the target. So ask for A, and when that
    is empty ask for CNAME and follow it by hand."""
    for _ in range(hops):
        data = route53_answer(name, 'A')
        if data and all(is_ipv4(d) for d in data):
            return sorted(data)
        target = route53_answer(name, 'CNAME')
        if not target:
            return []
        name = target[0].rstrip('.')
    return []

def live_addresses(name):
    """What the world resolves today, through Strato. DoH follows CNAMEs for us,
    so take only the A records out of the answer chain."""
    req = urllib.request.Request(
        f'https://cloudflare-dns.com/dns-query?name={name}&type=A',
        headers={'accept': 'application/dns-json'})
    try:
        d = json.load(urllib.request.urlopen(req, timeout=10))
    except Exception:
        return None
    return sorted(a['data'] for a in d.get('Answer', []) if a['type'] == 1)

records = []
for raw in open(sys.argv[1]):
    line = raw.split('#', 1)[0].strip()
    if not line:
        continue
    parts = line.split(None, 3)
    if len(parts) < 4:
        continue
    records.append(parts[0])

if not records:
    print('    The zone file lists no records, so there is nothing to compare.')
    print('    The cv, apex and www aliases belong to CvHostSite; check those with: status')
    sys.exit(0)

print('    %-36s %-22s %-22s' % ('NAME', 'ROUTE 53', 'LIVE NOW'))
mismatches = 0
for name in records:
    if name == '@':
        probe = zone
    elif name == '*':
        # A wildcard cannot be queried literally; probe a name it would match.
        probe = 'wildcard-probe.' + zone
    else:
        probe = f'{name.rstrip(".")}.{zone}'

    new = route53_addresses(probe)
    live = live_addresses(probe)
    same = new == live and new != []
    if not same:
        mismatches += 1
    print('  %s %-36s %-22s %-22s' % (
        ' ' if same else '*', probe,
        ','.join(new) or '-', ','.join(live) if live is not None else '(lookup failed)'))

print()
if mismatches == 0:
    print('    Every name resolves to the same address it does today.')
    print('    Safe to change the nameservers at the registrar.')
else:
    print('    \033[33m! %d name(s) differ (marked *). Each is either intentional or a name' % mismatches)
    print('    ! about to go dark. Account for every one before touching the nameservers.\033[0m')
sys.exit(0)
PY_EOF
}

case "${1:-}" in
  status) cmd_status ;;
  verify) shift; cmd_verify "${1:-}" ;;
  *) sed -n '2,24p' "$0" | sed 's/^#//'; exit 2 ;;
esac
