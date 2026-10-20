# jobs

The owner's job search and its full history, kept by `job-scout`. It is not part of the site. The search is
public on purpose. A scheduled cloud task runs a sweep every night and **pushes one commit straight to
`master`, touching only `jobs/`**: pull before you commit.

**Start here:** `README.md` (the file formats and who writes what), then `tools/ingest.py` (its docstring).
Read `.claude/agents/job-scout.md` before running or changing a sweep.

## Rules

- **`runs/*.json` are history.** Never edit or reformat a committed sweep. `ingest.py` refuses to rewrite a
  git-tracked one.
- **`runs/*.md` and `postings.jsonl` are generated** by `python3 jobs/tools/ingest.py`. Never write them by
  hand. With no argument it validates every sweep and rewrites both files in place; give it a run file
  to ingest a new sweep.
- The routine never writes `tracker.md`. That file belongs to the owner and to interactive sessions.
- `CV/additionalInfo.md`, the owner's street address, phone number and birth date stay out of `jobs/`, out of
  search queries, and out of anything sent to a website.
- A shortlisted posting needs a link check by `http` or `api` (`SHORTLIST_CHECKS` in `ingest.py`). The pay
  floor is `PAY_FLOOR` in the same file. The schema is version 3; it still reads 1 and 2.
- `tools/google_jobs.py` reads `SERPAPI_KEY` from the environment or the repo-root `.env`. Never echo, log
  or write the key. Its `MONTHLY_BUDGET` counts only queries recorded in sweep JSON, so ad-hoc calls are
  not counted.
- Python here is standard library only, single-quoted, with no formatter or linter.
