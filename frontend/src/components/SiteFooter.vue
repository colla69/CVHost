<template>
  <footer class="foot lg-page">
    <div class="lg-inner">
      <div class="foot-grid">
        <div class="foot-id">
          <p class="brand-mark">ACT</p>
          <p class="foot-name">Andrea Colarieti Tosti</p>
          <p class="foot-role">Senior IT Consultant &middot; Tech Lead &middot; München</p>
        </div>

        <nav class="foot-col" aria-label="Pages">
          <p class="lg-eyebrow">Pages</p>
          <router-link v-for="link in links" :key="link.to" :to="link.to">
            {{ link.label }}
          </router-link>
        </nav>

        <div class="foot-col">
          <p class="lg-eyebrow">Elsewhere</p>
          <a href="mailto:a.colarietitosti@googlemail.com">Email</a>
          <a href="https://github.com/colla69" target="_blank" rel="noopener noreferrer">GitHub</a>
          <a
            href="https://www.linkedin.com/in/andrea-colarieti-662032197/"
            target="_blank"
            rel="noopener noreferrer"
          >LinkedIn</a>
        </div>

        <div class="foot-col">
          <p class="lg-eyebrow">Curriculum</p>
          <a href="/data/CV_en.pdf" download>CV — English</a>
          <a href="/data/Lebenslauf.pdf" download>Lebenslauf — Deutsch</a>
          <a href="/data/CV_it.pdf" download>CV — Italiano</a>
        </div>
      </div>

      <p class="foot-fine">
        Built with Vue and Vuetify. Photography credits in
        <code>public/img/CREDITS.md</code>. No trackers, no cookies, no third-party fonts.
      </p>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'SiteFooter',
  data () {
    return {
      links: [
        { to: '/projectInfos', label: 'Work' },
        { to: '/experience', label: 'Experience' },
        { to: '/qualifications', label: 'Certificates' },
        { to: '/news', label: 'Notes' },
        { to: '/aboutMe', label: 'About' }
      ]
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

.foot-fine code {
  font-family: var(--lg-mono);
  font-size: 0.9em;
}
</style>
