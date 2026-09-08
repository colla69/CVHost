#!/usr/bin/env bash
#
# Renders the three CV markdown files into frontend/public/data/ and rebuilds the
# download bundle, so what the site serves matches what CV/*.md says.
#
#   CV.md     -> frontend/public/data/CV_en.pdf
#   CV-de.md  -> frontend/public/data/Lebenslauf.pdf
#   CV-it.md  -> frontend/public/data/CV_it.pdf
#   all three -> frontend/public/data/CV_Docs.zip
#
# Nothing runs this automatically. Run it by hand once the CVs are finished, then
# rebuild the site and deploy — see DEPLOYMENT.md.
#
# Requires one of: pandoc (preferred), weasyprint, or wkhtmltopdf. On Manjaro:
#
#   sudo pacman -S pandoc-cli typst                      # pandoc route, small
#   sudo pacman -S python-weasyprint                     # weasyprint route
#
set -euo pipefail

cd "$(dirname "$0")/.."
CV_DIR="CV"
OUT_DIR="frontend/public/data"

# The `>` blockquote lines are working notes, never CV content. Refuse to publish
# a CV that still has open ones.
if grep -rn '^>' "$CV_DIR"/CV.md "$CV_DIR"/CV-de.md "$CV_DIR"/CV-it.md; then
  echo
  echo "Open TODO notes above. Resolve and delete them before generating PDFs." >&2
  exit 1
fi

render () {
  local src="$1" dst="$2"
  # The CVs wrap prose across lines, so pandoc folds a project's title, role and
  # description into one paragraph. Mark the structural lines — anything starting
  # bold, and the italic role lines — as markdown hard breaks first, leaving the
  # wrapped prose alone.
  local tmp
  tmp="$(mktemp --suffix=.md)"
  trap 'rm -f "$tmp"' RETURN
  # A line gets a hard break when the NEXT line opens a structural element (a bold
  # title or an italic role line), and when the line itself is a whole italic role
  # line. Deciding on the next line rather than the current one keeps wrapped
  # continuations — long Stack lists, the certification line — unbroken.
  awk '{ l[NR] = $0 }
       END {
         for (i = 1; i <= NR; i++) {
           nxt = (i < NR ? l[i+1] : "")
           hard = 0
           if (nxt ~ /^\*/ && nxt !~ /^\*\*(Stack|Technologien|Tecnologie):\*\*/) hard = 1
           if (l[i] ~ /^\*[^*].*\*$/) hard = 1
           if (hard && l[i] != "") printf "%s  \n", l[i]; else print l[i]
         }
       }' "$src" > "$tmp"
  src="$tmp"
  if command -v pandoc >/dev/null; then
    # typst renders these documents fine and is a fraction of a TeX install, so
    # prefer it when present; otherwise pandoc falls back to its default engine.
    local engine=()
    command -v typst >/dev/null && engine=(--pdf-engine=typst)
    pandoc "$src" -o "$dst" "${engine[@]}" --metadata-file="$CV_DIR/pdf-meta.yaml"
  elif command -v weasyprint >/dev/null; then
    # weasyprint needs HTML; pandoc is absent here, so use a minimal markdown->html
    npx --yes marked -i "$src" -o "${dst%.pdf}.html"
    weasyprint "${dst%.pdf}.html" "$dst"
    rm -f "${dst%.pdf}.html"
  elif command -v wkhtmltopdf >/dev/null; then
    npx --yes marked -i "$src" -o "${dst%.pdf}.html"
    wkhtmltopdf --enable-local-file-access "${dst%.pdf}.html" "$dst"
    rm -f "${dst%.pdf}.html"
  else
    echo "No markdown-to-PDF converter found. See the header of this script." >&2
    exit 1
  fi
  echo "wrote $dst"
}

render "$CV_DIR/CV.md"    "$OUT_DIR/CV_en.pdf"
render "$CV_DIR/CV-de.md" "$OUT_DIR/Lebenslauf.pdf"
render "$CV_DIR/CV-it.md" "$OUT_DIR/CV_it.pdf"

rm -f "$OUT_DIR/CV_Docs.zip"
zip -j "$OUT_DIR/CV_Docs.zip" \
  "$OUT_DIR/CV_en.pdf" "$OUT_DIR/Lebenslauf.pdf" "$OUT_DIR/CV_it.pdf"

echo
echo "Done. CVShort.pdf and CVShort.jpg are older one-pagers and were left untouched."
echo "Home.vue links CV_Docs.zip, so the download is now current."
