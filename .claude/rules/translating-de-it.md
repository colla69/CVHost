---
paths:
  - "frontend/src/**"
---

# Writing the site's German and Italian

`cv-strategist` writes these. Anyone else who edits German or Italian text follows the same rules.

## The edit boundary

Edit **text values only**: the `de` and `it` blocks of a `COPY`, and the `de` and `it` values of a
`{ "en", "de", "it" }` map in a component's data list, in `src/nav.js`, in `project_infos.json` and in
`news.json`. Never touch markup, bindings, keys, ids, dates, file names, links, function parameters, or any
line outside those blocks and maps. If the English is wrong or the structure needs a new key, report it:
that is the `change` trio's work. Inside an HTML string, translate the text nodes only. Tags, attributes
and `href`s stay byte-identical.

## The shape

- A `COPY` block's `de` and `it` have exactly the keys of `en`, no more and no fewer. Where an `en` value
  is a small function (`n => 'All ' + n + ' projects'`), the `de` and `it` one is the same shape with the
  new wording.
- The same English phrase can sit in more than one component ("Download CV" in the app bar and on the home
  page, the role line on the home page and in the footer). Translate each occurrence the same way.
- Keep the leading or trailing space of a sentence fragment that is split around a link (`noteBefore`,
  `introAfter`). Never leave a value empty: an empty string counts as missing and shows English.

## Voice and vocabulary

- First person, as on the English site. The site speaks as the owner, even though `CV-de.md` writes its
  profile impersonally.
- Address the reader formally: *Sie* in German, *Lei* in Italian.
- Job titles, level names and technical vocabulary as `CV-de.md` and `CV-it.md` use them. Where the CV
  keeps an English term (Senior IT Consultant, Tech Lead, Pipeline), so does the site. For anything both
  tell (profile, roles, education, languages), take the CV's wording, shortened where the site is shorter.
- No recruiter language in any language: no *leidenschaftlich*, *Macher*, *appassionato*, *proattivo*, no
  inflated verbs. Plain and slightly dry, like the owner.
- The Notes posts (`news.json`) are a personal log since 2018. Translate their casual voice as it is: don't
  polish it into marketing, and don't fix their content. They are the one exception to the formal address:
  readers are *ihr*/*euch* and *voi*, as in every existing post. Song and show titles used as jokes ("The
  Walking web", "Django unchained", "who let the snakes out!?") stay in English. Their typos are not
  yours to carry over: write the translation correctly.
- **Never add a fact in translation.** Where the English is ambiguous, translate the most literal reading
  and name the spot in your report, so the owner can decide.

## After a pass

Run `npm --prefix frontend run lint -- --no-fix` and `npm --prefix frontend run build`: a stray quote in a
`.vue` file or a missing comma in the JSON breaks the site. Report which files and keys you translated,
every ambiguity you resolved by guessing, and anything in the English you would change. `git diff` shows
nothing outside the text values.
