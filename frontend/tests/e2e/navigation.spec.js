import { test, expect } from '@playwright/test'
import { BASE_URL, PATHS } from './helpers'

// Start from a different page, so the click has to change something.
const startFor = path => (path === '/' ? '/contact' : '/')

async function expectArrivedAt (page, path, startTitle) {
  await expect(page).toHaveURL(`${BASE_URL}${path}`)
  await expect(page).not.toHaveTitle(startTitle)
}

for (const path of PATHS) {
  test(`the desktop menu reaches ${path}`, async ({ page }) => {
    await page.goto(startFor(path))
    await expect(page.locator('main .lg-page').first()).toBeVisible()
    const startTitle = await page.title()

    await page.locator(`nav.links a[href="${path}"]`).click()

    await expectArrivedAt(page, path, startTitle)
  })
}

test.describe('at phone width', () => {
  test.use({ viewport: { width: 390, height: 844 } })

  for (const path of PATHS) {
    test(`the drawer reaches ${path}`, async ({ page }) => {
      await page.goto(startFor(path))
      await expect(page.locator('main .lg-page').first()).toBeVisible()
      const startTitle = await page.title()

      await page.locator('.v-app-bar-nav-icon').click()
      await page.locator(`.v-navigation-drawer a[href="${path}"]`).click()

      await expectArrivedAt(page, path, startTitle)
    })
  }
})
