# CVHost

The owner's personal CV / portfolio site: a fully static Vue 3 SPA, live at
https://cv.colarietitosti.info/ and public at github.com/colla69/CVHost.

## Layout

- `frontend/` holds the entire application. Run npm from the repo root as
  `npm --prefix frontend run <script>` — no `cd` needed.
- `frontend/src/components/` — one folder per page area: `CV/`, `news/`, `projectInfos/`, `Contact/`,
  plus top-level `Home.vue`, `Menu.vue` (app bar + drawer) and `SiteFooter.vue`.
- `frontend/public/fonts/` — the three self-hosted variable typefaces. Never swap these for a Google
  Fonts CDN link: it would send every visitor's IP to Google, which this site deliberately avoids.
- `frontend/public/data/` — CV PDFs, certificates and images, served as static files.
- `frontend/dist/` — build output, gitignored. This is the deployable artifact.
- `jobs/` — the owner's job search and its full history, kept by `job-scout`. Not part of the site; see
  "Job search" below and `jobs/README.md`.
- Dead weight, do not run or repair unless asked: `frontend/pom.xml` and `frontend/node/` (a Maven build
  wrapper pinned to node v12). Its stale `frontend/target/` output and the unused Docker image
  (`Dockerfile`, `docker-compose.yml`) were deleted on 2026-10-03.

## Commands

| Command | What it does |
| --- | --- |
| `npm --prefix frontend run serve` | dev server, hot reload, :8080 |
| `npm --prefix frontend run build` | production build into `frontend/dist/` |
| `npm --prefix frontend run lint` | eslint over `src/` — **auto-fixes by default** |
| `npm --prefix frontend run lint -- --no-fix` | report only, no writes |

There is **no test suite** — no runner, no `test` script, no test files. Verification is lint clean, a
successful build, and looking at the affected page in a browser. Don't claim more than you checked.

## Stack

Vue 3 (Options API), Vuetify 3, vue-router 4, vue-cli 5 on webpack. No backend, no store, no HTTP
client — the backend was deleted in `8dd7cd4` and every byte the site serves ships from this repo.

## Conventions

- `@/` resolves to `frontend/src/`.
- eslint is `@vue/standard`: two-space indent, single quotes, no semicolons.
- Components are Options API SFCs with `name`, `data()` and `<style scoped>`. Match that. Don't introduce
  `<script setup>` or the Composition API into a component that doesn't already use it — consistency
  beats modernity here. If a change genuinely needs it, raise it rather than deciding unilaterally.
- Vuetify is registered globally in `src/plugins/vuetify.js` (full component + directive import), so
  `<v-*>` tags need no import. Icons are MDI.

## Where content lives

Adding a news post or a project is a data edit, not a markup edit:

- News → `src/components/news/news.json`. The component reverses the array, so entries render
  newest-first; keep `id` ascending and unique. `description_text` renders through `v-html`.
- Projects → `src/components/projectInfos/project_infos.json`. `client` is the end customer (distinct
  from `company_name`, the employer) and is omitted where no source states it. `featured: true` puts an
  entry in the "Selected work" table on the home page — that table is curated by this flag, not by date.
- Images → all local under `public/img/`, credited in `public/img/CREDITS.md`. Nothing is hot-linked;
  keep it that way, and add a CREDITS row for anything new.
- Certificates → the `qualifications` array in `src/components/CV/Qualifications.vue`. Every entry needs
  `filename` (a PDF in `public/data/`) plus `issuer`, `year` and `group`, and a matching thumbnail at
  `public/img/certs/<name>.jpg`, rendered from the PDF with
  `magick -density 72 "data/X.pdf[0]" -background white -alpha remove -resize 520x -quality 76 img/certs/X.jpg`.
  Verify both files exist — a typo renders a broken tile.
- Bio, languages, work history → hardcoded in the matching `src/components/CV/*.vue`.

`CV/CV.md` (outside `frontend/`) is the master CV document, kept by hand. It tells the same story as the
`CV/` components and `project_infos.json`, and the two drift apart — the site is years older. Lines
starting with `>` in it are working notes, not CV content. Changing the story on one side means checking
the other; `cv-strategist` owns that.

`v-html` on those strings is fine only because the owner authors them by hand. Never route anything from
outside the repo into it.

## Routing and hosting

The router uses `createWebHistory`, so `/news` and friends are real URLs and the host must rewrite
unknown paths to `index.html`. In-app, unmatched paths redirect to `/` via the router catch-all.

**How production actually serves:** `cv.colarietitosti.info` → Route 53 alias → **CloudFront** →
private S3 bucket. `colarietitosti.info` and `www` answer from the same distribution with a 301 to
`cv`. The domain is registered at Route 53 too. All of it is CDK in `infra/`. See `DEPLOYMENT.md` to
publish and `infra/README.md` for the architecture.

The SPA rewrite is the CloudFront Function `infra/functions/spa-fallback.js`: any extensionless path
serves `index.html`, so deep links work and a new route needs no host change. A path whose last
segment contains a dot is treated as a file and fails if missing. There is no other rewrite
config: the old `.htaccess` and `nginx.conf` were deleted on 2026-10-03.

Before you touch routing or deploys:

- Content goes out with `scripts/deploy.sh --apply`. Infrastructure changes are `cdk deploy` in
  `infra/`: preview with `cdk diff` first. Auto mode refuses the deploy, so the owner runs it with `!`.
- `"cutover": true` in `infra/cdk.json` is what keeps the live aliases. Never deploy `CvHostSite`
  with `-c cutover=false` unless you mean to take the site offline.
- The old Strato box (`85.214.103.161`) serves nothing here and goes away with the Strato account on
  2026-11-02. Never point a record at it. `STRATO-EXIT.md` has the history and the leftovers.

There is no CI. Deploys are run by hand. The `feature/aws` branch name is left over from earlier,
abandoned infrastructure attempts (see `DEPLOYMENT.md`, History).

## Design system — "Ledger"

Every component reads colour and type from CSS custom properties defined in `src/app.css` under
`.v-theme--ledgerLight` / `.v-theme--ledgerDark`, which stay in step with the two Vuetify palettes in
`src/plugins/vuetify.js`. **Never hard-code a colour in a component** — use `var(--lg-ink)`,
`var(--lg-accent)`, `var(--lg-rule)` and friends so both themes keep working. Shared utility classes
(`lg-page`, `lg-inner`, `lg-eyebrow`, `lg-display`, `lg-heading`, `lg-prose`, `lg-tnum`) live there too.

The look is a ruled document: hairline bands, a 2px ink rule above each section heading, Archivo for
display, Public Sans for prose, JetBrains Mono for labels and figures, one petrol accent.

Layout is mobile-first and was measured at 320px and 390px with no horizontal overflow. There is
deliberately no `overflow-x: hidden` guard, so a regression shows up instead of being masked.

## State of the tree

The Vue 2 → Vue 3 / Vuetify 2 → 3 migration is finished, and the 2026 redesign went over every
component. The leftovers this file used to warn about (unread `data()` state, Vuetify 2 component
names, debug `console.log`, the dead `infoList.vue`) are gone.

## Job search

`job-scout` searches for remote roles and records every sweep under `jobs/`. It runs on demand
(`claude --agent job-scout`, or `@agent-job-scout`) and as a cloud routine at 02:07 on Monday and
Thursday nights, which **pushes one commit straight to `master`**, touching only `jobs/`. Pull before you
commit.

`jobs/runs/*.json` are history: never edit or reformat a committed sweep. `jobs/runs/*.md` and
`jobs/postings.jsonl` are generated by `python3 jobs/tools/ingest.py`, never by hand. The routine
never writes `jobs/tracker.md`; that file belongs to the owner and interactive sessions. The job search
is public on purpose; `CV/additionalInfo.md` and the owner's contact details still stay out of `jobs/`.

## Agents

Six specialists live in `.claude/agents/`, each carrying deeper context than this file:

- `vue-expert` — feature work, fixes and refactors under `frontend/src/`
- `unit-tester` — tests; knows no harness exists and must agree on one before installing anything
- `code-quality-reviewer` — read-only review of a diff or branch; reports, does not edit
- `aws-deployer` — hosting and deployment; confirms before any mutating AWS call
- `cv-strategist` — the CV as a document: story, wording, recruiter/ATS keywords, and whether `CV/CV.md`
  and the site still agree. Edits `CV/CV.md` only; site fixes go to `vue-expert`
- `job-scout` — finds and vets remote permanent and freelance roles, scores them against the CV, keeps
  the history in `jobs/`. Never applies or contacts anyone
