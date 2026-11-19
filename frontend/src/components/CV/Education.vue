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
import { STEPS } from './education'

const COPY = {
  en: {
    heading: 'Education'
  },
  de: {
    heading: 'Ausbildung'
  },
  it: {
    heading: 'Istruzione e formazione'
  }
}

export default {
  name: 'education',
  data () {
    return {
      steps: STEPS
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
