/* eslint-env browser */
import { test, expect } from '@playwright/test'
import { savedLanguage } from './helpers'

const mainText = page => page.locator('main').innerText()

for (const [from, to] of [['en', 'de'], ['de', 'it'], ['it', 'en']]) {
  test.describe(`starting in ${from}`, () => {
    test.use({ storageState: savedLanguage(from) })

    test(`choosing ${to} in the switcher changes the page without a reload`, async ({ page }) => {
      await page.goto('/')
      await expect(page.locator('html')).toHaveAttribute('lang', from)
      const before = await mainText(page)
      await page.evaluate(() => { window.notReloaded = true })

      await page.locator('#lang-switch').click()
      await page.locator(`.v-overlay .v-list-item[lang="${to}"]`).click()

      await expect(page.locator('html')).toHaveAttribute('lang', to)
      await expect.poll(() => mainText(page)).not.toBe(before)
      expect(await page.evaluate(() => window.notReloaded)).toBe(true)
    })

    test(`the choice of ${to} survives a reload`, async ({ page }) => {
      await page.goto('/')
      await page.locator('#lang-switch').click()
      await page.locator(`.v-overlay .v-list-item[lang="${to}"]`).click()
      await expect(page.locator('html')).toHaveAttribute('lang', to)
      const chosen = await mainText(page)

      await page.reload()

      await expect(page.locator('html')).toHaveAttribute('lang', to)
      await expect.poll(() => mainText(page)).toBe(chosen)
    })
  })
}

for (const [locale, expected] of [['de-DE', 'de'], ['it-IT', 'it'], ['fr-FR', 'en']]) {
  test.describe(`a ${locale} browser with nothing saved`, () => {
    test.use({ locale })

    test(`gets the site in ${expected}`, async ({ page }) => {
      await page.goto('/')
      await expect(page.locator('#lang-switch')).toHaveText(expected.toUpperCase())
      await expect(page.locator('html')).toHaveAttribute('lang', expected)
    })
  })
}

test.describe('a de-DE browser with Italian saved', () => {
  test.use({ locale: 'de-DE', storageState: savedLanguage('it') })

  test('gets the site in the saved language', async ({ page }) => {
    await page.goto('/')
    await expect(page.locator('#lang-switch')).toHaveText('IT')
    await expect(page.locator('html')).toHaveAttribute('lang', 'it')
  })
})
