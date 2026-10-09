<template>
  <footer class="foot lg-page">
    <div class="lg-inner">
      <div class="foot-grid">
        <div class="foot-id">
          <p class="brand-mark">ACT</p>
          <p class="foot-name">Andrea Colarieti Tosti</p>
          <p class="foot-role">{{ t.role }}</p>
        </div>

        <nav class="foot-col" :aria-label="t.pages">
          <p class="lg-eyebrow">{{ t.pages }}</p>
          <router-link v-for="link in links" :key="link.to" :to="link.to">
            {{ $tr(link.label) }}
          </router-link>
        </nav>

        <div class="foot-col">
          <p class="lg-eyebrow">{{ t.elsewhere }}</p>
          <a href="mailto:a.colarietitosti@googlemail.com">{{ t.email }}</a>
          <a href="https://github.com/colla69" target="_blank" rel="noopener noreferrer">GitHub</a>
          <a
            href="https://www.linkedin.com/in/andrea-colarieti-662032197/"
            target="_blank"
            rel="noopener noreferrer"
          >LinkedIn</a>
        </div>

        <div class="foot-col">
          <p class="lg-eyebrow">{{ t.curriculum }}</p>
          <!-- Each CV is named in its own language, like the language switcher,
               so these labels are the same on every version of the page. -->
          <a href="/data/CV_en.pdf" download lang="en">CV — English</a>
          <a href="/data/Lebenslauf.pdf" download lang="de">Lebenslauf — Deutsch</a>
          <a href="/data/CV_it.pdf" download lang="it">CV — Italiano</a>
        </div>
      </div>

      <!-- Hand-authored copy from COPY below; never external input. -->
      <p class="foot-fine" v-html="t.fine"></p>
    </div>
  </footer>
</template>

<script>
import { pick } from '@/i18n'
import { PAGES } from '@/nav'

// Page names come from PAGES in src/nav.js, shared with the app bar.
const COPY = {
  en: {
    role: 'Senior IT Consultant · Tech Lead · München',
    pages: 'Pages',
    elsewhere: 'Elsewhere',
    email: 'Email',
    curriculum: 'Curriculum',
    fine: 'Built with Vue and Vuetify. Photography credits in <code>public/img/CREDITS.md</code>. ' +
      'No trackers, no cookies, no third-party fonts.'
  },
  de: {
    role: 'Senior IT Consultant · Tech Lead · München',
    pages: 'Seiten',
    elsewhere: 'Anderswo',
    email: 'E-Mail',
    curriculum: 'Lebenslauf',
    fine: 'Gebaut mit Vue und Vuetify. Bildnachweise in <code>public/img/CREDITS.md</code>. ' +
      'Keine Tracker, keine Cookies, keine Schriften von Drittanbietern.'
  },
  it: {
    role: 'Senior IT Consultant · Tech Lead · Monaco di Baviera',
    pages: 'Pagine',
    elsewhere: 'Altrove',
    email: 'E-mail',
    curriculum: 'Curriculum',
    fine: 'Realizzato con Vue e Vuetify. Crediti fotografici in <code>public/img/CREDITS.md</code>. ' +
      'Nessun tracker, nessun cookie, nessun font di terze parti.'
  }
}

export default {
  name: 'SiteFooter',
  data () {
    return {
      links: PAGES.filter(page => page.footer)
    }
  },
  computed: {
    t () {
      return pick(COPY)
    }
  }
}
</script>

<style scoped>
.foot {
  border-top: 2px solid var(--lg-ink);
  margin-top: var(--lg-band);
  padding-top: 2rem;
  padding-bottom: 2.5rem;
}

.foot-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
}

@media (min-width: 700px) {
  .foot-grid {
    grid-template-columns: 1.4fr repeat(3, minmax(0, 1fr));
    gap: 2.5rem;
  }
}

.brand-mark {
  font-family: var(--lg-display);
  font-weight: 800;
  font-size: 1.5rem;
  letter-spacing: -0.045em;
  color: var(--lg-ink);
  margin: 0 0 0.5rem;
}

.foot-name {
  font-weight: 600;
  color: var(--lg-ink);
  margin: 0;
}

.foot-role {
  color: var(--lg-muted);
  font-size: 0.875rem;
  margin: 0.125rem 0 0;
}

.foot-col {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 0.5rem;
}

.foot-col .lg-eyebrow {
  margin-bottom: 0.25rem;
}

.foot-col a {
  color: var(--lg-muted);
  text-decoration: none;
  font-size: 0.9375rem;
  border-bottom: 1px solid transparent;
}

.foot-col a:hover {
  color: var(--lg-ink);
  border-bottom-color: var(--lg-accent);
}

.foot-fine {
  margin: 2.5rem 0 0;
  padding-top: 1rem;
  border-top: 1px solid var(--lg-rule);
  font-size: 0.8125rem;
  color: var(--lg-faint);
}

.foot-fine :deep(code) {
  font-family: var(--lg-mono);
  font-size: 0.9em;
}
</style>
