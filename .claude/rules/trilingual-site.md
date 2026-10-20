---
paths:
  - "frontend/src/**"
---

# The site is in English, German and Italian

Every user-visible string exists in all three languages. There is no `vue-i18n`, on purpose: its message
syntax treats `@ { } |` as special, which breaks the e-mail address and paragraph-length prose. The whole
mechanism is `src/i18n.js`.

## How the language is chosen

An explicit choice from the switcher in `Menu.vue` wins; it is stored as `lg-lang`, like the theme's
`lg-theme`. Otherwise the first supported entry of `navigator.languages`. Otherwise English. The language
is not in the URL, so routes and CloudFront are unaffected, and crawlers and link previews see English.

## The module

- `LANGUAGES`: one entry per language, `{ code, name, cv, dates }`. `cv` is the PDF that `CV/build-pdf.sh`
  renders for it (`/data/CV_en.pdf`, `/data/Lebenslauf.pdf`, `/data/CV_it.pdf`). `dates` is the locale for
  `toLocaleDateString` (`en-GB`, `de-DE`, `it-IT`). Nothing checks that the PDF paths match what
  `build-pdf.sh` writes.
- `detectLanguage(saved, browserLanguages)` is pure. A valid saved code wins. Otherwise the first browser
  tag whose base subtag is supported (`de-AT` → `de`), walking the whole list. Otherwise `en`.
- `lang` is a reactive `{ code }` plus a `language` getter for the active entry. It is resolved when the
  module loads, so the first paint is right.
- `setLanguage(code)` persists the explicit choice.
- `tr(value)`: a plain string passes through; a `{ en, de, it }` object returns the active language and
  falls back to `en`.
- `pick(COPY)` returns the active block merged over `COPY.en`, so a missing block, a missing key or an
  empty `''` value falls back to English instead of rendering blank.
- In development builds only, `tr` and `pick` warn in the console (`[i18n]`, each distinct message once)
  on every fallback, and when a copy block's keys differ from `en`. **Those warnings are the completeness
  check**: a page in German or Italian with a warning is not finished. They see only text that already
  sits in a `COPY` block or a map. English hard-coded in a template raises no warning at all, and that
  includes `aria-label`, `alt`, `title` and placeholder text.
- The plugin exposes `this.$lang` and `this.$tr` to every component and keeps `<html lang>` in sync.

## Where translated text lives

1. **Page copy and prose** (headings, intros, button labels, bio paragraphs): a module-level
   `const COPY = { en: {...}, de: {...}, it: {...} }` above `export default`, read through
   `computed: { t () { return pick(COPY) } }` and used as `{{ t.heading }}`. All three blocks carry the
   same keys. Counts are small functions: `allProjects: n => 'All ' + n + ' projects'`. **Keep the values
   flat**, strings and functions only, because `pick` checks top-level keys: a paragraph missing from an
   array would vanish without a warning. Several paragraphs get several named keys (`bioLead`, `bioStack`).
2. **Data lists and content JSON** (roles, education, skills, certificates, projects, news): one list.
   Only the fields that differ by language become `{ en, de, it }`, rendered with `$tr(item.field)`.
   Company names, digits, file names, links and ids stay plain strings. Never split a list into three
   per-language copies.

Never hard-code a CV path (use `$lang.language.cv`) or a date locale (use `$lang.language.dates`). The
footer and the contact page list all three PDFs on purpose. Tab
titles are `meta.title` in `router.js`. Page names come only from `PAGES` in `src/nav.js`.

## Markup inside prose

An HTML string in the copy (a `<b>`, a link inside a paragraph) renders with `v-html`. Two traps:

- Scoped CSS does not reach `v-html` content. A rule like `.hero-pitch b` must become
  `.hero-pitch :deep(b)`.
- A `<router-link>` cannot live inside `v-html`: it renders a plain `<a>` and reloads the page. Split the
  sentence around it: `{{ t.introBefore }}<router-link …>{{ t.work }}</router-link>{{ t.introAfter }}`.

## Who writes which words

The `change` trio builds structure and writes the English. `cv-strategist` writes the German and Italian,
editing text values only (see `.claude/rules/translating-de-it.md`). So when you add or change text:

- Write the `en` side only: a new `COPY.en` key, or a map holding just `{ "en": "…" }`. Leave `de` and
  `it` to `cv-strategist`.
- **Never copy the English into `de` or `it` as a placeholder.** It silences the fallback warnings and
  hides the gap. The site still works meanwhile, because everything falls back to English.
- In your report, list exactly which keys and fields still need `de` and `it`. When you *change* existing
  English, the German and Italian beside it are now stale and nothing will warn: list those keys too.
- If the user gives you the translation directly, use it.

A missing, stale or misplaced translation is a defect in the change. Whether the German or Italian wording
is good is `cv-strategist`'s and the owner's call.
