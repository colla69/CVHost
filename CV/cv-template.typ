// ── Ledger, on paper ────────────────────────────────────────────────────────
//
// The print twin of the site's design system. Same three typefaces, same
// palette as `.v-theme--ledgerLight` in frontend/src/app.css, same idea: a
// ruled document. Nothing here is hand-fed content — md2typst.py parses
// CV/*.md and calls `cv()` with the result, so the PDFs cannot drift from
// the markdown.
//
// Colours are duplicated from app.css rather than imported: typst cannot read
// CSS. Change one, change the other.

#let ink = rgb("#14181b")
#let muted = rgb("#5d666b")
#let faint = rgb("#8a9296")
#let hairline = rgb("#d6dad8")
#let accent = rgb("#12464c")
#let accent-soft = rgb("#e4ecea")
#let sunk = rgb("#eceeeb")

#let DISPLAY = "Archivo"
#let BODY = "Public Sans"
#let MONO = "JetBrains Mono"

// One vertical alignment runs through the whole document: dates, skill labels
// and section markers all sit in this rail, everything else starts after it.
#let RAIL = 26mm
#let GUTTER = 5mm

#let rail-row(left, right) = grid(
  columns: (RAIL, 1fr),
  column-gutter: GUTTER,
  left, right,
)

#let period-label(it) = align(
  right + top,
  text(font: MONO, size: 6.8pt, weight: 500, fill: accent, it),
)

#let eyebrow(it) = text(
  font: MONO, size: 6.4pt, weight: 500, tracking: 0.05em, fill: faint, upper(it),
)

// Technologies read as a row of tags rather than another sentence — the single
// biggest thing separating a scannable entry from a paragraph of nouns.
#let chip(it) = box(
  fill: accent-soft,
  radius: 1.5pt,
  inset: (x: 3.5pt, y: 2.2pt),
  outset: (y: 1.4pt),
  text(font: MONO, size: 6.4pt, fill: accent, it),
)

#let chip-row(meta) = {
  if meta == none { return }
  block(above: 4pt, below: 0pt, {
    set par(leading: 0.92em, spacing: 0pt, justify: false)
    if meta.label != none { eyebrow(meta.label) + h(4pt) }
    meta.items.map(chip).join([ ])
  })
}

// `sticky` keeps the heading on the same page as the block that follows it.
#let section-head(title) = block(
  above: 12pt, below: 4.5pt, width: 100%, breakable: false, sticky: true,
  {
    line(length: 100%, stroke: 1.8pt + ink)
    v(3pt, weak: true)
    text(font: DISPLAY, size: 10.5pt, weight: 600, fill: ink, title)
  },
)

// ── Blocks ──────────────────────────────────────────────────────────────────

#let prose-section(paragraphs) = paragraphs.enumerate().map(((i, p)) => {
    rail-row(
      [],
      block(
        above: if i == 0 { 0pt } else { 5.5pt },
        below: 0pt,
        text(size: if i == 0 { 9.5pt } else { 8.7pt }, fill: if i == 0 { ink } else { muted }, p),
      ),
    )
})

// Skills and languages: a definition list on the rail. The label carries the
// scanning weight, so it is set in ink against muted values.
#let deflist-section(items) = items.map(it => {
    block(above: 4pt, below: 0pt, {
      set par(justify: false)
      set text(hyphenate: false)
      rail-row(
        align(right, text(size: 7.9pt, weight: 600, fill: ink, it.label)),
        text(size: 8.4pt, fill: muted, it.body),
      )
    })
})

// Education, certificates: one line each, no prose.
#let records-section(records) = records.map(r => {
    block(above: 4.5pt, below: 0pt, rail-row(
      if r.period == none { [] } else { period-label(r.period) },
      {
        text(font: DISPLAY, size: 9.2pt, weight: 600, fill: ink, r.title)
        if r.detail != none {
          linebreak()
          text(size: 8pt, fill: muted, r.detail)
        }
      },
    ))
})

#let entry-body(e) = {
  text(font: DISPLAY, size: 9.4pt, weight: 600, fill: ink, e.title)
  if e.client != none {
    text(size: 8.4pt, fill: accent, [ · ] + e.client)
  }
  if e.extra != none {
    linebreak()
    text(font: MONO, size: 6.8pt, fill: muted, e.extra)
  }
  if e.role != none {
    linebreak()
    text(size: 7.8pt, fill: muted, style: "italic", e.role)
  }
  if e.body != none {
    block(above: 3.5pt, below: 0pt, text(size: 8.7pt, fill: ink, e.body))
  }
  chip-row(e.meta)
}

#let entry(e) = block(above: 6.5pt, below: 0pt, breakable: false, rail-row(
  if e.period == none { [] } else { period-label(e.period) },
  entry-body(e),
))

#let company(c) = {
  block(above: 9.5pt, below: 0pt, breakable: false, sticky: true, rail-row(
    period-label(c.period),
    {
      block(
        fill: sunk,
        width: 100%,
        inset: (x: 6pt, y: 4.5pt),
        radius: 2pt,
        {
          set par(spacing: 2.5pt)
          text(font: DISPLAY, size: 10pt, weight: 700, fill: ink, c.name)
          if c.place != none { text(size: 8pt, fill: faint, [ — ] + c.place) }
          if c.roles.len() > 0 {
            linebreak()
            text(font: MONO, size: 7pt, fill: accent, c.roles.join([ · ]))
          }
        },
      )
      if c.note != none {
        block(above: 3.5pt, below: 0pt, text(size: 7.8pt, fill: muted, style: "italic", c.note))
      }
    },
  ))
}

#let experience-section(companies) = {
  companies.map(c => (company(c),) + c.entries.map(entry)).flatten()
}

// ── Document ────────────────────────────────────────────────────────────────

#let masthead(name, tagline, contact, photo) = {
  let text-col = {
    text(font: DISPLAY, size: 23pt, weight: 700, fill: ink, tracking: -0.015em, name)
    block(above: 5pt, below: 0pt, text(
      font: DISPLAY, size: 9.6pt, weight: 500, fill: accent, tagline,
    ))
    block(above: 7pt, below: 0pt, {
      set par(leading: 0.9em, justify: false)
      set text(font: MONO, size: 7pt, fill: muted)
      let sep = text(fill: faint)[ · ]
      let plain = contact.filter(c => not c.link).map(c => c.text)
      let links = contact.filter(c => c.link).map(c => c.text)
      if plain.len() > 0 { plain.join(sep) }
      if links.len() > 0 {
        linebreak()
        links.join(sep)
      }
    })
  }

  if photo == none {
    text-col
  } else {
    grid(
      columns: (1fr, 27mm),
      column-gutter: 7mm,
      align(horizon, text-col),
      block(
        clip: true,
        radius: 2pt,
        stroke: 0.6pt + hairline,
        image(photo, width: 27mm),
      ),
    )
  }
  v(9pt, weak: true)
}

#let cv(
  meta: (title: "", author: ""),
  lang: "en",
  name: [],
  tagline: [],
  contact: (),
  photo: none,
  sections: (),
) = {
  set document(title: meta.title, author: meta.author)

  set page(
    paper: "a4",
    margin: (x: 15mm, top: 14mm, bottom: 15mm),
    header: context {
      if counter(page).get().first() > 1 {
        set text(font: MONO, size: 6.4pt, fill: faint)
        grid(columns: (1fr, auto), align(left, meta.author), align(right, meta.title))
        v(2pt, weak: true)
        line(length: 100%, stroke: 0.6pt + hairline)
      }
    },
    footer: context {
      set text(font: MONO, size: 6.4pt, fill: faint)
      line(length: 100%, stroke: 0.6pt + hairline)
      v(3pt, weak: true)
      grid(
        columns: (1fr, auto),
        align(left, meta.author),
        align(right, [
          #counter(page).display("1") / #counter(page).final().first()
        ]),
      )
    },
  )

  set text(font: BODY, size: 8.8pt, fill: ink, lang: lang, hyphenate: auto)
  set par(justify: true, leading: 0.58em, spacing: 0.65em)
  show link: set text(fill: accent)

  masthead(name, tagline, contact, photo)

  for s in sections {
    let parts = if s.kind == "prose" { prose-section(s.items) } else if s.kind == "deflist" {
      deflist-section(s.items)
    } else if s.kind == "records" {
      records-section(s.items)
    } else if s.kind == "experience" {
      experience-section(s.items)
    } else {
      s.items.map(entry)
    }
    section-head(s.title)
    for part in parts { part }
  }
}
