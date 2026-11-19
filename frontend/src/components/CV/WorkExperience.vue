<template>
  <section class="section">
    <h2 class="section-head lg-heading">{{ t.heading }}</h2>

    <ol class="index">
      <li v-for="role in roles" :key="$tr(role.period) + $tr(role.company)" class="entry">
        <span class="entry-period lg-tnum">{{ $tr(role.period) }}</span>
        <div class="entry-body">
          <h3 class="entry-role">{{ $tr(role.title) }}</h3>
          <p class="entry-company">{{ $tr(role.company) }}</p>
          <p v-if="role.detail" class="entry-detail">{{ $tr(role.detail) }}</p>
        </div>
      </li>
    </ol>
  </section>
</template>

<script>
import { pick } from '@/i18n'
import { ROLES } from './roles'

const COPY = {
  en: {
    heading: 'Professional experience'
  },
  de: {
    heading: 'Beruflicher Werdegang'
  },
  it: {
    heading: 'Esperienza professionale'
  }
}

export default {
  name: 'WorkExperience',
  data () {
    return {
      roles: ROLES
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

.entry-role {
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: 1.125rem;
  letter-spacing: -0.02em;
  color: var(--lg-ink);
  margin: 0;
  text-wrap: balance;
}

.entry-company {
  font-size: 0.9375rem;
  color: var(--lg-muted);
  margin: 0.125rem 0 0;
}

.entry-detail {
  font-size: 0.9375rem;
  color: var(--lg-faint);
  margin: 0.625rem 0 0;
  max-width: 62ch;
  line-height: 1.6;
  font-weight: 300;
}
</style>
