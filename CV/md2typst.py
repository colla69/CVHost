#!/usr/bin/env python3
"""Turn one of the CV markdown files into a typst document for cv-template.typ.

The three CVs are the same document in three languages, so nothing here keys on
a heading's wording: sections are classified by shape.

    a section containing `### ...`          -> experience
    a section that is one bullet list       -> deflist  (skills, languages)
    bold-led blocks with an italic role line -> entries (personal work)
    bold-led blocks without one              -> records (education, certificates)
    anything else                            -> prose   (profile)

Blocks are separated by blank lines, and every entry in the markdown is one
block: title line, italic role line, wrapped prose, then a `**Stack:**` line.
Wrapped lines are joined back together here, which is what stops a project from
arriving as one grey paragraph.

Usage: md2typst.py CV.md out.typ --lang en [--photo path/inside/typst/root]
"""

import argparse
import pathlib
import re
import sys

# ── markdown inline → typst markup ──────────────────────────────────────────

# Every character typst would otherwise read as markup.
ESCAPE = re.compile(r"([\\#$*_`<>@\[\]~])")
INLINE = re.compile(
    r"\[([^\]]+)\]\(([^)]+)\)"   # 1,2 link
    r"|\*\*(.+?)\*\*"            # 3   strong
    r"|\*([^*]+?)\*"             # 4   emphasis
    r"|`([^`]+)`"                # 5   code
)


def escape(text):
    return ESCAPE.sub(r"\\\1", text)


def markup(md):
    """Markdown inline markup as typst content-block source."""
    out, pos = [], 0
    for m in INLINE.finditer(md):
        out.append(escape(md[pos:m.start()]))
        if m.group(1) is not None:
            out.append('#link("%s")[%s]' % (quote(m.group(2)), markup(m.group(1))))
        elif m.group(3) is not None:
            out.append("*%s*" % markup(m.group(3)))
        elif m.group(4) is not None:
            out.append("_%s_" % markup(m.group(4)))
        else:
            out.append('#raw("%s")' % quote(m.group(5)))
        pos = m.end()
    out.append(escape(md[pos:]))
    return "".join(out)


def quote(text):
    return text.replace("\\", "\\\\").replace('"', '\\"')


# ── typst value emitter ─────────────────────────────────────────────────────

class Content(str):
    """A string that is already typst markup and must be emitted as `[...]`."""


def dump(value, indent=1):
    pad = "  " * indent
    if value is None:
        return "none"
    if isinstance(value, bool):
        return "true" if value else "false"
    if isinstance(value, Content):
        return "[%s]" % value
    if isinstance(value, str):
        return '"%s"' % quote(value)
    if isinstance(value, list):
        if not value:
            return "()"
        inner = (",\n" + pad).join(dump(v, indent + 1) for v in value)
        return "(\n%s%s,\n%s)" % (pad, inner, "  " * (indent - 1))
    if isinstance(value, dict):
        if not value:
            return "(:)"
        inner = (",\n" + pad).join(
            "%s: %s" % (k, dump(v, indent + 1)) for k, v in value.items()
        )
        return "(\n%s%s,\n%s)" % (pad, inner, "  " * (indent - 1))
    raise TypeError(value)


# ── markdown structure ──────────────────────────────────────────────────────

BOLD_LEAD = re.compile(r"^\*\*(.+?)\*\*\s*(.*)$")
ITALIC_LINE = re.compile(r"^\*([^*].*[^*])\*$")
BULLET = re.compile(r"^-\s+(.*)$")
LABELLED = re.compile(r"^\*\*(.+?):\*\*\s*(.*)$")
DEFLIST_ITEM = re.compile(r"^\*\*(.+?)\*\*\s*[—–-]\s*(.*)$")
# A rail label has to stay short: "01/2024 – present" yes, a sentence about
# recertification dates no.
PERIOD = re.compile(
    r"^(\d{2}/\d{4}|\d{4})(\s*[–—-]\s*(\d{2}/\d{4}|\d{4}|[^\s,.;]{3,12}))?$"
)
LEAD_DASH = re.compile(r"^[—–-]\s*")


def blocks(lines):
    """Group consecutive non-empty lines, dropping `---` rules and `>` notes."""
    out, current = [], []
    for raw in lines:
        line = raw.rstrip()
        if line.startswith(">"):
            continue
        if not line.strip() or line.strip() == "---":
            if current:
                out.append(current)
                current = []
            continue
        current.append(line.strip())
    if current:
        out.append(current)
    return out


def split_meta(rest):
    """`— client · 01/2024 – present` → (client, period, extra)."""
    rest = LEAD_DASH.sub("", rest.strip()).strip()
    if rest.startswith("·"):
        rest = rest[1:].strip()
    if not rest:
        return None, None, None
    chunks = [c.strip() for c in rest.split(" · ") if c.strip()]
    period = None
    for i in reversed(range(len(chunks))):
        if PERIOD.match(chunks[i]):
            period = chunks.pop(i)
            break
    client = chunks.pop(0) if chunks else None
    extra = " · ".join(chunks) if chunks else None
    return client, period, extra


def parse_entry(block):
    """A project block: title (possibly wrapped), role line, prose, stack line."""
    role_at = next(
        (i for i, l in enumerate(block) if ITALIC_LINE.match(l)), None
    )
    if role_at is None:
        title_lines, role, body_lines = block[:1], None, block[1:]
    else:
        title_lines = block[:role_at]
        role = markup(ITALIC_LINE.match(block[role_at]).group(1))
        body_lines = block[role_at + 1:]

    m = BOLD_LEAD.match(" ".join(title_lines))
    title, rest = (m.group(1), m.group(2)) if m else (" ".join(title_lines), "")
    client, period, extra = split_meta(rest)

    meta_at = next(
        (i for i, l in enumerate(body_lines) if LABELLED.match(l)), None
    )
    meta = None
    if meta_at is not None:
        m = LABELLED.match(" ".join(body_lines[meta_at:]))
        meta = {
            "label": Content(markup(m.group(1))),
            "items": [Content(markup(i.strip())) for i in m.group(2).split(",") if i.strip()],
        }
        body_lines = body_lines[:meta_at]

    body = " ".join(body_lines).strip()
    return {
        "title": Content(markup(title)),
        "client": Content(markup(client)) if client else None,
        "period": Content(markup(period)) if period else None,
        "extra": Content(markup(extra)) if extra else None,
        "role": Content(role) if role else None,
        "body": Content(markup(body)) if body else None,
        "meta": meta,
    }


def parse_records(block):
    """Education / certificates: a new record starts at every bold-led line."""
    groups, current = [], []
    for line in block:
        if BOLD_LEAD.match(line) and current:
            groups.append(current)
            current = [line]
        else:
            current.append(line)
    if current:
        groups.append(current)

    records = []
    for group in groups:
        m = BOLD_LEAD.match(" ".join(group))
        if not m:
            continue
        title, rest = m.group(1), m.group(2)
        client, period, extra = split_meta(rest)
        detail = " · ".join(p for p in (client, extra) if p)
        records.append({
            "title": Content(markup(title)),
            "period": Content(markup(period)) if period else None,
            "detail": Content(markup(detail)) if detail else None,
        })
    return records


def parse_bullets(block):
    items, current = [], None
    for line in block:
        m = BULLET.match(line)
        if m:
            if current:
                items.append(current)
            current = m.group(1)
        elif current is not None:
            current += " " + line
    if current:
        items.append(current)

    out = []
    for item in items:
        m = DEFLIST_ITEM.match(item)
        if m:
            out.append({
                "label": Content(markup(m.group(1))),
                "body": Content(markup(m.group(2))),
            })
        else:
            out.append({"label": Content(""), "body": Content(markup(item))})
    return out


def parse_experience(section_blocks):
    companies = []
    head = False
    for block in section_blocks:
        first = block[0]
        if first.startswith("### "):
            name, _, place = first[4:].partition(" — ")
            companies.append({
                "name": Content(markup(name.strip())),
                "place": Content(markup(place.strip())) if place.strip() else None,
                "roles": [],
                "period": Content(""),
                "note": None,
                "entries": [],
            })
            head = True
            continue
        if not companies:
            continue
        company = companies[-1]

        if head and all(BOLD_LEAD.match(l) for l in block) and not any(
            ITALIC_LINE.match(l) for l in block
        ):
            # `**Senior IT Consultant** · 2024 – present`, one line per role.
            periods = []
            for line in block:
                m = BOLD_LEAD.match(line)
                _, period, _ = split_meta(m.group(2))
                company["roles"].append(Content(markup(m.group(1))))
                if period:
                    periods.append(period)
            if periods:
                span = periods[-1].split("–")[0].strip() + " – " + periods[0].split("–")[-1].strip()
                company["period"] = Content(markup(span if len(periods) > 1 else periods[0]))
            continue

        if len(block) == 1 and ITALIC_LINE.match(block[0]):
            company["note"] = Content(markup(ITALIC_LINE.match(block[0]).group(1)))
            head = False
            continue

        head = False
        company["entries"].append(parse_entry(block))
    return companies


def classify(section_blocks):
    if any(b[0].startswith("### ") for b in section_blocks):
        return "experience"
    if all(BULLET.match(b[0]) for b in section_blocks):
        return "deflist"
    if any(ITALIC_LINE.match(l) for b in section_blocks for l in b):
        return "entries"
    if all(BOLD_LEAD.match(b[0]) for b in section_blocks):
        return "records"
    return "prose"


def parse(path):
    lines = pathlib.Path(path).read_text(encoding="utf-8").splitlines()

    name = ""
    sections, current = [], None
    head_lines = []
    for line in lines:
        if line.startswith("# "):
            name = line[2:].strip()
        elif line.startswith("## "):
            current = {"title": line[3:].strip(), "lines": []}
            sections.append(current)
        elif current is None:
            head_lines.append(line)
        else:
            current["lines"].append(line)

    head = blocks(head_lines)
    tagline = ""
    contact = []
    if head:
        m = BOLD_LEAD.match(head[0][0])
        tagline = m.group(1) if m else " ".join(head[0])
    if len(head) > 1:
        joined = " ".join(head[1])
        contact = [c.strip() for c in joined.split(" · ") if c.strip()]

    parsed = []
    for section in sections:
        section_blocks = blocks(section["lines"])
        if not section_blocks:
            continue
        kind = classify(section_blocks)
        if kind == "experience":
            items = parse_experience(section_blocks)
        elif kind == "deflist":
            items = [i for b in section_blocks for i in parse_bullets(b)]
        elif kind == "records":
            items = [r for b in section_blocks for r in parse_records(b)]
        elif kind == "entries":
            items = [parse_entry(b) for b in section_blocks]
        else:
            items = [Content(markup(" ".join(b))) for b in section_blocks]
        parsed.append({
            "kind": kind,
            "title": Content(markup(section["title"])),
            "items": items,
        })

    return name, tagline, contact, parsed


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("source")
    ap.add_argument("target")
    ap.add_argument("--lang", default="en")
    ap.add_argument("--photo", default=None)
    ap.add_argument("--template", default="cv-template.typ")
    args = ap.parse_args()

    name, tagline, contact, sections = parse(args.source)
    if not name or not sections:
        sys.exit("%s: no CV structure found" % args.source)

    document = "\n".join([
        '#import "%s": cv' % args.template,
        "",
        "#cv(",
        "  meta: %s," % dump({"title": tagline, "author": name}, 2),
        '  lang: "%s",' % args.lang,
        "  name: %s," % dump(Content(markup(name)), 2),
        "  tagline: %s," % dump(Content(markup(tagline)), 2),
        "  contact: %s," % dump([
            {"text": Content(markup(c)), "link": "](" in c} for c in contact
        ], 2),
        "  photo: %s," % dump(args.photo, 2),
        "  sections: %s," % dump(sections, 2),
        ")",
        "",
    ])
    pathlib.Path(args.target).write_text(document, encoding="utf-8")


if __name__ == "__main__":
    main()
