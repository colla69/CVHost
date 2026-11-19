import { test, expect } from '@playwright/test'
import { LANGUAGES, inLanguage, openSettled, savedLanguage } from './helpers'
import projects from '../../src/components/projectInfos/project_infos.json'
import { ROLES } from '../../src/components/CV/roles'
import { STEPS } from '../../src/components/CV/education'

// The expected titles come from the same data the pages render, so new content
// needs no change here. An array to toHaveText checks the count and the order too.
for (const code of LANGUAGES) {
  test.describe(`in ${code}`, () => {
    test.use({ storageState: savedLanguage(code) })

    test('/projectInfos shows every project, newest first, by its title', async ({ page }) => {
      await openSettled(page, '/projectInfos')
      const titles = projects.slice().reverse().map(project => inLanguage(project.Name, code))
      await expect(page.locator('article.card h2')).toHaveText(titles)
    })

    test('/experience shows every job by its title and company', async ({ page }) => {
      await openSettled(page, '/experience')
      await expect(page.locator('.entry-role')).toHaveText(ROLES.map(role => inLanguage(role.title, code)))
      await expect(page.locator('.entry-company')).toHaveText(ROLES.map(role => inLanguage(role.company, code)))
    })

    test('/experience shows every education step by its title', async ({ page }) => {
      await openSettled(page, '/experience')
      await expect(page.locator('.entry-title')).toHaveText(STEPS.map(step => inLanguage(step.title, code)))
    })
  })
}
