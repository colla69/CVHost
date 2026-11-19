import { test, expect } from '@playwright/test'
import { BASE_URL } from './helpers'

// The contact page has no form, only links. These tests read the links from
// the page and never let a request leave the machine: any other site answers
// with a stub page, and a mail link is checked, never followed.
const STUB = '<!doctype html><title>stub</title>'

test.beforeEach(async ({ context }) => {
  await context.route(url => !url.href.startsWith(BASE_URL), route =>
    route.fulfill({ status: 200, contentType: 'text/html', body: STUB }))
})

const channels = page => page.locator('.channels a.channel')

test('the contact page has no form that could send anything', async ({ page }) => {
  await page.goto('/contact')
  await expect(channels(page).first()).toBeVisible()
  await expect(page.locator('form')).toHaveCount(0)
})

test('the email link writes to the address the page shows', async ({ page }) => {
  await page.goto('/contact')
  const email = page.locator('.channels a.channel[href^="mailto:"]')
  await expect(email).toHaveCount(1)
  const shown = (await email.locator('.channel-value').innerText()).trim()
  expect(shown).toMatch(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)
  await expect(email).toHaveAttribute('href', `mailto:${shown}`)
  await expect(email).not.toHaveAttribute('target', /./)
})

test('each profile link opens its site in a new tab without leaking the page', async ({ page }) => {
  await page.goto('/contact')
  const profiles = page.locator('.channels a.channel[href^="http"]')
  await expect(profiles.first()).toBeVisible()

  for (let i = 0; i < await profiles.count(); i++) {
    const link = profiles.nth(i)
    const href = await link.getAttribute('href')
    await expect(link).toHaveAttribute('target', '_blank')
    await expect(link).toHaveAttribute('rel', /(?=.*\bnoopener\b)(?=.*\bnoreferrer\b)/)

    const [tab] = await Promise.all([page.waitForEvent('popup'), link.click()])
    await expect(tab).toHaveURL(href)
    expect(page.url(), href).toBe(`${BASE_URL}/contact`)
    await tab.close()
  }
})
