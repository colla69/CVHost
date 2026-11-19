import { defineConfig, devices } from '@playwright/test'
import { PORT } from './tests/e2e/port'

// The dev server on purpose: it has the SPA fallback, and the [i18n] fallback
// warnings exist only in development builds.
export default defineConfig({
  testDir: 'tests/e2e',
  fullyParallel: true,
  timeout: 60000,
  use: { baseURL: `http://localhost:${PORT}` },
  projects: [{ name: 'chromium', use: { ...devices['Desktop Chrome'] } }],
  webServer: {
    command: `npm run serve -- --port ${PORT}`,
    url: `http://localhost:${PORT}`,
    reuseExistingServer: true,
    // The first webpack compile takes 20-30 s, longer on a busy machine.
    timeout: 180000
  }
})
