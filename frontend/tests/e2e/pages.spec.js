import { test, expect } from '@playwright/test'
import {
  BASE_URL, LANGUAGES, PATHS, imageProblems, openSettled, pageWidths, recordProblems, savedLanguage
} from './helpers'

for (const code of LANGUAGES) {
  test.describe(`in ${code}`, () => {
    test.use({ storageState: savedLanguage(code) })

    for (const path of PATHS) {
      test(`${path} loads with a title, the right html lang and a clean console`, async ({ page }) => {
        const problems = recordProblems(page)
        await openSettled(page, path)
        await expect(page).toHaveTitle(/\S/)
        await expect(page.locator('html')).toHaveAttribute('lang', code)
        expect(problems).toEqual([])
      })
    }
  })
}

test('an unknown path redirects to the home page', async ({ page }) => {
  await page.goto('/no/such/page')
  await expect(page).toHaveURL(`${BASE_URL}/`)
})

for (const path of PATHS) {
  // Fonts and images are self-hosted so visitors' IPs never reach third parties.
  test(`${path} contacts no host other than the site itself`, async ({ page }) => {
    const hosts = new Set()
    const note = url => {
      const { protocol, hostname } = new URL(url)
      if (/^(https?|wss?):$/.test(protocol)) hosts.add(hostname)
    }
    page.on('request', request => note(request.url()))
    // The dev server's own hot-reload socket dials the machine's LAN address;
    // it does not exist in the deployed site.
    page.on('websocket', socket => {
      if (new URL(socket.url()).pathname !== '/ws') note(socket.url())
    })
    await openSettled(page, path)
    expect([...hosts]).toEqual(['localhost'])
  })

  test(`every image on ${path} actually loads`, async ({ page }) => {
    await openSettled(page, path)
    await expect.poll(() => imageProblems(page)).toEqual({ vImgsWithoutImage: 0, broken: [] })
  })
}

test.describe('in German', () => {
  test.use({ storageState: savedLanguage('de') })

  for (const width of [320, 390, 1280]) {
    for (const path of PATHS) {
      test(`${path} has no horizontal overflow at ${width}px`, async ({ page }) => {
        await page.setViewportSize({ width, height: 800 })
        await openSettled(page, path)
        const { scrollWidth, clientWidth, tooWide } = await pageWidths(page)
        expect(scrollWidth, `wider than the viewport: ${tooWide.join(', ')}`).toBeLessThanOrEqual(clientWidth)
      })
    }
  }
})
