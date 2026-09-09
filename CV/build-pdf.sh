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
# md2typst.py parses the markdown into structure — companies, entries, dates,
# technology lists — and cv-template.typ lays that out in the site's design
# system. Neither file holds CV content: change the markdown, re-run this.
#
# Nothing runs this automatically. Run it by hand once the CVs are finished, then
# rebuild the site and deploy — see DEPLOYMENT.md.
#
# Requires typst and python3. On Manjaro:
#
#   sudo pacman -S typst woff2
#
# woff2 is optional: it supplies woff2_decompress, which unpacks the site's three
# webfonts into a form typst can read. Without it the PDFs still build, but in
# whatever fonts the system happens to offer.
set -euo pipefail

cd "$(dirname "$0")/.."
CV_DIR="CV"
OUT_DIR="frontend/public/data"
BUILD_DIR="$CV_DIR/.build"
FONT_DIR="$BUILD_DIR/fonts"
PHOTO="/frontend/src/assets/foto.jpg"

# The `>` blockquote lines are working notes, never CV content. Refuse to publish
# a CV that still has open ones.
if grep -rn '^>' "$CV_DIR"/CV.md "$CV_DIR"/CV-de.md "$CV_DIR"/CV-it.md; then
  echo
  echo "Open TODO notes above. Resolve and delete them before generating PDFs." >&2
  exit 1
fi

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR" "$FONT_DIR"
trap 'rm -rf "$BUILD_DIR"' EXIT

# typst reads TTF/OTF, the site ships woff2. Unpack into the build directory
# rather than committing a second copy of each font.
font_args=()
if command -v woff2_decompress >/dev/null; then
  for woff2 in frontend/public/fonts/*.woff2; do
    cp "$woff2" "$FONT_DIR/"
    woff2_decompress "$FONT_DIR/$(basename "$woff2")" >/dev/null
    rm -f "$FONT_DIR/$(basename "$woff2")"
  done
  font_args=(--font-path "$FONT_DIR")
else
  echo "woff2_decompress not found — falling back to system fonts." >&2
fi

photo_args=()
if [ -f "frontend/src/assets/foto.jpg" ]; then
  photo_args=(--photo "$PHOTO")
else
  echo "frontend/src/assets/foto.jpg missing — building without the portrait." >&2
fi

render () {
  local src="$1" dst="$2" lang="$3"
  local typ="$BUILD_DIR/$(basename "${dst%.pdf}").typ"
  python3 "$CV_DIR/md2typst.py" "$src" "$typ" \
    --lang "$lang" --template ../cv-template.typ "${photo_args[@]}"
  # --root keeps the portrait, which lives under frontend/, readable from a
  # document that sits in CV/.build.
  typst compile --root . "${font_args[@]}" "$typ" "$dst"
  echo "wrote $dst ($(pdfinfo "$dst" 2>/dev/null | awk '/^Pages/ {print $2}') pages)"
}

render "$CV_DIR/CV.md"    "$OUT_DIR/CV_en.pdf"     en
render "$CV_DIR/CV-de.md" "$OUT_DIR/Lebenslauf.pdf" de
render "$CV_DIR/CV-it.md" "$OUT_DIR/CV_it.pdf"     it

rm -f "$OUT_DIR/CV_Docs.zip"
zip -j "$OUT_DIR/CV_Docs.zip" \
  "$OUT_DIR/CV_en.pdf" "$OUT_DIR/Lebenslauf.pdf" "$OUT_DIR/CV_it.pdf"

echo
echo "Done. Home.vue links CV_Docs.zip, so the download is now current."
