import { test, expect } from '@playwright/test'

const THEME_TOGGLE = '.lg-bar button:has(.mdi-weather-night), .lg-bar button:has(.mdi-weather-sunny)'

for (const [colorScheme, from, to] of [['light', 'ledgerLight', 'ledgerDark'], ['dark', 'ledgerDark', 'ledgerLight']]) {
  test.describe(`on a ${colorScheme} system`, () => {
    test.use({ colorScheme })

    test(`the theme toggle switches from ${from} to ${to}`, async ({ page }) => {
      await page.goto('/')
      await expect(page.locator('.v-application')).toHaveClass(new RegExp(`v-theme--${from}`))

      await page.locator(THEME_TOGGLE).click()

      await expect(page.locator('.v-application')).toHaveClass(new RegExp(`v-theme--${to}`))
    })

    test(`a toggled ${to} theme survives a reload`, async ({ page }) => {
      await page.goto('/')
      await page.locator(THEME_TOGGLE).click()
      await expect(page.locator('.v-application')).toHaveClass(new RegExp(`v-theme--${to}`))

      await page.reload()

      await expect(page.locator('.v-application')).toHaveClass(new RegExp(`v-theme--${to}`))
    })
  })
}

test('toggling the theme twice returns to the light theme', async ({ page }) => {
  await page.goto('/')
  await page.locator(THEME_TOGGLE).click()
  await expect(page.locator('.v-application')).toHaveClass(/v-theme--ledgerDark/)

  await page.locator(THEME_TOGGLE).click()

  await expect(page.locator('.v-application')).toHaveClass(/v-theme--ledgerLight/)
})
