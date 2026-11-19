/* eslint-env browser */
import { test, expect } from '@playwright/test'
import { LANGUAGES as SITE_LANGUAGES } from '../../src/i18n'
import posts from '../../src/components/news/news.json'
import { LANGUAGES, openSettled, savedLanguage } from './helpers'

// Newest first by release date, whatever order the file keeps.
const newestFirst = posts.slice().sort((a, b) =>
  b.release_date.localeCompare(a.release_date) || b.id - a.id)

const datesLocale = code => SITE_LANGUAGES.find(language => language.code === code).dates

// Formatted in the page, so the expected text uses the browser's own Intl data.
function expectedDates (page, locale) {
  return page.evaluate(({ dates, locale }) => dates.map(value =>
    new Date(value).toLocaleDateString(locale, { year: 'numeric', month: 'long', day: 'numeric' })),
  { dates: newestFirst.map(post => post.release_date), locale })
}

for (const code of LANGUAGES) {
  test.describe(`in ${code}`, () => {
    test.use({ storageState: savedLanguage(code) })

    test('/news shows every post, newest first, with its title and date', async ({ page }) => {
      await openSettled(page, '/news')
      const notes = page.locator('main article.note')

      await expect(notes).toHaveCount(posts.length)
      await expect(notes.locator('.note-title')).toHaveText(newestFirst.map(post => post.title[code]))
      await expect(notes.locator('.note-date')).toHaveText(await expectedDates(page, datesLocale(code)))
    })

    test('/news shows each post with its own image, loaded', async ({ page }) => {
      await openSettled(page, '/news')
      const images = await page.locator('main article.note').evaluateAll(notes => notes.map(note => {
        const image = note.querySelector('.note-img img')
        return image && {
          path: new URL(image.currentSrc || image.src).pathname,
          alt: image.alt,
          loaded: image.complete && image.naturalWidth > 0
        }
      }))

      expect(images).toEqual(newestFirst.map(post => ({
        path: post.img_link, alt: post.title[code], loaded: true
      })))
    })
  })
}
