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
- Dead weight, do not run or repair unless asked: `frontend/pom.xml` and `frontend/node/` (a Maven build
  wrapper pinned to node v12) and `frontend/target/` (stale build output from it).

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

**How production actually serves:** `cv.colarietitosti.info` → CNAME → `colarietitosti.info` →
`85.214.103.161`, a Strato box running nginx, which reverse-proxies to an **S3 bucket** holding the
built site. No CloudFront. See `DEPLOYMENT.md` for the full picture and the runbook.

Neither rewrite config in this repo is what runs in production: `frontend/public/.htaccess` is from the
older plain-Apache setup, and `frontend/nginx.conf` belonged to the Docker image deleted in `8dd7cd4`.
**The nginx that actually routes traffic lives on the Strato host, outside this repo** — editing those
files does not change the live site.

Two consequences before you touch routing or deploys:

- **Deep links are broken in production.** `/qualifications` returns 404: the proxy passes S3's
  key-not-found straight through instead of falling back to `index.html`. Only in-app navigation works.
- The live `index.html` is dated **2023-02-08**, so the deployed site is far behind this repo.

There is no CI. The CDK project was deleted in `8dd7cd4`; the `feature/aws` branch name is a leftover
from that abandoned work.

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

## Agents

Five specialists live in `.claude/agents/`, each carrying deeper context than this file:

- `vue-expert` — feature work, fixes and refactors under `frontend/src/`
- `unit-tester` — tests; knows no harness exists and must agree on one before installing anything
- `code-quality-reviewer` — read-only review of a diff or branch; reports, does not edit
- `aws-deployer` — hosting and deployment; confirms before any mutating AWS call
- `cv-strategist` — the CV as a document: story, wording, recruiter/ATS keywords, and whether `CV/CV.md`
  and the site still agree. Edits `CV/CV.md` only; site fixes go to `vue-expert`
