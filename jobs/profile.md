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
  1. `tech-lead` — tech lead / team lead / lead engineer, ideally at a product company, owning a system
     end to end. The CV's profile closes on exactly this wish.
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
| seniority | lead / ownership explicitly in the role | senior IC | seniority unclear |
| remote | fully remote from Germany, stated | remote-first with travel, or EU-wide but unconfirmed for DE | only implied |
| domain | fintech, banking, regtech, leasing, automotive, health/public sector, industrial IoT — or needs Italian | general B2B SaaS / devtools | unrelated |
| company | product company with an in-house team and visible engineering culture | unknown | body-leasing consultancy (permanent list only) |

When the posting does not say, a criterion scores 1: "unknown" is neither evidence for nor against.

**Shortlist** at 7 or more, **kept** at 4–6, the rest **dropped** as `low-score`. In the
freelance list, agencies and consultancies are the normal channel and are not penalised.

## Open questions — asked at the intake, never guessed

> Minimum salary (permanent)? The repo is public: the answer can also be recorded as "asked, not stored".
> Minimum day rate (freelance)? Same choice.
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
| Language leverage | WebSearch with the terms above on LinkedIn job pages, StepStone, Indeed (.de and .it), InfoJobs.it and the employer boards; plus the description search in every feed | Not yet checked; the Italian boards may block fetching, in which case record `verified: false` |

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
