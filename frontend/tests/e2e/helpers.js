/* eslint-env browser */
import { expect } from '@playwright/test'
import { PAGES } from '../../src/nav'

export const BASE_URL = 'http://localhost:8137'
export const PATHS = PAGES.map(page => page.to)
export const LANGUAGES = ['en', 'de', 'it']

// Stored through storageState rather than an init script: an init script runs
// again on every reload and would overwrite the choice a test is checking.
export function savedLanguage (code) {
  return {
    cookies: [],
    origins: [{ origin: BASE_URL, localStorage: [{ name: 'lg-lang', value: code }] }]
  }
}

export function recordProblems (page) {
  const problems = []
  page.on('console', message => {
    if (message.type() === 'error' || message.text().includes('[i18n]')) {
      problems.push(`${message.type()}: ${message.text()}`)
    }
  })
  page.on('pageerror', error => problems.push(`pageerror: ${error.message}`))
  return problems
}

// Opens a page and waits until it is laid out in its final fonts, with every
// lazy image in view at least once: v-img adds its <img> only on intersection.
export async function openSettled (page, path) {
  await page.goto(path)
  await expect(page.locator('main .lg-page').first()).toBeVisible()
  await page.evaluate(async () => {
    await document.fonts.ready
    const step = Math.ceil(window.innerHeight / 2)
    for (let y = 0; y < document.documentElement.scrollHeight; y += step) {
      window.scrollTo(0, y)
      await new Promise(resolve => setTimeout(resolve, 40))
    }
  })
  await page.waitForLoadState('networkidle')
}

export function imageProblems (page) {
  return page.evaluate(() => ({
    vImgsWithoutImage: document.querySelectorAll('.v-img:not(:has(img))').length,
    broken: Array.from(document.images)
      .filter(image => !image.complete || image.naturalWidth === 0)
      .map(image => image.currentSrc || image.src)
  }))
}

export function pageWidths (page) {
  return page.evaluate(() => ({
    scrollWidth: document.documentElement.scrollWidth,
    clientWidth: document.documentElement.clientWidth,
    // Names what sticks out, so a failure can be acted on without a browser.
    tooWide: Array.from(document.querySelectorAll('body *'))
      .filter(element => element.getBoundingClientRect().right > document.documentElement.clientWidth + 1)
      .slice(0, 5)
      .map(element => `${element.tagName.toLowerCase()}.${element.className}`)
  }))
}

// The dev server answers a missing file with 200 and index.html, so the status
// alone proves nothing: check the type and the file's own first bytes.
export async function expectFile (request, href, contentType, magic) {
  const response = await request.get(href)
  expect(response.status(), href).toBe(200)
  expect(response.headers()['content-type'], href).toContain(contentType)
  expect((await response.body()).subarray(0, magic.length).toString('latin1'), href).toBe(magic)
}

export const expectPdf = (request, href) => expectFile(request, href, 'application/pdf', '%PDF')
