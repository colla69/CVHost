# jobs/ — the job search, and its history

Everything the `job-scout` agent (`.claude/agents/job-scout.md`) finds, and everything the owner decides
about it. The agent runs twice a week as a cloud routine and on demand; every run is kept, so the history
can be analysed later.

## Files and who writes them

| Path | Written by | What it is |
| --- | --- | --- |
| `profile.md` | owner + agent (intake) | What to look for: decisions, lanes, keywords, open questions, source notes |
| `vocab.json` | owner + agent, deliberately | The controlled vocabularies below |
| `runs/<run_id>.json` | agent, once | One sweep, complete. **Immutable once committed** |
| `runs/<run_id>.md` | `tools/ingest.py` | The same sweep as a readable report — generated, never hand-edited |
| `postings.jsonl` | `tools/ingest.py` | One line per unique posting ever seen — derived from `runs/`, rebuilt on every ingest |
| `tracker.md` | owner + agent, interactive only | The owner's pipeline: shortlisted → applied → interview → … |
| `leads/<id>.md` | agent, on request | Deep dive on one posting |
| `applications/<id>/` | agent / cv-strategist, on request | Cover letter, outreach message, tailored CV |

The routine writes `runs/`, `postings.jsonl` and, when a source stops working, the *Source notes* in
`profile.md`. It never touches `tracker.md`, so an owner's status update and a scheduled commit cannot
conflict.

## The routine

A claude.ai cloud routine on the owner's account, not something in this repo: its schedule, prompt, model
and environment live there, and it clones `master` on every run, so changes to the agent or the profile
take effect without touching it. It runs Monday and Thursday at **02:07 Europe/Berlin**. The time is
deliberate: a run draws on the owner's subscription limits, and the five-hour usage window it opens has
closed by about 07:00, before the working day. It runs in a cloud environment with **Full** network
access (the default "Trusted" allowlist blocks job boards). Its prompt, kept here so the repo shows what
it does (editing this copy does not change the routine):

> Use the job-scout agent (`.claude/agents/job-scout.md`) to run a scheduled sweep exactly as its
> definition describes — unattended, `trigger: routine`, no questions. When it returns, run
> `python3 jobs/tools/ingest.py` (no argument) and confirm it passes. Stage only paths under `jobs/`,
> check with `git status --short` that nothing outside `jobs/` is staged, and commit with the message
> `job-scout: sweep <run_id> (<n> shortlisted)`. Push to `master`. If that push is rejected, push the same
> commit to `claude/job-sweep-<run_id>` instead and say so. Finish with the report path, the shortlist
> counts per contract, and any source that was blocked.

Manage it with `/schedule list`, `/schedule run` (a sweep now) and `/schedule update`, or at
claude.ai/code/routines.

## A sweep: `runs/<run_id>.json`, schema 1

`run_id` is the UTC start time as `YYYY-MM-DDTHHMMZ`, so file names sort chronologically. After writing a
new sweep, run

```sh
python3 jobs/tools/ingest.py jobs/runs/<run_id>.json
```

It sets every posting's `id`, fixes key order, validates the file against this schema and `vocab.json`,
writes `runs/<run_id>.md` and rebuilds `postings.jsonl`. It exits non-zero and names every problem if the
sweep is invalid. With no argument it re-checks every sweep and rebuilds the index without rewriting
anything. It refuses to rewrite a sweep that is already committed.

```jsonc
{
  "schema": 1,
  "run_id": "2026-10-01T0507Z",
  "started_at": "2026-10-01T05:07:00Z",
  "finished_at": "2026-10-01T05:41:00Z",
  "trigger": "routine",                 // vocab: trigger
  "agent_sha": "a1b2c3d",               // git log -1 --format=%h -- .claude/agents/job-scout.md
  "profile_sha": "d4e5f6a",             // same for jobs/profile.md; "+dirty" suffix if uncommitted edits
  "summary": "Two to five sentences: what stood out, what changed since the last sweep.",
  "open_questions": ["Things only the owner can answer."],
  "sources": [{
    "name": "himalayas",
    "method": "api",                    // vocab: source_method
    "queries": ["q=java&country=Germany"],
    "results": 120,                     // rows the source returned
    "evaluated": 9,                     // of those, recorded in postings[] below
    "status": "ok",                     // vocab: source_status
    "note": ""
  }],
  "postings": [{
    "id": "acme--senior-full-stack-engineer-java-react",   // set by ingest.py
    "url": "https://…",                 // the posting itself, not a search page
    "source": "himalayas",              // a sources[].name
    "company": "Acme GmbH",
    "title": "Senior Full Stack Engineer (Java/React) (m/w/d)",
    "lane": "fullstack",                // vocab: lane
    "contract": "permanent",            // vocab: contract
    "seniority": "senior",              // vocab: seniority
    "remote": {
      "type": "remote",                 // vocab: remote_type
      "countries": ["DE", "EU"],        // ISO 3166 alpha-2, or vocab: regions
      "timezone": "CET±2",              // free text or null
      "onsite_days_per_quarter": 2,     // null when the posting does not say
      "eor": false                      // hires abroad through an employer of record; null if unknown
    },
    "salary": { "stated": true, "min": 85000, "max": 100000, "currency": "EUR", "period": "year" },
                                        // or just { "stated": false } — with verified: true that means the
                                        // posting states no pay; with verified: false, nobody read it
    "languages": ["en", "de"],          // every language the posting asks for, required or a plus;
                                        // ISO 639-1. "it" marks language leverage
    "stack": ["java", "spring", "react", "aws"],   // vocab: stack
    "domain": ["fintech"],              // vocab: domain
    "posted_at": "2026-09-24",          // or null
    "verified": true,                   // the agent opened the posting itself and confirmed the terms
    "verdict": "shortlisted",           // vocab: verdict
    "drop_reason": null,                // vocab: drop_reason, only when verdict is "dropped"
    "score": { "stack": 2, "seniority": 2, "remote": 2, "domain": 1, "company": 1, "total": 8 },
                                        // null only for a posting dropped before scoring
    "why": "One line, citing CV evidence.",
    "gap": "One line, honest."
  }]
}
```

What goes into `postings[]`: every listing the agent looked at because its title matched a lane —
**including the ones it dropped**, with the reason. Rows a source returned that never matched a lane are
only counted in `sources[].results`. Posting descriptions are never copied in: the sources' terms forbid
republishing them, and the link is enough.

A posting's `id` is `<company>--<title>`, slugged, with legal suffixes (GmbH, AG, Ltd …) and German gender
tags (`(m/w/d)`) removed. The same role reposted keeps its id, which is what lets the history say how long
a role stayed open.

## The report: `runs/<run_id>.md`

Generated by `ingest.py` from the sweep, and regenerated for every sweep on each ingest, so a better
layout reaches old reports too. From top to bottom:

1. **Language leverage:** every role that passed the filters and asks for Italian, whatever its contract
   or score.
2. **One table per contract** (permanent, freelance, and unknown if any): every other role scoring 6 or
   more. Columns: score with its breakdown (stack · level · remote · domain · company), role with company,
   source and whether it is new, lane and level, remote terms, pay, stack, languages, posting date, why it
   fits, the gap. Bold scores are shortlisted.
3. **Kept, below 6:** one line each.
4. **Dropped:** counted by reason.
5. **Sources and open questions.**

## `postings.jsonl`

One JSON object per line, sorted by first sighting:
`id, company, title, url, lane, contract, first_seen, last_seen, seen_in[], last_verdict, best_score`.
It is derived data: delete it and `python3 jobs/tools/ingest.py` rebuilds it from `runs/`.

## Controlled vocabularies — `vocab.json`

`stack`, `domain`, `drop_reason` and every enum above only accept values listed in `vocab.json`; the
agent maps aliases onto them (`Spring Boot` → `spring-boot`, `JavaEE` → `jakarta-ee`, `Vue.js` → `vue`).
When a posting genuinely needs a new tag, the vocabulary is extended in the same commit and the sweep's
summary says so. Never rename or remove a value: older sweeps use it.

## Changing the schema

Bump `SCHEMA` in `tools/ingest.py`, keep it able to read the old version, and describe the change here.
Old sweeps are not migrated in place — they are history.

## Reading the history

```python
import json, glob
runs = [json.load(open(f)) for f in sorted(glob.glob('jobs/runs/*.json'))]
postings = [dict(p, run_id=r['run_id']) for r in runs for p in r['postings']]
```

Or `duckdb -c "select * from read_json('jobs/runs/*.json')"`. Questions the data is shaped for: which
stacks are rising per lane, what share of "remote" roles exclude Germany, salary ranges by lane and
contract, how long a role stays posted, which sources earn their place, how the shortlist moved when the
profile changed (`profile_sha`).
