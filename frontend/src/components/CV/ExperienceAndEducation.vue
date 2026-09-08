<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">2013 to today</p>
        <h1 class="lg-heading page-title">Experience</h1>
        <p class="lg-prose page-intro">
          Where I have worked and what I studied. The project-by-project detail lives under
          <router-link to="/projectInfos">Work</router-link>.
        </p>
      </header>

      <work-experience></work-experience>

      <section class="section">
        <h2 class="section-head lg-heading">Certifications</h2>
        <div class="cert">
          <span class="cert-period lg-tnum">2022 — 2029</span>
          <div>
            <h3 class="cert-title">AWS Certified Solutions Architect — Associate</h3>
            <p class="cert-detail">
              First certified December 2022, recertified April 2026, valid until April 2029.
            </p>
          </div>
        </div>
      </section>

      <education></education>
    </div>
  </div>
</template>

<script>

import WorkExperience from '@/components/CV/WorkExperience'
import Education from '@/components/CV/Education'

export default {
  name: 'ExperienceAndEducation',
  components: { Education, WorkExperience }
}
</script>

<style scoped>
.page-head {
  padding-top: clamp(2rem, 5vw, 3.5rem);
  padding-bottom: 1.5rem;
  border-bottom: 2px solid var(--lg-ink);
  margin-bottom: var(--lg-band);
}

.page-title {
  font-size: clamp(2rem, 6vw, 3.25rem);
  margin: 0.375rem 0 0.75rem;
}

.page-intro {
  margin: 0;
  font-size: 1.0625rem;
}

.page-intro a {
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}

.section {
  margin-bottom: var(--lg-band);
}

.section-head {
  font-size: clamp(1.375rem, 3.4vw, 1.875rem);
  border-top: 2px solid var(--lg-ink);
  padding-top: 0.875rem;
  margin: 0 0 1.5rem;
}

.cert {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.375rem;
}

@media (min-width: 720px) {
  .cert {
    grid-template-columns: 9.5rem minmax(0, 1fr);
    gap: 2rem;
  }
}

.cert-period {
  font-family: var(--lg-mono);
  font-size: 0.75rem;
  letter-spacing: 0.06em;
  color: var(--lg-accent);
  white-space: nowrap;
  padding-top: 0.25rem;
}

.cert-title {
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: 1.125rem;
  letter-spacing: -0.02em;
  color: var(--lg-ink);
  margin: 0;
  text-wrap: balance;
}

.cert-detail {
  font-size: 0.9375rem;
  color: var(--lg-muted);
  margin: 0.25rem 0 0;
  font-weight: 300;
}
</style>
