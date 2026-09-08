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
# Requires one of: pandoc (preferred), weasyprint, or wkhtmltopdf. None of them was
# installed when this script was written; on Manjaro:
#
#   sudo pacman -S pandoc-cli texlive-latexrecommended   # pandoc route
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
  if command -v pandoc >/dev/null; then
    pandoc "$src" -o "$dst" \
      -V geometry:margin=2cm \
      -V fontsize=10pt \
      -V colorlinks=true \
      -V linkcolor=black \
      --metadata title=""
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
