# Leaving Strato

Goal: cancel the Strato contract completely — server and domain — without signing up for the
domain-only contract Strato offers (≈ €2.50/month, paid upfront). Everything moves to the AWS
account that already holds the DNS zone.

```
                         today                               after this plan
registrar                Strato (Cronon GmbH)                Route 53 (Amazon Registrar)
DNS                      Route 53  ✓ already done            Route 53
cv.colarietitosti.info   Strato nginx → public S3 bucket     CloudFront → private S3
apex, www                Strato box                          301 → https://cv.colarietitosti.info/
admin, nextcloud, …      Strato box (wildcard + 8 records)   gone
Strato box               85.214.103.161                      cancelled
```

## Two rules that override everything else

1. **Strato won't release the Auth-Code until you cancel,** so cancelling comes first, at 1.3. That
   cancellation sets a date. **The transfer (1.7) and the DNS cleanup (Phase 3) must both be done
   before that date.** A contract that ends with the domain still at Strato either deletes the
   domain or bills you for the domain-only contract.
2. **Remove every DNS record pointing at 85.214.103.161 before the box is switched off.** Strato
   gives the IP to another customer. A record still pointing at it lets that customer serve
   pages, and get a valid certificate, for `<name>.colarietitosti.info`.

## Where things stand (checked 2026-10-02)

| | |
| --- | --- |
| Nameservers | Route 53 (`ns-*.awsdns-*`) since 2026-09-17: runbook steps 1–4 of `infra/README.md` are done |
| Registrar | Cronon GmbH (Strato). Expires **2027-08-03**. Status `active`, so no transfer lock to remove |
| DNSSEC / mail | none / no MX records, so there's no mailbox to move |
| `cv` | still Strato nginx, `/qualifications` → 404 |
| Box | serves the apex and admin, collabora, dash, grafana, homestats, nextcloud, plex, ssh |

## Timeline

```
done        Phase 2  cv → CloudFront (2026-10-02)
done        Phase 1  Strato cancelled, domain at Route 53, locked, private (2026-10-03)
done        Phase 3  box records gone, apex + www → 301 to cv (2026-10-03)
by 11-01    Phase 0  back up whatever you want off the box: by IP now, no name points at it
2026-11-02  Phase 4  the Strato account closes, and the box goes with it
after       Phase 5  cleanup
```

Helpers used below (there's no `dig` on this machine):

```sh
dns()  { curl -s "https://cloudflare-dns.com/dns-query?name=$1&type=${2:-A}" -H 'accept: application/dns-json' \
           | python3 -c 'import json,sys; print(" ".join(a["data"] for a in json.load(sys.stdin).get("Answer",[])) or "NONE")'; }
rdap() { curl -sL https://rdap.identitydigital.services/rdap/domain/colarietitosti.info | python3 -c '
import json,sys; d=json.load(sys.stdin)
print("registrar:", [x[3] for e in d["entities"] if "registrar" in e["roles"] for x in e["vcardArray"][1] if x[0]=="fn"])
print("expires:  ", [e["eventDate"] for e in d["events"] if e["eventAction"]=="expiration"])
print("status:   ", d["status"])'; }
```

---

## Phase 0: Inventory and backups (nothing changes)

**0.1 Inventory the box.** Run as root, or add `sudo`:

```sh
ssh <you>@85.214.103.161 '
  hostname; uptime; df -h /
  grep -rh server_name /etc/nginx/sites-enabled/ | sort -u
  systemctl list-units --type=service --state=running --no-pager --plain | head -60
  command -v docker >/dev/null && docker ps --format "{{.Names}}\t{{.Image}}\t{{.Status}}"
  crontab -l 2>/dev/null; ls /etc/cron.d
  du -sh /var/www /srv /opt /var/lib/docker /home/* 2>/dev/null
' | tee ~/strato-inventory.txt
```

**0.2 Decide each service.** "Drop" means no backup.

| Name | What it is | Drop / keep data / move | Backup verified |
| --- | --- | --- | --- |
| apex, www | serves a page today | **decided:** 301 to `cv` (Phase 3) | n/a |
| admin | | | |
| nextcloud | | | |
| grafana | | | |
| dash | | | |
| plex | | | |
| collabora | | | |
| homestats | | | |
| ssh | the box itself | goes with the box | |

**0.3 Back up whatever you keep**, onto this machine. Configs as cheap insurance:

```sh
mkdir -p ~/strato-backup
ssh <you>@85.214.103.161 'sudo tar czf - /etc /var/www 2>/dev/null' > ~/strato-backup/etc-www.tgz
# Nextcloud user files, if kept: rsync the data dir; dump its database too if you want shares/calendars
# rsync -a <you>@85.214.103.161:<nextcloud-data>/<user>/files/ ~/strato-backup/nextcloud/
```

Open at least one file from each backup. A backup you've never read back doesn't count.

**Gate:** every row in 0.2 has a decision, and the backups have been read back.

---

## Phase 1: Transfer the domain to Route 53 ✅ transferred 2026-10-03

Strato was cancelled at 1.3. **The account closes on 2026-11-02**, and that's the deadline for
Phase 0 and Phase 3. On 2026-10-03 the registry already showed `Amazon Registrar, Inc.`, expiry
2028-08-03, status `transfer period` (the normal five-day grace period after a transfer), and the
nameservers unchanged. **1.5 is done**: contact reachability shows `DONE`. **1.8 is done,
2026-10-03:** auto-renew on, transfer lock on (`clientTransferProhibited`), WHOIS privacy on for
the registrant, admin and tech contacts.

Nothing visible changes. The nameservers already point at Route 53, and the transfer only changes
who bills you.

**1.1** `! aws login`

**1.2** Price and transferability:

```sh
aws route53domains list-prices --region us-east-1 --tld info
aws route53domains check-domain-transferability --region us-east-1 --domain-name colarietitosti.info
```

Expect `TRANSFERABLE`. Expect the price to be around $25–28, which also covers the next year.

**1.3 At Strato: cancel, then request the Auth-Code.** Strato issues no code for an active,
uncancelled domain ([Strato FAQ](https://www.strato.de/faq/domains/authinfo-code/)).
1. Strato login → cancel the package (Kündigung). For `colarietitosti.info` choose **transfer to
   another provider (Providerwechsel)**, never delete/release. **Decline the domain-only offer.**
   Write down the end date the form shows. Everything in Phase 0, 1 and 3 has to fit before it.
   If that date is under ~2 weeks away, stop and rethink before confirming.
2. Then **Domains → Domainverwaltung → "Authcode anfordern"** next to the domain. The code arrives
   within 24 hours at the domain owner's email. Without this request, Strato only sends it
   automatically 30 days before the end date, so don't wait for that.
- **Don't edit the registrant name or email at Strato.** A registrant change can trigger a
  60-day transfer lock. Fix your contact details at Route 53 afterwards.
- Type the code straight into the Route 53 console. Don't paste it into a chat: whoever holds it
  can move the domain.

**1.4 Route 53 console → Domains → Registered domains → Transfer in → Single domain:**
- domain `colarietitosti.info`, plus the Auth-Code
- name servers: **Import name servers from a Route 53 hosted zone that has the same name as the
  domain**, then pick the `CvHostDns` zone
- contacts: your details, **privacy protection on**
- **auto-renew on**

AWS bills the transfer immediately. Expiry moves out a year, to **2028-08-03**.

**1.5 Watch the registrant mailbox:**
- Route 53 sends a contact-verification mail. Click it: an unverified registrant gets the domain
  suspended after 15 days.
- Strato may send a "confirm outgoing transfer" mail or panel prompt. Approving it skips the
  ~5-day automatic wait.

**1.6 Track it:**

```sh
aws route53domains list-operations --region us-east-1
aws route53domains get-operation-detail --region us-east-1 --operation-id <id>
```

**1.7 Done when the registry says so:** `rdap` prints `Amazon Registrar, Inc.` and an expiry in
2028. **This has to happen before the end date written down at 1.3.**

**1.8 Lock it down:**

```sh
aws route53domains get-domain-detail --region us-east-1 --domain-name colarietitosti.info \
  --query '{autoRenew:AutoRenew,privacy:RegistrantPrivacy,status:StatusList,expires:ExpirationDate}'
# if StatusList lacks clientTransferProhibited:
aws route53domains enable-domain-transfer-lock --region us-east-1 --domain-name colarietitosti.info
```

**If it fails:** a rejected transfer (wrong code, Strato refused) costs nothing and changes nothing.
Get a fresh code and resubmit.

---

## Phase 2: Move `cv` to CloudFront (runbook steps 5–8 of `infra/README.md`) ✅ done 2026-10-02

The cutover ran at 09:40 UTC. Verified afterwards: Route 53, Cloudflare and Google all resolve
`cv` to CloudFront. The response carries `via: … cloudfront` and HSTS. `/qualifications`, `/news`,
`/contact` and `/projects` return 200. A real PDF returns 200 `application/pdf`. A missing PDF
returns 403. `http://` redirects to `https://`. The apex and the other subdomains still point at
the box, as intended. 2.7 is done too: `cdk diff CvHostSite` without the flag shows no
differences.

**2.1–2.4 were done on 2026-09-17, everything except the switch.** That session deployed
`CvHostSite`, uploaded the site into the new bucket (87 objects, the same as `cv-host`), checked
it at https://d3ta37wujukxlb.cloudfront.net/ (`/qualifications` → 200), and then stopped to ask
before switching. The answer never came. Re-checked 2026-10-02: the CloudFront copy still serves
200, and nothing under `frontend/` has changed since the upload, so there's nothing to redeploy.

**2.5** The switch: `npx --prefix infra cdk deploy CvHostSite -c cutover=true`

**2.6** Verify:

```sh
curl -sI https://cv.colarietitosti.info/ | grep -iE '^(HTTP|via|x-cache)'             # via: … cloudfront
curl -s -o /dev/null -w '%{http_code}\n' https://cv.colarietitosti.info/qualifications  # 200
```

**2.7 Make the cutover the default.** Add `"cutover": true` to `context` in `infra/cdk.json` and
commit it. Otherwise any later `cdk deploy CvHostSite` without the flag deletes the `cv` alias.
Today the wildcard would quietly catch that. After Phase 3, `cv` would simply stop resolving.

**Rollback:** `npx cdk deploy CvHostSite -c cutover=false` hands `cv` back to the box through the
wildcard. That only works until Phase 3 removes the wildcard.

---

## Phase 3: Take the box out of DNS ✅ deployed 2026-10-03

Deployed 2026-10-03, 10:59–11:06 local time, from the preview in 3.2 with no surprises. Verified
afterwards. Route 53 answers NXDOMAIN for admin, nextcloud, ssh and any other name, and the apex
and `www` resolve to CloudFront. Cloudflare and Google had already caught up. `https://` and
`http://` on the apex and `www` return 301 to `https://cv.colarietitosti.info/<path>`, and `cv`
still serves `/`, `/qualifications` and the PDFs with 200. The zone now holds 11 records: NS,
SOA, three alias pairs, three ACM validation CNAMEs.

**The box is now only reachable by IP:** `ssh <you>@85.214.103.161`. `ssh.colarietitosti.info` is
gone.

This needs a code change. Do it while the box is still paid for. It doubles as a scream test:
anything you forgot breaks while you can still reach the server.

**3.1 Code changes: written 2026-10-03, not yet deployed.**
- `infra/zone-records.txt`: empty. The apex `A`, the `*` wildcard and all eight subdomains are
  gone, and the header explains why none of them may come back.
- `infra/lib/site-stack.ts` + `infra/bin/cvhost.ts`: the certificate and the distribution also
  cover `colarietitosti.info` and `www.colarietitosti.info`, with A + AAAA aliases for each. The
  `cv` records keep their construct ids, so they aren't touched.
- `infra/functions/spa-fallback.js`: apex and `www` return a 301 to
  `https://cv.colarietitosti.info` + the path, ahead of the SPA rewrite. Only those two hosts
  redirect, so the `*.cloudfront.net` name still serves the site.
- `scripts/aws-infra.sh verify` says "nothing to compare" for an empty list instead of a
  meaningless all-clear.
- Checked offline: `tsc` and `cdk synth` are clean. The generated function was run in node
  against apex, `www`, mixed-case host, `cv` deep link, PDF, static asset and the
  `cloudfront.net` host, and every case did the right thing.

**3.2 Preview, then deploy.** After `! aws login`:

```sh
cd /home/cola/IdeaProjects/CVHost/infra && npx cdk diff CvHostDns CvHostSite
```

Expect exactly this: `CvHostDns` removes 10 records. `CvHostSite` replaces the certificate
(adds the two names), updates the distribution aliases and the function, and adds 4 alias
records. **Nothing else, and in particular no change to the two `cv` records.** Then:

```sh
! cd /home/cola/IdeaProjects/CVHost/infra && npx cdk deploy CvHostDns CvHostSite
```

`CvHostDns` deploys first because `CvHostSite` depends on it. So the old apex `A` is gone before
the apex alias takes its name, and the apex resolves to nothing for the few minutes in between.
The new certificate validates itself through the zone, and the distribution update takes
5–10 minutes. `cv` keeps serving throughout.

**3.3 Verify.** The old records had a 3600 s TTL, so allow up to an hour:

```sh
for h in colarietitosti.info www.colarietitosti.info; do curl -sI https://$h/ | grep -iE '^(HTTP|location)'; done
#   → 301, location: https://cv.colarietitosti.info/
for h in admin nextcloud grafana dash plex collabora homestats ssh anything; do printf '%-10s ' $h; dns $h.colarietitosti.info; done
#   → NONE for every one
dns colarietitosti.info   # CloudFront IPs, not 85.214.103.161
```

**3.4** Live without the box for a few days.

**Rollback:** revert the commit, then deploy **`CvHostSite` first**, each stack on its own:
`npx cdk deploy CvHostSite --exclusively`, then `npx cdk deploy CvHostDns --exclusively`. The
apex alias has to be gone before the old apex `A` can take the name back. A plain two-stack
deploy runs `CvHostDns` first and fails on exactly that clash. Only roll back while the box still
exists, before 2026-11-02.

---

## Phase 4: Let the Strato contract run out

You cancelled at 1.3. **Before the end date:** 1.7 shows `Amazon Registrar, Inc.`, 3.3 is clean,
and nothing is missing from the backups. If the transfer is still pending a few days before the
end date, chase it. Approving it at Strato speeds it up (1.5).

**4.1** Once the box is gone:
- `ssh-keygen -R 85.214.103.161`
- confirm that no further invoice arrives
- delete the Strato customer account if they let you

---

## Phase 5: Clean up

**5.1 Delete the old world-readable bucket `cv-host`** (eu-central-1), once CloudFront has served
`cv` for a while. First confirm the stack's bucket is a different one:

```sh
aws cloudformation describe-stacks --region us-east-1 --stack-name CvHostSite \
  --query "Stacks[0].Outputs[?OutputKey=='BucketName'].OutputValue" --output text   # must NOT be cv-host
aws s3 rb s3://cv-host --force    # irreversible
```

**5.2 Budget alarm** (optional, free for the first two): Billing → Budgets → monthly cost budget,
$5, email alert. It catches a surprise long before the invoice does.

**5.3 Docs: done 2026-10-03.** `CLAUDE.md`, `DEPLOYMENT.md`, `infra/README.md`, `scripts/deploy.sh`,
`scripts/.env.deploy.example`, and the `aws-deployer`, `vue-expert` and `code-quality-reviewer`
agents now describe the CloudFront setup. Still open: deleting the dead `frontend/public/.htaccess`
and `frontend/nginx.conf`.

---

## What it costs afterwards

| Item | Per year |
| --- | --- |
| `.info` at Route 53 | ~$25–28 (first charge at 1.4 covers the year to 2028-08-03) |
| Hosted zone | $6 |
| CloudFront, ACM certificate | $0, far inside the free tier |
| S3 | under $1 |
| **Total** | **~$32–35** |

Strato's domain-only offer alone would be ~€30 a year, paid upfront, on top of a box you no
longer need.
