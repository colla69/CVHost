import { test, expect } from '@playwright/test'
import { LANGUAGES, PATHS, expectFile, expectPdf, savedLanguage } from './helpers'

const CV = { en: '/data/CV_en.pdf', de: '/data/Lebenslauf.pdf', it: '/data/CV_it.pdf' }
const hrefsOf = locator => locator.evaluateAll(links => links.map(link => link.getAttribute('href')))

for (const code of LANGUAGES) {
  test.describe(`in ${code}`, () => {
    test.use({ storageState: savedLanguage(code) })

    test('the app bar offers the CV of that language', async ({ page }) => {
      await page.goto('/')
      await expect(page.locator('.lg-bar a[download]')).toHaveAttribute('href', CV[code])
    })

    test('every CV download link on every page returns a PDF', async ({ page, request }) => {
      const hrefs = new Set()
      for (const path of PATHS) {
        await page.goto(path)
        await expect(page.locator('footer a[download]').first()).toBeVisible()
        for (const href of await hrefsOf(page.locator('a[download][href$=".pdf"]'))) hrefs.add(href)
      }
      expect([...hrefs]).toContain(CV[code])
      for (const href of hrefs) await expectPdf(request, href)
    })
  })
}

test('the three footer CV links return PDFs', async ({ page, request }) => {
  await page.goto('/')
  await expect(page.locator('footer a[download]').first()).toBeVisible()
  const hrefs = await hrefsOf(page.locator('footer a[href$=".pdf"]'))
  expect(hrefs.sort()).toEqual(Object.values(CV).sort())
  for (const href of hrefs) await expectPdf(request, href)
})

test('every certificate thumbnail on /qualifications is a real image', async ({ page, request }) => {
  await page.goto('/qualifications')
  await expect(page.locator('.cert-sheet img').first()).toBeAttached()
  const sources = await page.locator('.cert-sheet img').evaluateAll(images => images.map(image => image.getAttribute('src')))
  expect(sources.length).toBeGreaterThan(0)
  for (const src of sources) await expectFile(request, src, 'image/jpeg', '\xFF\xD8\xFF')
})

test('every certificate tile on /qualifications opens a real PDF', async ({ page, request }) => {
  await page.goto('/qualifications')
  const tiles = page.locator('.cert-sheet')
  await expect(tiles.first()).toBeVisible()
  const viewer = page.locator('.viewer')

  for (let i = 0; i < await tiles.count(); i++) {
    await tiles.nth(i).click()
    await expect(viewer).toBeVisible()
    const frame = await viewer.locator('iframe').getAttribute('src')
    const links = await hrefsOf(viewer.locator('a[href]'))
    expect(links.length).toBeGreaterThan(0)
    for (const href of new Set([frame, ...links])) await expectPdf(request, href)
    await viewer.locator('button:has(.mdi-close)').click()
    await expect(viewer).toBeHidden()
  }
})

test('the complete document set on /qualifications is a real zip', async ({ page, request }) => {
  await page.goto('/qualifications')
  const href = await page.locator('.note a[download]').getAttribute('href')
  await expectFile(request, href, 'application/zip', 'PK')
})
