<template>
  <v-app id="app">
    <Menu></Menu>
    <v-main>
      <router-view></router-view>
      <SiteFooter></SiteFooter>
    </v-main>
  </v-app>
</template>

<script>

import Menu from '@/components/Menu'
import SiteFooter from '@/components/SiteFooter'

export default {
  name: 'App',
  components: {
    Menu,
    SiteFooter
  },
  mounted () {
    // An explicit choice wins; otherwise follow the operating system.
    let saved = null
    try {
      saved = window.localStorage.getItem('lg-theme')
    } catch (e) {
      // Blocked storage: fall through to the system preference.
    }
    if (saved === 'ledgerLight' || saved === 'ledgerDark') {
      this.$vuetify.theme.change(saved)
    } else if (window.matchMedia('(prefers-color-scheme: dark)').matches) {
      this.$vuetify.theme.change('ledgerDark')
    }
  }
}
</script>

<style>
.v-application {
  background: var(--lg-paper) !important;
}

/* The page gutter every view shares, so bands can still run full-bleed. */
.lg-page {
  padding: 0 var(--lg-gutter);
}

.lg-inner {
  max-width: 1180px;
  margin: 0 auto;
}
</style>
