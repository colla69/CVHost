# Search profile

What `job-scout` looks for. The owner edits this file freely; the agent reads it at the start of every
sweep and records its git SHA in each run, so the history shows when the criteria changed. Lines starting
with `>` are open questions.

## Decided

Decided with the owner on 2026-09-28.

- **Contract:** permanent (Festanstellung) *and* freelance (Freiberufler). Always ranked in two separate
  lists — a salary and a day rate are not one scale.
- **Remote:** remote-first. Occasional onsite travel is fine — a few days a quarter. The employer must be
  able to employ, or contract, someone living in Munich, Germany: a German entity, an employer of record,
  or an explicit "EU"/"Europe"/"CET" hiring region. "Remote (US only)" and its relatives are out.
- **Lanes:**
  1. `tech-lead` — tech lead / team lead / lead engineer roles. **No preference over senior IC roles**
     (decided 2026-10-14): a lead title neither adds nor costs points; the label only sorts roles.
  2. `fullstack` — senior full-stack engineer, Java/Spring (or Jakarta EE) plus TypeScript with React,
     Angular or Vue. The core of thirteen years.
  3. `ai-eng` — AI-assisted engineering: developer productivity, AI enablement, LLM application and
     agentic tooling. Evidence: the eleven-agent review board around PlayCryptoWithAI
     (`writing/agentic-review-board.md`) and Copilot/Claude as a standing part of client delivery. Not
     ML research, not model training.
  4. `implementation` — implementation, integration or technical consulting at a product company: rolling
     its product out at customers in Germany, Italy or Switzerland, integration lead, technical project
     lead. Close to the consulting years, but on the vendor's side. **Only in scope with language
     leverage** (below); without it, it is `off-lane`.
- **Language leverage** (decided 2026-09-28): native Italian plus fluent German and English is an edge
  few candidates have, so it is searched for on purpose, not just noticed. A posting has it when it asks
  for Italian, as a requirement or a plus; `languages` then contains `it`. Three kinds count:
  engineering roles at companies whose customers, teams or offices span DACH and Italy (lanes 1–3 as
  usual); implementation and consulting roles for those markets (lane 4); and Italian employers or Italian
  subsidiaries hiring engineers who can work from Germany. Customer-facing sales roles (pre-sales, account
  management, sales engineering) are not wanted. The report lists these roles in their own table, whatever
  their contract or score.
- **Supporting keywords, not a lane:** AWS (Solutions Architect – Associate, valid to 04/2029),
  Terraform/CDK, Kubernetes, CI/CD.
- **Languages:** Italian (native), German, English — all three are working languages.
- **Pay floor** (decided 2026-10-14): **EUR 60,000 a year gross**. A role whose stated or projected pay
  tops out below that is dropped as `low-pay`, however well it fits. Freelance: a day rate that adds up to
  less over 200 billable days, so under EUR 300 a day (EUR 38 an hour).
- **Changing industry** (decided 2026-10-14): the owner wants out of banking. Banks, leasing and
  bank-owned finance score 0 on domain; the CV's banking years still count as evidence of skills, never as
  a reason a role fits.
- **Ranking** (decided 2026-10-14): by fit with the CV (the score), then by projected pay. Senior or lead
  does not matter.

## Pay projection

Every posting that is not dropped gets a `pay_estimate`: one figure (`point`), a range, a confidence and
the `basis` it rests on. It is a projection, never presented as a stated salary, and it is only as strong
as its sources. In order of strength:

1. **Stated in the posting** (salary range, "up to", the company's pay bands) → confidence `high`; `point`
   is the midpoint unless the posting says where in the band a senior lands.
2. **The same company's own figures:** its other postings with a stated range at the same level (Ashby
   `includeCompensation=true`, join.com, Personio, GermanTechJobs), its public salary bands or calculator
   (GitLab, Sourcegraph and others publish them), its kununu or Glassdoor salary page for the same title
   in Germany → `medium`, or `high` for a published band.
3. **The market for that role in Germany:** a published survey or salary report with its year (StepStone
   Gehaltsreport, Glassdoor/kununu title averages for Germany, levels.fyi for Germany/Berlin/Munich, the
   GermanTechJobs salary report), adjusted for company size and remote → `low`.

`basis` names each source with its figure and date ("Checkly Germany tier 88–105k stated in the ad";
"no range; kununu Senior Software Engineer at X, 12 entries, avg 74k, 2026"). Never average sources you did
not read, never round a guess into a confident figure, and when the sources disagree, widen the range and
lower the confidence instead of picking the flattering one. A USD, GBP or CHF figure is converted to EUR at
the day's rate, and for a pay-by-location company the Germany tier is used, not the US one.

## Search terms

Combine a lane term with a remote term. German postings say "remote" in many ways; search them all.

| Lane | English | German |
| --- | --- | --- |
| tech-lead | Tech Lead, Lead Software Engineer, Lead Developer, Engineering Lead, Team Lead Software | Teamleiter Softwareentwicklung, Technischer Teamleiter, Lead Entwickler, Tech Lead |
| fullstack | Senior Full Stack Engineer Java, Senior Software Engineer Java TypeScript, Senior Java Developer React/Angular/Vue | Senior Fullstack Entwickler Java, Senior Java Entwickler, Softwareentwickler Java Angular |
| ai-eng | Developer Productivity Engineer, AI Enablement, AI Engineer (LLM applications), Applied AI Engineer, Developer Experience AI, Agentic workflows | KI Engineer, AI Engineer (LLM), Entwickler KI-Anwendungen |

Remote terms: `remote`, `fully remote`, `remote-first`, `remote Germany`, `remote EU`, `100% remote`,
`Remote in Deutschland`, `deutschlandweit remote`, `mobiles Arbeiten 100%`, `Homeoffice 100%`.

### Language leverage

At least three queries in every sweep go here: the structured feeds almost never surface these roles (8
of 326 Arbeitnow rows mentioned Italian or Italy on 2026-09-28, one of them a remote engineering role).

| | Terms |
| --- | --- |
| German | `Italienisch` + Softwareentwickler / Java Entwickler / Tech Lead / Teamleiter; `Deutsch und Italienisch`; `Italienischkenntnisse` Entwickler; `DACH und Italien`; `Implementierung` / `Integration Consultant` / `Technischer Projektleiter` + Italienisch |
| English | `Italian speaking` + software engineer / tech lead / implementation consultant; `German and Italian` engineer; `DACH and Italy`; `Italian market` + engineer |
| Italian | `sviluppatore Java full remote`; `tech lead full remote`; `sviluppatore senior` + `tedesco`; `consulente tecnico` / `implementation specialist` + `tedesco`; `lavoro da remoto dalla Germania` |

Also, in every feed that carries description text (Arbeitnow, GermanTechJobs, HN, Himalayas, Remotive,
Remote OK, We Work Remotely), search the description for `italien|italian|italiano|italia|italy`. A role
found that way is recorded when it fits a lane, even if its title matched none of the lane terms.

An Italian employer still has to be able to hire from Germany: "remote, Italy only" is `not-germany`.

## Scoring

Hard filters first — any one drops the posting, with its `drop_reason`:

- `not-germany` — the posting's own location list leaves Germany out. "US only" is out even when the pay
  is "adjusted for other countries"; only an explicit Germany, EU/EEA/Europe/DACH/EMEA or worldwide counts.
- `onsite` — more than about five working days a quarter onsite ("a few days a quarter" is the ceiling).
- `too-junior` — junior or mid level.
- `ml-research` — ML research or model training at the core.
- `stack-mismatch` — the core stack is one the CV lacks entirely (Rails, .NET, systems-level Go or Rust,
  game engines, native mobile), however good the role looks otherwise.
- `off-lane` — none of the four lanes; an `implementation` role without language leverage; any sales role.
- `closed` / `stale` — closed, or first published more than ~60 days ago. A stated application deadline
  still in the future, or a re-advertisement by the company itself within the last 30 days (for example in
  this month's HN thread), makes it current again.
- `excluded-company` — on the avoid list.

**A shortlisted posting must be verified and explicitly open to Germany.** `ingest.py` refuses a
shortlist entry with `verified: false` or with no `DE`, EU/EEA/Europe/DACH/EMEA or worldwide in
`remote.countries`. Such a posting can be kept, never shortlisted.

Then 0–2 on each criterion, total 0–10:

| Criterion | 2 | 1 | 0 |
| --- | --- | --- | --- |
| stack | Java/Spring or TypeScript front end is the core | adjacent (Kotlin, Node-only, another JVM stack) | mostly foreign |
| seniority | senior, staff, lead or principal: all the same | level unclear | — (junior and mid are dropped) |
| remote | fully remote from Germany, stated | remote-first with travel, or EU-wide but unconfirmed for DE | only implied |
| domain | health, public sector, industrial IoT, automotive (not its finance arm), energy/climate — or needs Italian | general B2B SaaS, devtools, AI, e-commerce; fintech, payments or regtech that is not a bank | **banking**, leasing and bank-owned finance (devalued 2026-10-14: the owner wants to change industry); unrelated |
| company | product company with an in-house team and visible engineering culture | unknown | body-leasing consultancy (permanent list only) |

When the posting does not say, a criterion scores 1: "unknown" is neither evidence for nor against.

**Shortlist** at 7 or more, **kept** at 4–6, the rest **dropped** as `low-score`. Within a score, rank by
projected pay. In the
freelance list, agencies and consultancies are the normal channel and are not penalised.

## Open questions — asked at the intake, never guessed

> Freelance floor: EUR 300 a day is the EUR 60k floor spread over 200 days; it ignores the cost a freelancer
> carries themselves. Raise it? (Default: 300.)
> Notice period at msg — when could a new role start?
> Consultancies as a permanent employer: acceptable, or down-rank them? (Default: down-rank.)
> Companies or industries to avoid?
> Preferred company size — startup, scale-up, established?
> Travel limit — is "a few days a quarter" the ceiling?

## Sources

Checked 2026-09-28 from this machine; every sweep also records per-source status in its run file.

| Source | How | Notes |
| --- | --- | --- |
| Arbeitnow | `curl https://www.arbeitnow.com/api/job-board-api?page=N` | German-market feed, ~300 rows a page, few remote (15/326). Filter on `remote: true` and title |
| Himalayas | `curl 'https://himalayas.app/jobs/api/search?q=<term>&country=Germany'` | Has `locationRestrictions`, `timezoneRestrictions`, salary, seniority. Cursor pagination |
| Remotive | `curl 'https://remotive.com/api/remote-jobs?category=software-dev&search=<term>'` | `candidate_required_location` is often "USA" — check it. Terms forbid republishing: store the link only |
| Remote OK | `curl -A <browser UA> 'https://remoteok.com/api?tag=<tag>'` | First element is the legal notice; skip it. Terms ask for a link back — store the link |
| We Work Remotely | RSS `https://weworkremotely.com/categories/remote-full-stack-programming-jobs.rss` (also `…/remote-back-end-programming-jobs.rss`) | Region is in the item title/region field |
| GermanTechJobs | RSS `https://germantechjobs.de/rss` | ~600 items, German market, salary ranges stated |
| HN Who is hiring | `curl 'https://hn.algolia.com/api/v1/search_by_date?tags=story,author_whoishiring&hitsPerPage=3'` → thread id → `…/search?tags=comment,story_<id>&query=remote%20<term>&hitsPerPage=100` | Monthly thread; first line of a comment is usually `Company \| Role \| Location \| Remote` |
| Employer job boards | WebSearch `site:boards.greenhouse.io`, `site:jobs.lever.co`, `site:jobs.ashbyhq.com`, `site:jobs.personio.de`, `site:join.com` + lane term + remote Germany/EU | Postings straight from the company — the best source for the tech-lead lane |
| Big boards | WebSearch on LinkedIn job pages, StepStone, XING, Indeed.de, Welcome to the Jungle, Wellfound, EU Remote Jobs, Landing.jobs | Public pages only; many block fetching — then keep the search-result facts and mark `verified: false` |
| Freelance | freelancermap.de, GULP, freelance.de, Malt — public listings, via WebSearch or fetch | Not yet checked |
| Language leverage | WebSearch with the terms above on LinkedIn job pages, StepStone, freelancermap and the employer boards (Greenhouse, Lever, Ashby, Personio, Workable, join.com); plus the description search in every feed | First run 2026-09-28: low yield everywhere; freelancermap found the only real Italian-language engineering project. Indeed returns listing pages only; InfoJobs.it has shut down |

### Daily rotation

Decided 2026-10-14, when the routine went daily. Every sweep:

- **Feeds, every day:** Arbeitnow page 1, GermanTechJobs, Himalayas (2–3 lane queries), We Work Remotely
  (both feeds) and the current HN *Who is hiring* thread: one call per source and query, filtered
  locally, repeats skipped as the agent definition says.
- **Mondays only:** Remotive (17 jobs, rarely changes) and Remote OK (untagged feed, titles filtered locally).
- **WebSearch, 6–8 queries:** 2 on employer boards (Ashby, Lever, Greenhouse, Personio, join.com) for the
  tech-lead and ai-eng lanes, 1–2 on freelancermap, GULP or freelance.de for freelance, 2 on language
  leverage (rotate the English, German and Italian terms by day), 1 open query for whichever lane got
  least today. Verify through the boards' public APIs (see the source notes) rather than their HTML.
- **Google Jobs, 7 queries:** through `python3 jobs/tools/google_jobs.py` (SerpApi; key in `SERPAPI_KEY`).
  This is the route to LinkedIn, StepStone, XING and Indeed ads, which Google Jobs indexes. One query per
  lane plus language leverage, each with a remote term; rotate the wording by day. 7 a night stays inside
  the free plan's 250 searches a month, and the script refuses to go past 240. Log it as source
  `google-jobs`, method `api`, one entry in `queries` per search spent.
- **Retired**, since no posting came of them: Indeed (.de/.it), InfoJobs.it, StepStone and LinkedIn
  aggregate pages through WebSearch. A LinkedIn `/jobs/view/…` link found some other way may still be opened.

### Source notes

Kept by the agent: which sources stopped working, changed shape, or proved worthless — with the date.

- 2026-09-28 — all seven APIs and feeds above answered 200 from this machine. Not yet checked from the cloud
  routine's network.
- 2026-09-28 (first sweep) — **Remotive**: the free API now holds only 17 jobs in total (`total-job-count: 17`)
  and ignores `category` and `search`; every query returns the same rows. It asks for at most 4 calls a day.
  Close to worthless for this search: one call per sweep is enough.
- 2026-09-28 — **Arbeitnow**: pages now mix arbeitnow.co.uk/.fr/.ch postings (UK, France, Switzerland) into the
  German feed; ~325 rows a page, and page 2 already reaches back a day. Remote rows: 17 + 6 of 651.
- 2026-09-28 — **Himalayas**: the search endpoint returns 20 rows and no `nextCursor`, despite the API comment
  announcing cursor pagination; only page 1 per query is reachable without the deprecated `offset`.
  `q=developer productivity` returns 0. Employer-posted jobs live on Himalayas itself, so its job page counts
  as the posting.
- 2026-09-28 — **Remote OK**: `tag=` returns mostly non-tech rows carrying unrelated tags; the tag filter is
  unreliable. Use the untagged feed and filter titles locally; most rows have no usable location.
- 2026-09-28 — **We Work Remotely**: the `region` field says "Anywhere in the World" even when the `country` field
  is US-only; trust `country`. The back-end feed had only 5 items. Posting pages return 403 to WebFetch, so
  WWR rows cannot be verified from the posting.
- 2026-09-28 — **GermanTechJobs**: items have no location field; remote policy appears only in the description
  text, and most lane-titled roles say nothing about it. The feed's links drop umlauts (`Haggenmller`), so copy
  them, never rebuild them.
- 2026-09-28 — **HN Who is hiring**: `search?tags=comment,story_<id>&hitsPerPage=1000` returns the whole thread in
  one call (256 top-level posts in September); filter on `parent_id` locally. No October thread yet on 09-28.
  Linked Greenhouse (`boards-api.greenhouse.io/v1/boards/<board>/jobs/<id>`) and Workable
  (`apply.workable.com/api/v2/accounts/<acct>/jobs/<shortcode>`) APIs are the reliable way to verify HN roles;
  Workable's HTML page does not render.
- 2026-09-28 (language-leverage sweep, first run of the WebSearch sources) — **InfoJobs.it has shut down**: its
  posting pages show only "Questa piattaforma è ufficialmente chiusa e non più disponibile", while search engines
  still index old listings. Drop it from the language-leverage sources.
- 2026-09-28 — **Indeed (.de and .it)** through WebSearch returns only search-result pages (`q-…-jobs.html`,
  `offerte-lavoro`), never a posting, and one result was an hCaptcha page. Worthless for this search as it stands.
- 2026-09-28 — **StepStone** through WebSearch returns mostly category pages; one real posting in nine results and
  `site:` queries ignore "Italienisch". Low yield; not fetched yet.
- 2026-09-28 — **LinkedIn** job pages: WebSearch returns mostly aggregate pages ("62 Jobs für Italian Speaking in
  Deutschland"), and the Italian-speaking postings that surface are sales, support or years old. Public
  `/jobs/view/…` pages do open with WebFetch and show "no longer accepting applications", so they can be verified.
- 2026-09-28 — **Employer boards via `site:`** (Personio, Greenhouse, Lever, Ashby, join.com): the search ignores the
  language term and returns remote German postings instead, several already gone (Greenhouse 404, Lever "Document
  not found"). Their public APIs are the fast way to verify: Lever `api.lever.co/v0/postings/<acct>/<id>?mode=json`,
  Ashby `api.ashbyhq.com/posting-api/job-board/<board>?includeCompensation=true` (a whole board, with location,
  workplace type, publish date, pay and full text, in one call), Greenhouse `boards-api…/jobs?content=true`,
  Workable `apply.workable.com/api/v1/widget/accounts/<acct>` for a board. Personio posting pages open with WebFetch.
- 2026-09-28 — **freelancermap** via WebSearch was the one source that surfaced a real Italian-leverage engineering
  project. Project pages open with WebFetch but show no posting date; the stated start date is the only age signal.
- 2026-09-28 — **Open WebSearch** (no `site:`): mostly aggregators (Glassdoor, Jooble, remoterocketship, jobleads).
  The phrasing employers actually use is "Italian Speaker"/"Italian speaking" in the job title; that found
  TeamViewer's roles. Naming Italian employers (Satispay, Moneyfarm, Scalapay) finds their boards, but so far they
  hire engineers only in Milan. Aggregators: startup.jobs returns 403; freehire.me opens and links the original ad.
- 2026-09-28 — **Himalayas** `q=italian&country=Germany` returns non-engineering rows only; in other queries "Italy"
  matches `locationRestrictions`, so run the Italian regex on descriptions, not on location lists.
- 2026-10-14 (first daily sweep) — **curl refused**: every `curl -s` (Arbeitnow, GermanTechJobs, Himalayas, WWR, HN
  Algolia, Lever, Workable, Greenhouse, Personio, freelancermap, Remotive) failed with CONNECT 403 from that session's
  egress proxy. WebFetch reached all of them, but it reads only the first 100,000 characters: Arbeitnow page 1 gave 15
  of ~300 rows and GermanTechJobs 16 of ~600, and its epoch-to-date conversion was wrong for Arbeitnow. Himalayas
  (20 rows) and HN keyword searches (`query=europe`, `germany`, `lead remote`) fit. If curl stays blocked, query HN
  by keyword rather than `hitsPerPage=1000`.
- 2026-10-14 — **europelanguagejobs.com** ("jobs for English+German+Italian speakers", 669 listings): the first 20 are
  all sales, customer service or support. Not worth a query.
- 2026-10-14 (second sweep) — **news.ycombinator.com** item pages fail through WebFetch; `hn.algolia.com/api/v1/items/<id>`
  confirms a comment is still live. **Ashby** job pages render only with JavaScript, so check them through the board API.
  **remotely.de** surfaces German remote roles that other sources miss (Europace) and links the employer's softgarden page;
  its "nicht mehr verfügbar" flag does not always match the employer's board.
- 2026-10-14 — **WebSearch `site:jobs.ashbyhq.com`** returned no Ashby page, only copies on Arbeitnow, php-resource,
  Remotive and freelancermap; `site:jobs.lever.co` + "AI Engineer" returned only Jobgether reposts.
- 2026-10-15 — **curl works again** from this session's network. **GermanTechJobs** now answers a Cloudflare challenge
  (403) to a browser User-Agent but serves the RSS to curl's default one. Its ad pages are a JavaScript shell that answers
  200 for any path, so an http check proves nothing; whether the RSS still lists the ad is the usable check.
- 2026-10-15 — **Himalayas** and **We Work Remotely** posting pages answer 403 (Cloudflare challenge) to curl with any
  User-Agent; their search API and RSS still answer. Their links cannot get an http check.
- 2026-10-15 — **Google Jobs** (SerpApi), first night: of 8 searches, 4 timed out at `google_jobs.py`'s 60 s limit and 4
  returned "Google hasn't returned any results for this query", including the generic "Tech Lead Java Homeoffice".
  The script sends `gl=de`, `hl=en` and no `location`.
- 2026-10-15 — **HN October thread** read whole (`hitsPerPage=1000`) for the first time: it held roles the keyword-only
  reads of 2026-10-14 had missed (LiveKit, Mastra, GiveDirectly). Read it whole whenever curl works.
- 2026-10-15 — `watu.global/careers` redirects to an unrelated `.ru.com` domain; do not follow it.
- 2026-10-16 — **Google Jobs** with `location=Germany`: 1 of 7 searches returned results (10 jobs, for the short
  "Lead Software Engineer remote Deutschland"); the 6 longer ones, and all three language-leverage ones, got "Google hasn't
  returned any results for this query". Keep queries short. Hits link to aggregators (Zuhausejobs, JobLeads, Talent.com,
  remotely.de); the employer's Personio XML (`<company>.jobs.personio.de/xml`) was the way to the original ad, and it lists
  the employer's other open roles too (1KOMMA5°, Checkmk). zuhausejobs.com answers 403 to curl.
- 2026-10-16 — **Personio** job pages carry a JSON-LD `datePosted` that can be later than the XML's `createdAt`
  (1KOMMA5°: created 2026-02-27, datePosted 2026-08-19); record `datePosted` as `posted_at` and quote both in the evidence.
- 2026-10-16 — **Arbeitnow** pages of removed ads answer 410; **join.com** pages of removed ads answer 410 with
  `"status":"OFFLINE"`. Both are usable dead-link checks. Teamtailor boards expose `careers.<company>/jobs.rss`.
