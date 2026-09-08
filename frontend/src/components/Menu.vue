<template>
  <v-app-bar :elevation="0" height="68" class="lg-bar">
    <v-app-bar-nav-icon
      v-if="compact"
      :aria-label="drawer ? 'Close menu' : 'Open menu'"
      @click="drawer = !drawer"
    />

    <router-link to="/" class="brand" aria-label="Home">
      <span class="brand-mark">ACT</span>
      <span v-if="!compact" class="brand-name">Andrea Colarieti Tosti</span>
    </router-link>

    <v-spacer></v-spacer>

    <nav v-if="!compact" class="links" aria-label="Main">
      <router-link
        v-for="link in links"
        :key="link.to"
        :to="link.to"
        class="link"
        exact-active-class="link--on"
      >
        {{ link.label }}
      </router-link>
    </nav>

    <v-btn
      :icon="dark ? 'mdi-weather-sunny' : 'mdi-weather-night'"
      variant="text"
      size="small"
      class="ml-2"
      :aria-label="dark ? 'Switch to light theme' : 'Switch to dark theme'"
      @click="toggleTheme"
    ></v-btn>

    <v-btn
      v-if="!compact"
      :href="cvFile"
      download
      class="cta ml-3 mr-1"
      variant="flat"
      color="primary"
    >
      Download CV
    </v-btn>
  </v-app-bar>

  <v-navigation-drawer v-model="drawer" temporary location="start" width="272">
    <div class="drawer-head">
      <span class="brand-mark">ACT</span>
      <p class="lg-eyebrow mt-2">Senior IT Consultant</p>
    </div>
    <v-list nav density="comfortable">
      <v-list-item
        v-for="link in links"
        :key="link.to"
        :to="link.to"
        :title="link.label"
        :prepend-icon="link.icon"
        exact
      ></v-list-item>
    </v-list>
    <template #append>
      <div class="pa-4">
        <v-btn :href="cvFile" download block variant="flat" color="primary" class="cta">
          Download CV
        </v-btn>
        <v-btn
          href="mailto:a.colarietitosti@googlemail.com"
          block
          variant="outlined"
          class="mt-2 cta"
        >
          Get in touch
        </v-btn>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: 'Menu',
  data () {
    return {
      drawer: false,
      cvFile: '/data/CV_en.pdf',
      links: [
        { to: '/', label: 'Home', icon: 'mdi-home-outline' },
        { to: '/projectInfos', label: 'Work', icon: 'mdi-briefcase-outline' },
        { to: '/experience', label: 'Experience', icon: 'mdi-timeline-outline' },
        { to: '/qualifications', label: 'Certificates', icon: 'mdi-certificate-outline' },
        { to: '/news', label: 'Notes', icon: 'mdi-note-text-outline' },
        { to: '/aboutMe', label: 'About', icon: 'mdi-account-outline' },
        { to: '/contact', label: 'Contact', icon: 'mdi-email-outline' }
      ]
    }
  },
  computed: {
    // Seven links plus the brand and CTA still fit at 1280px; below that the
    // bar crowds, so it collapses into the drawer.
    compact () {
      return this.$vuetify.display.mdAndDown
    },
    dark () {
      return this.$vuetify.theme.name.value === 'ledgerDark'
    }
  },
  methods: {
    toggleTheme () {
      const next = this.dark ? 'ledgerLight' : 'ledgerDark'
      this.$vuetify.theme.change(next)
      try {
        window.localStorage.setItem('lg-theme', next)
      } catch (e) {
        // Private mode or blocked storage: the theme just won't persist.
      }
    }
  }
}
</script>

<style scoped>
.lg-bar {
  background: var(--lg-paper);
  border-bottom: 1px solid var(--lg-ink);
}

.brand {
  display: flex;
  align-items: baseline;
  gap: 0.75rem;
  text-decoration: none;
  margin-left: 0.5rem;
  min-width: 0;
}

.brand-mark {
  font-family: var(--lg-display);
  font-weight: 800;
  font-size: 1.375rem;
  letter-spacing: -0.045em;
  color: var(--lg-ink);
}

.brand-name {
  font-size: 0.875rem;
  color: var(--lg-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.links {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.link {
  font-size: 0.9375rem;
  color: var(--lg-muted);
  text-decoration: none;
  padding-bottom: 2px;
  border-bottom: 1.5px solid transparent;
  white-space: nowrap;
}

.link:hover {
  color: var(--lg-ink);
}

.link--on {
  color: var(--lg-ink);
  font-weight: 500;
  border-bottom-color: var(--lg-accent);
}

.cta {
  font-weight: 600;
}

.drawer-head {
  padding: 1.25rem 1.25rem 0.75rem;
  border-bottom: 1px solid var(--lg-rule);
}

.drawer-head .brand-mark {
  font-size: 1.5rem;
}
</style>
