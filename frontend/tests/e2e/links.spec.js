import { test, expect } from '@playwright/test'
import news from '../../src/components/news/news.json'
import projects from '../../src/components/projectInfos/project_infos.json'
import { LANGUAGES, PATHS, savedLanguage } from './helpers'

// The hand-written HTML that pages render through v-html. Read from the data
// files, not the pages: a project's description sits in a closed panel that
// Vuetify does not render until it opens.
const CONTENT = { 'news.json': news, 'project_infos.json': projects }

const OTHER_SITE = /^https?:\/\//i
const ANY_SCHEME = /^[a-z][a-z0-9+.-]*:/i

function stringsIn (value) {
  if (typeof value === 'string') return [value]
  if (value && typeof value === 'object') return Object.values(value).flatMap(stringsIn)
  return []
}

function attribute (tag, name) {
  const match = tag.match(new RegExp(`\\s${name}\\s*=\\s*"([^"]*)"`, 'i'))
  return match ? match[1] : null
}

function anchorsIn (file) {
  return stringsIn(CONTENT[file])
    .flatMap(text => text.match(/<a\b[^>]*>/gi) || [])
    .map(tag => ({ tag, href: attribute(tag, 'href'), target: attribute(tag, 'target'), rel: attribute(tag, 'rel') }))
}

const opensSafelyInNewTab = link =>
  link.target === '_blank' && /\bnoopener\b/.test(link.rel) && /\bnoreferrer\b/.test(link.rel)

for (const file of Object.keys(CONTENT)) {
  test(`every link to another site in ${file} opens in a new tab without leaking the page`, () => {
    const external = anchorsIn(file).filter(link => OTHER_SITE.test(link.href))
    expect(external.length).toBeGreaterThan(0)
    expect(external.filter(link => !opensSafelyInNewTab(link)).map(link => link.tag)).toEqual([])
  })

  // The router sends a path it does not know to '/', so a page that stays on
  // its own path is one the router knows.
  test(`every link inside the site in ${file} reaches a real page`, async ({ page }) => {
    const internal = anchorsIn(file).filter(link => link.href !== null && !ANY_SCHEME.test(link.href) && !link.href.startsWith('#'))
    // Reported as skipped, not passed, while the file has nothing to check.
    test.skip(internal.length === 0, `${file} has no link inside the site yet`)
    for (const href of new Set(internal.map(link => link.href))) {
      await page.goto(href)
      expect(new URL(page.url()).pathname, href).toBe(new URL(href, page.url()).pathname)
    }
  })
}

// The same rule for what the pages show, which also covers the v-html copy
// written inside the components.
for (const code of LANGUAGES) {
  test.describe(`in ${code}`, () => {
    test.use({ storageState: savedLanguage(code) })

    test('every link to another site on every page opens in a new tab without leaking the page', async ({ page }) => {
      const unsafe = []
      for (const path of PATHS) {
        await page.goto(path)
        await expect(page.locator('footer a[download]').first()).toBeVisible()
        const links = await page.locator('a[href^="http"]').evaluateAll(anchors => anchors.map(anchor => ({
          tag: anchor.outerHTML.match(/<a\b[^>]*>/)[0],
          href: anchor.getAttribute('href'),
          target: anchor.getAttribute('target'),
          rel: anchor.getAttribute('rel')
        })))
        unsafe.push(...links.filter(link => !opensSafelyInNewTab(link)).map(link => `${path}: ${link.tag}`))
      }
      expect(unsafe).toEqual([])
    })
  })
}
