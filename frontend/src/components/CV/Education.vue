<template>
  <section class="section">
    <h2 class="section-head lg-heading">{{ t.heading }}</h2>

    <ol class="index">
      <li v-for="step in steps" :key="step.period + $tr(step.title)" class="entry">
        <span class="entry-period lg-tnum">{{ step.period }}</span>
        <div class="entry-body">
          <h3 class="entry-title">{{ $tr(step.title) }}</h3>
          <p v-if="step.detail" class="entry-detail">{{ $tr(step.detail) }}</p>
        </div>
      </li>
    </ol>
  </section>
</template>

<script>
import { pick } from '@/i18n'

const COPY = {
  en: {
    heading: 'Education'
  }
}

export default {
  name: 'education',
  data () {
    return {
      // School names are maps too: each language may render or gloss them
      // (and their quotation marks) its own way.
      steps: [
        {
          period: '2011 — 2013',
          title: { en: 'Ausbildung: Fachinformatiker für Anwendungsentwicklung (IHK)' },
          detail: { en: '3Points Software GmbH · final grade 71/100' }
        },
        {
          period: '2010 — 2011',
          title: { en: 'Diploma di Liceo Scientifico' },
          detail: { en: 'Liceo Scientifico “Voltaire”, Italy · final grade 70/100' }
        },
        {
          period: '2008 — 2010',
          title: { en: 'Max-Planck-Gymnasium München' },
          detail: { en: 'Scientific gymnasium, after moving to Munich in September 2008' }
        },
        {
          period: '2006 — 2008',
          title: { en: 'Liceo Scientifico “Niccolò Copernico”' },
          detail: { en: 'Udine, Italy' }
        },
        {
          period: '2002 — 2005',
          title: { en: 'Scuola media “Ignazio Silone”' },
          detail: { en: 'Rome, Italy' }
        },
        {
          period: '1997 — 2002',
          title: { en: 'Scuola primaria “Walt Disney”' },
          detail: { en: 'Rome, Italy' }
        }
      ]
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
.section {
  margin-bottom: var(--lg-band);
}

.section-head {
  font-size: clamp(1.375rem, 3.4vw, 1.875rem);
  border-top: 2px solid var(--lg-ink);
  padding-top: 0.875rem;
  margin: 0 0 1.5rem;
}

.index {
  list-style: none;
  margin: 0;
  padding: 0;
}

.entry {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.375rem;
  padding: 1.25rem 0;
  border-top: 1px solid var(--lg-rule);
}

.entry:first-child {
  border-top: none;
  padding-top: 0;
}

@media (min-width: 720px) {
  .entry {
    grid-template-columns: 9.5rem minmax(0, 1fr);
    gap: 2rem;
  }
}

.entry-period {
  font-family: var(--lg-mono);
  font-size: 0.75rem;
  letter-spacing: 0.06em;
  color: var(--lg-accent);
  white-space: nowrap;
  padding-top: 0.25rem;
}

.entry-title {
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: 1.0625rem;
  letter-spacing: -0.02em;
  color: var(--lg-ink);
  margin: 0;
  text-wrap: balance;
}

.entry-detail {
  font-size: 0.9375rem;
  color: var(--lg-muted);
  margin: 0.25rem 0 0;
  font-weight: 300;
}
</style>
