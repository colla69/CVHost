import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

// Ledger — see the matching CSS custom properties in src/app.css.
const ledgerLight = {
  dark: false,
  colors: {
    background: '#F5F6F4',
    surface: '#FFFFFF',
    'surface-bright': '#FFFFFF',
    'surface-variant': '#ECEEEB',
    'on-surface-variant': '#14181B',
    primary: '#12464C',
    'on-primary': '#F5F6F4',
    secondary: '#5D666B',
    'on-secondary': '#F5F6F4',
    'on-background': '#14181B',
    'on-surface': '#14181B',
    error: '#B4342B',
    info: '#12464C',
    success: '#2F6B4F',
    warning: '#9A6410'
  }
}

const ledgerDark = {
  dark: true,
  colors: {
    background: '#101314',
    surface: '#171B1C',
    'surface-bright': '#1E2325',
    'surface-variant': '#1E2325',
    'on-surface-variant': '#E7EAE9',
    primary: '#6FBFC7',
    'on-primary': '#0B1416',
    secondary: '#99A2A5',
    'on-secondary': '#0B1416',
    'on-background': '#E7EAE9',
    'on-surface': '#E7EAE9',
    error: '#F0857A',
    info: '#6FBFC7',
    success: '#7BC29C',
    warning: '#E0A94A'
  }
}

export default createVuetify({
  components,
  directives,
  icons: { defaultSet: 'mdi' },
  theme: {
    defaultTheme: 'ledgerLight',
    themes: { ledgerLight, ledgerDark }
  },
  defaults: {
    VBtn: { rounded: 'sm', style: 'text-transform: none; letter-spacing: 0.01em;' },
    VCard: { rounded: 'sm' },
    VTextField: { variant: 'outlined', density: 'comfortable' }
  }
})
