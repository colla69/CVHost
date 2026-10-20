# CV

The owner's CV as a document, told in two places that must not contradict each other: this folder, and the
public site (`frontend/src/`). Each exists in English, German and Italian. What a newcomer gets wrong
first: changing the story on one side and not the other, and never regenerating the PDFs.

**Start here:** `CV.md`, the master. `CV-de.md` and `CV-it.md` are derived from it, and all three are
updated together: a change in the master that is not carried into both is an unfinished edit.

## Rules for the documents

- **Lines starting with `>` are working notes, not CV content.** They are the channel between editor and
  owner: answer one and delete it; add one for a new gap. `CV/build-pdf.sh` refuses to build while any
  remain. Never leave one in a version the owner is about to send, and never let one become body text.
- Angle-bracket placeholders (`<e-mail>`, `<phone>`) are deliberate blanks. Leave them until the owner
  fills them in.
- A CV tailored to one job ad goes to its own file, `CV/CV-<role>.md`. The master stays the superset.
- **Never invent a fact:** not a date, a client, a number, a technology or a scope. Fill in wording, never
  facts. Ask the owner, or leave a `>` note. This outranks every other instruction, a request to "fill in
  the gaps" included.
- Voice: plain and slightly dry. No "spearheaded", "leveraged", "passionate about" and relatives. The site
  speaks in the first person; the CV describes the owner.
- Clients are named (Porsche AG, Porsche Bank, BMW Financial Services, Volkswagen Financial Services,
  Krones, Schwarz IT, Red Arrow International, Pioneer Investments), because the employer's own profile
  names them (owner decision, 09/2026). **The one exception is the public-sector client in healthcare:
  unnamed everywhere**, in text, in logos and in image URLs.
- The German market is the primary one. `CV-de.md` and `Lebenslauf.pdf` are a first-class document, not a
  courtesy translation.
- `CV/additionalInfo.md` is the employer's internal profile, gitignored. Background only: never commit it,
  never quote it into `jobs/` or the site.

## The PDFs

`CV/build-pdf.sh` (`md2typst.py` + typst) renders the three files to `frontend/public/data/CV_en.pdf`,
`Lebenslauf.pdf`, `CV_it.pdf` and `CV_Docs.zip`. Every download button on the site serves them. After a
material edit they are stale, and a recruiter downloads a CV that contradicts `CV.md`. **Regenerating
changes nothing until the site is built and deployed.** Home links the per-language PDF through
`$lang.language.cv`; `Qualifications.vue` links `CV_Docs.zip`.

## Where the story also lives

| What | Where on the site |
|---|---|
| Employers, titles, year ranges | `components/CV/WorkExperience.vue` (hard-coded timeline) |
| Schools and year ranges | `components/CV/Education.vue` |
| Languages and language certificates | `components/CV/Languages.vue` |
| Public contact details: e-mail, city, GitHub, LinkedIn only | `components/CV/PersonalInfo.vue` |
| Profile prose and the skills groups | `components/CV/AboutMe.vue` |
| Scanned certificates and references | `qualifications` in `components/CV/Qualifications.vue`, PDFs in `public/data/` |
| Every project: role name, dates, stack, client | `components/projectInfos/project_infos.json` |
| Dated posts, the informal voice | `components/news/news.json` |
| Landing pitch, client strip, featured projects | `components/Home.vue` |

**The split of personal data is deliberate:** the site shows e-mail, city, GitHub and LinkedIn. Address,
phone, date of birth and marital status live in the CV download alone. Anything more on the open web is a
privacy question for the owner, not an error to fix.

## Gotchas

- Neither side is automatically right. The site is usually the staler one, but sometimes it is accurate and
  the CV optimistic. Report a divergence as a pair, with both locations, and recommend one.
- The German and Italian text on the site follows `CV-de.md` and `CV-it.md`: same titles, same facts, same
  vocabulary. Rules for writing it: `.claude/rules/translating-de-it.md`.
