import { reactive, watchEffect } from 'vue'

// One entry per language the site speaks. `cv` is the PDF CV/build-pdf.sh
// renders for it, `dates` the locale its dates are written in.
export const LANGUAGES = [
  { code: 'en', name: 'English', cv: '/data/CV_en.pdf', dates: 'en-GB' },
  { code: 'de', name: 'Deutsch', cv: '/data/Lebenslauf.pdf', dates: 'de-DE' },
  { code: 'it', name: 'Italiano', cv: '/data/CV_it.pdf', dates: 'it-IT' }
]

const FALLBACK = 'en'
const STORAGE_KEY = 'lg-lang'

function supported (code) {
  return LANGUAGES.some(language => language.code === code)
}

// An explicit choice wins; otherwise the first browser language the site
// speaks. Walk the whole list: ['fr-FR', 'it-IT'] means Italian, not English.
export function detectLanguage (saved, browserLanguages) {
  if (supported(saved)) return saved
  for (const tag of browserLanguages || []) {
    const base = String(tag || '').split('-')[0].toLowerCase()
    if (supported(base)) return base
  }
  return FALLBACK
}

function initialLanguage () {
  let saved = null
  try {
    saved = window.localStorage.getItem(STORAGE_KEY)
  } catch (e) {
    // Blocked storage: fall through to the browser's languages.
  }
  // Some embedded web views report an empty list; fall back to the single tag.
  const browser = navigator.languages && navigator.languages.length
    ? navigator.languages
    : [navigator.language]
  return detectLanguage(saved, browser)
}

// Resolved at module load rather than in a mounted hook, so the first paint
// is already in the right language.
export const lang = reactive({
  code: initialLanguage(),
  get language () {
    return LANGUAGES.find(language => language.code === this.code)
  }
})

export function setLanguage (code) {
  if (!supported(code)) return
  lang.code = code
  try {
    window.localStorage.setItem(STORAGE_KEY, code)
  } catch (e) {
    // Private mode or blocked storage: the choice just won't persist.
  }
}

// tr and pick run on every render, so each distinct warning is logged once
// per page load instead of flooding the console.
const warned = new Set()

function warnOnce (message) {
  if (warned.has(message)) return
  warned.add(message)
  // Deliberate, and only reachable in development builds.
  // eslint-disable-next-line no-console
  console.warn('[i18n] ' + message)
}

function preview (text) {
  const flat = String(text).replace(/\s+/g, ' ').trim()
  return flat.length > 60 ? flat.slice(0, 60) + '…' : flat
}

export function tr (value) {
  if (!value || typeof value !== 'object') return value
  const text = value[lang.code]
  if (text) return text
  if (process.env.NODE_ENV !== 'production') {
    warnOnce('no \'' + lang.code + '\' text, showing English: "' + preview(value.en) + '"')
  }
  return value.en
}

export function pick (copy) {
  const block = copy[lang.code]
  if (process.env.NODE_ENV !== 'production' && lang.code !== FALLBACK) {
    const keys = Object.keys(copy.en)
    if (!block) {
      warnOnce('no \'' + lang.code + '\' copy block, showing English for: ' + keys.join(', '))
    } else {
      const missing = keys.filter(key => !block[key])
      const extra = Object.keys(block).filter(key => !(key in copy.en))
      if (missing.length) {
        warnOnce('\'' + lang.code + '\' copy block is missing: ' + missing.join(', '))
      }
      if (extra.length) {
        warnOnce('\'' + lang.code + '\' copy block has keys English lacks: ' + extra.join(', '))
      }
    }
  }
  if (!block) return copy.en
  // Merge over English, so a single missing or empty key falls back like a
  // missing block does, instead of rendering blank or breaking a count function.
  const merged = Object.assign({}, copy.en)
  for (const key in block) {
    if (block[key]) merged[key] = block[key]
  }
  return merged
}

export default {
  install (app) {
    app.config.globalProperties.$lang = lang
    app.config.globalProperties.$tr = tr
    // Screen readers, hyphenation and the browser's translate prompt all read
    // <html lang>, so it follows the switcher.
    watchEffect(() => {
      document.documentElement.lang = lang.code
    })
  }
}
