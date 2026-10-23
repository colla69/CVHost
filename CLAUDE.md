# CVHost

The owner's personal CV / portfolio site: a fully static Vue 3 SPA, live at
https://cv.colarietitosti.info/ and public at github.com/colla69/CVHost. The repo also holds the CV as
markdown, the CDK code that hosts the site, and the owner's job-search archive. **Everything in it is
public**: no secret and nothing private about the owner goes in, and `jobs/` is public on purpose.

Components have their own `CLAUDE.md`, which loads when a file in that folder is read:
`frontend/`, `infra/`, `CV/`, `jobs/`. The site's three-language rules are in `.claude/rules/`.

## Stack

- Vue 3 (Options API), Vuetify 3, vue-router 4, built by vue-cli 5 on webpack. JavaScript, no TypeScript.
  No backend, no store, no HTTP client: the backend was deleted in `8dd7cd4`, and every byte the site
  serves ships from this repo.
- npm, with the lockfile in `frontend/`. No Node version is pinned: no `.nvmrc`, no `engines` field.
- Infra: AWS CDK v2 in TypeScript (`infra/`). Two stacks, both in `us-east-1`.
- Python 3, standard library only, for `jobs/tools/` and `CV/md2typst.py`. The CV PDFs render with typst.
- **No test suite** (no runner, no `test` script, no test files) and **no CI**. Deploys are run by hand.

## Commands

Run npm from the repo root as `npm --prefix frontend run <script>`. No `cd` needed.

| Task | Command |
|---|---|
| Dev server, hot reload, :8080 | `npm --prefix frontend run serve` (not verified) |
| Build into `frontend/dist/` (the gitignored, deployable artifact) | `npm --prefix frontend run build` |
| Lint, report only | `npm --prefix frontend run lint -- --no-fix` |
| Lint and **auto-fix** (the default) | `npm --prefix frontend run lint` |
| Type-check the CDK code | `npx --prefix infra tsc --noEmit -p infra` (no infra lint exists) |
| Publish content to production | `scripts/deploy.sh --apply`; see `infra/CLAUDE.md` |
| Render the three CV PDFs | `CV/build-pdf.sh`; it **writes into `frontend/public/data/`** |
| Validate job data, rebuild its index | `python3 jobs/tools/ingest.py`; it **writes in place**, with no check-only mode |

Verification is lint clean, a successful build, and looking at the affected page in a browser. Say what
you ran. Don't claim more than you checked.

## Map

| Component | Responsibility | Start here |
|---|---|---|
| `frontend/` | The whole application, and the static data it serves | `frontend/src/main.js`, then `router.js` |
| `infra/` | CDK: DNS, CloudFront, private S3, certificate | `infra/bin/cvhost.ts`, `infra/README.md` |
| `scripts/` | `deploy.sh` (content out), `aws-infra.sh` (read-only diagnostics) | `scripts/deploy.sh` |
| `CV/` | The master CV, its German and Italian versions, the PDF pipeline | `CV/CV.md`, `CV/build-pdf.sh` |
| `jobs/` | The job search and its full history | `jobs/README.md`, `jobs/tools/ingest.py` |
| `writing/` | One standalone essay; the site does not render it | — |

Docs: `DEPLOYMENT.md` (publish and roll back), `infra/README.md` (architecture), `jobs/README.md`,
`STRATO-EXIT.md` (the history of leaving the old host).

**Dead weight. Do not run or repair it unless asked:** `frontend/pom.xml` and `frontend/node/`, a Maven
build wrapper pinned to node v12. Its stale `target/` output and the unused Docker image were deleted on
2026-10-03.

## Conventions

- The site is in English, German and Italian. Every user-visible string exists in all three: see
  `.claude/rules/trilingual-site.md`.
- Two-space indent, single quotes, no semicolons. eslint enforces it in `frontend/`; by habit it holds in
  `infra/` and `infra/functions/spa-fallback.js` too, where nothing enforces it.
- Comments say why, and often record the dated incident behind a rule ("on 2026-10-14 …"). Match that.
- Branch name `feature/aws` is left over from abandoned infrastructure attempts (`DEPLOYMENT.md`, History).

## Boundaries

- **`v-html` is for strings the owner wrote by hand.** Never route anything from outside the repo into it.
- **Fonts stay self-hosted** in `frontend/public/fonts/`. Never use a Google Fonts CDN link: it would
  send every visitor's IP to Google, which this site deliberately avoids. Images are local too, never
  hot-linked.
- **Never deploy `CvHostSite` with `-c cutover=false`** unless you mean to take the site offline.
  `"cutover": true` in `infra/cdk.json` keeps the live aliases.
- **Never point a DNS record at the old Strato box** (`85.214.103.161`). It serves nothing here and goes
  away with the Strato account on 2026-11-02.
- Production deploys are the owner's to run: preview with `cdk diff`, and see `infra/CLAUDE.md`.
- Never commit a secret. A token sits in this repo's history from a deleted CDK stack. `.env`,
  `scripts/.env.deploy` and `CV/additionalInfo.md` are gitignored for that reason.

## Trios

| Task | Run it with | Agents |
|---|---|---|
| Change the site: features, fixes, content, styling | `/change` | `change-plan`, `change-do`, `change-check` |

## How knowledge is kept here

This file, the `CLAUDE.md` files in `frontend/`, `infra/`, `CV/` and `jobs/`, and `.claude/rules/` hold the
facts. They load by themselves when a file in that area is read. The agent files under `.claude/agents/` hold the
process. An agent's file only loads when that agent is started as a subagent, and many sessions don't
start one, cloud project threads included. So:

- **Before working in an agent's area, read its file yourself** even if you won't start it: job search →
  `job-scout.md` and `jobs/README.md`; hosting → `aws-deployer.md`; CV text → `cv-strategist.md`.
- **Cloud project threads load only this file and the root `.claude/rules/` without `paths:`.** Per the
  docs checked on 2026-10-23, they skip `.claude/settings.json` (plugins, hooks, permissions),
  `.claude/agents/`, `.claude/skills/` and the `CLAUDE.md` files in subfolders. A thread on 2026-10-23
  started with no plugins installed. Plugins reach threads only through the project's settings
  (Plugins); local sessions on the owner's machine get the full setup.
- **The repo is the only memory every session shares.** Claude memory may be off and chat history does
  not reach new sessions. When you learn a fact a later session would otherwise have to rediscover (how
  something outside the repo was set up, who can change it, why a step failed), write it into the file
  for its subject in the same PR.
- **The repo states intent, not live state.** Scheduled tasks, cloud environments, secrets and GitHub
  access are settings on the owner's account. Check them with a tool before promising anything that
  depends on them, and never write a planned change into a doc as done before it is.
- **Check before you claim, in every answer.** A time, a count, a status, a cause or a limit you tell the
  owner is either checked first (say what you checked) or plainly marked as a guess. Limits you set
  yourself (search budgets, caps) are your choices, not facts: say so, and fit them to what the owner
  asked for. The owner asked for this on 2026-10-15 after a made-up run time and a promised nightly run
  that was switched off.

## Job search: the scheduled tasks

`job-scout` runs on demand (`claude --agent job-scout`, or `@agent-job-scout`) and as a scheduled cloud
task meant to run every night at 02:07 Berlin time. The rules for `jobs/` are in `jobs/CLAUDE.md`.

The scheduled tasks live on the owner's claude.ai account, not in this repo, and who can change one
depends on how it was made. Claude can only edit a scheduled task that the same kind of tool created:

- **`job-scout sweep`** (`trig_01QJBZG6WBiStVKEjUzcj3EF`) was created on 2026-09-28 by Claude Code on the
  owner's computer, with `/schedule`, through the API. A cloud session cannot edit, enable or run it;
  only the owner at claude.ai/code/routines or Claude Code on their computer (`/schedule update`) can.
  On 2026-10-15 it was off, still on its old Mon/Thu schedule and prompt.
- **`job-scout daily sweep`** (`trig_01Go9nbvEsAdDwfBmSRrQfGL`) was created on 2026-10-15 by a cloud
  thread in the owner's claude.ai project. Because each run starts a new project thread, a cloud
  session can only switch it off or run it as it is; any other change (schedule, prompt, name) means
  deleting it and creating it again. It runs in that project's cloud environment, which is where
  `SERPAPI_KEY` has to be set for it.

Whether a task is on, its schedule and its environment are live state that no file here can show: check
with the scheduled-task tools before saying a run will happen.

## Agents

The `change` trio (`/change`) does changes to the site: `change-plan` proposes and waits, `change-do` is the
only one that writes, `change-check` compares the result with the plan. It replaced `vue-expert` and
`code-quality-reviewer`. Five specialists remain in `.claude/agents/`, each carrying deeper context than
this file:

- `unit-tester` — tests; knows no harness exists and must agree on one before installing anything
- `aws-deployer` — hosting and deployment; confirms before any mutating AWS call
- `cv-strategist` — the CV as a document: story, wording, recruiter/ATS keywords, and whether the CV
  and the site still agree, in all three languages. Edits the `CV/` markdown and writes the site's German
  and Italian text (text values only); every other site fix goes to the `/change` trio
- `job-scout` — finds and vets remote permanent and freelance roles, scores them against the CV, keeps
  the history in `jobs/`. Never applies or contacts anyone
- `job-reviewer` — re-checks a new job-scout sweep before the owner reads it: live links, Germany, pay
  projection and CV claims; demotes or corrects postings in that sweep only
