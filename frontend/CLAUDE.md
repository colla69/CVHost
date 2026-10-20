# frontend

The whole application: the Vue app in `src/`, and everything it serves in `public/`. It is a static SPA
with no backend, so a "feature" is usually a component or a data edit. What a newcomer gets wrong first:
every text exists in three languages, and the content lives in JSON, not in markup.

**Start here:** `src/main.js` (8 lines), then `src/router.js`. `src/components/Home.vue` is the largest
page and uses every pattern: `COPY`/`pick`, `v-html` copy, both content files, `$lang.language.cv`.

## Map

| What | Where |
|---|---|
| Routes, tab titles, the catch-all redirect to `/` | `src/router.js` (`meta.title` comes from `pageLabel()`, except `/` and `/contact`) |
| The one list of pages and their labels | `src/nav.js` `PAGES`; `pageLabel(path)` throws on an unknown path |
| Language module | `src/i18n.js`; rules in `.claude/rules/trilingual-site.md` |
| Design tokens, fonts, utility classes | `src/app.css`; `lg-page` and `lg-inner` are in the unscoped `<style>` of `App.vue` |
| Vuetify palettes and component defaults | `src/plugins/vuetify.js` (full component and directive import: `<v-*>` needs no import; icons are MDI) |
| "Selected work" table on Home | `Home.vue`: filters `featured`, then **sorts by `start_date`**, newest first |
| Newest-first news and projects | `news/news.vue`, `projectInfos/projectInfos.vue`: `data.slice().reverse()` |
| Certificates | `qualifications` in `data()` of `CV/Qualifications.vue`; `source()` and `thumb()` build the paths |
| Contact page | `Contact/ContactForm.vue` has no form, only links. The file name is a leftover |
| Theme | `App.vue` on boot, `Menu.vue` switcher; stored as `lg-theme` |

## Conventions

- `@/` resolves to `src/`. eslint is `@vue/standard`.
- Components are Options API SFCs with `name`, `data()` and `<style scoped>` (`App.vue` is the exception).
  **Don't introduce `<script setup>` or the Composition API** into a component that doesn't use it.
  Consistency beats modernity here. If a change needs it, raise it; don't decide alone.
- **Never hard-code a colour.** Use `var(--lg-ink)`, `var(--lg-accent)`, `var(--lg-rule)` and the rest of
  the `--lg-*` tokens, so both themes work. A new colour is a new token in `app.css` under
  `.v-theme--ledgerLight` and `.v-theme--ledgerDark`, kept in step with the two palettes in `vuetify.js`.
- Prefer a Vuetify prop over hand-written CSS when one exists.
- Layout is mobile-first, measured at 320px and 390px with no horizontal overflow. There is deliberately
  no `overflow-x: hidden` guard, so a regression shows up instead of being masked.
- The look, "Ledger": a ruled document, hairline bands, a 2px ink rule above each section heading.
  Archivo for display, Public Sans for prose, JetBrains Mono for labels and figures, one petrol accent.
- The router uses `createWebHistory`. Every route path must be **extensionless**: the edge treats a last
  segment with a dot as a file (`infra/CLAUDE.md`).

## Where content lives

Adding a news post or a project is a data edit, and needs all three languages.

- **News** → `src/components/news/news.json`. The component reverses the array: keep `id` ascending and
  unique. `title` and `description_text` are `{ en, de, it }`; `description_text` renders through `v-html`.
- **Projects** → `src/components/projectInfos/project_infos.json`. `description` and `role_name` are
  `{ en, de, it }`, as are `client`, `lang`, `Name` and `company_name` where they are words, not names.
  `client` is the end customer, not the employer (`company_name`); leave it out where no source states it.
  `featured: true` puts an entry in "Selected work": a flag selects, the date orders.
- **Images** → local under `public/img/`, credited in `public/img/CREDITS.md`. Add a CREDITS row for each.
- **Certificates** → an entry in `qualifications` needs `filename` (a PDF in `public/data/`), `issuer`,
  `year`, `group`, and a thumbnail at `public/img/certs/<name>.jpg`. Render it with
  `magick -density 72 "data/X.pdf[0]" -background white -alpha remove -resize 520x -quality 76 img/certs/X.jpg`.
  Check both files exist: a typo renders a broken tile. `group` is an English filter key; translate only
  its heading.
- **Bio, languages, work history, education** → the `COPY` blocks and lists in `src/components/CV/*.vue`.

## Verifying

Run `lint -- --no-fix`, and `build` for any change to config, imports, the router or `i18n.js`. The build
lints in production mode, where `no-console` and `no-debugger` are warnings: read its eslint output too.
For a visible change, look at the page in **all three languages**, German at 320, 390 and 1280px (it runs
longest), with no `[i18n]` fallback warning in the dev console.

## Gotchas

- Vuetify 2 names and slots no longer exist (`v-list-item-content`, `v-expansion-panel-header`, `.sync`),
  nor do Vue 2 idioms (`$listeners`, `$children`, `filters`, `slot=`). The migration is finished, so one in
  a diff is new code repeating an old mistake.
- A `COPY` key that no template uses, and content that sits in both a JSON file and a template.
- `this` in an arrow function inside an Options API method. `v-for` without a stable `:key`. `v-model` on
  `v-expansion-panel`: it belongs on `v-expansion-panels`.
- A link with `target="_blank"` needs `rel="noopener"`.
- The site repeats card and list markup across `CV/`, `news/` and `projectInfos/`. Point at the existing
  copy; don't invent an abstraction for two occurrences.
- Don't add oversized files to `public/`.
