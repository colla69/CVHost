<template>
  <section class="card">
    <h2 class="card-head lg-eyebrow">Languages</h2>
    <dl class="rows">
      <div v-for="language in languages" :key="language.name" class="row">
        <dt>
          <span class="lang-name">{{ language.name }}</span>
          <span class="lang-level">{{ language.level }}</span>
        </dt>
        <dd>
          <p v-for="proof in language.proof" :key="proof" class="proof">{{ proof }}</p>
        </dd>
      </div>
    </dl>
  </section>
</template>

<script>
export default {
  name: 'Languages',
  data () {
    return {
      languages: [
        {
          name: 'Italian',
          level: 'Native',
          proof: ['Born in Rome; schooling in Rome and Udine.']
        },
        {
          name: 'German',
          level: 'Fluent',
          proof: [
            'Secondary schooling and vocational training completed in Germany.',
            'TestDaF — Goethe-Institut München, 2010',
            'B1 Zertifikat Deutsch — Goethe-Institut Rome, 2004'
          ]
        },
        {
          name: 'English',
          level: 'Fluent',
          proof: [
            'Professional working language.',
            'B1 Preliminary (PET) — British Council Rome, 2005',
            'A2 Key (KET) — British Council Rome, 2004'
          ]
        }
      ]
    }
  }
}
</script>

<style scoped>
.card-head {
  border-top: 1px solid var(--lg-rule-strong);
  padding-top: 0.75rem;
  margin-bottom: 0.75rem;
  color: var(--lg-accent);
}

.rows {
  margin: 0;
}

.row {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.375rem;
  padding: 0.875rem 0;
  border-top: 1px solid var(--lg-rule);
}

.row:first-child {
  border-top: none;
}

@media (min-width: 480px) {
  .row {
    grid-template-columns: 7rem minmax(0, 1fr);
    gap: 1rem;
  }
}

.row dt {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 0.5rem;
}

@media (min-width: 480px) {
  .row dt {
    display: block;
  }
}

.lang-name {
  display: block;
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: 1rem;
  letter-spacing: -0.015em;
  color: var(--lg-ink);
}

.lang-level {
  display: block;
  font-family: var(--lg-mono);
  font-size: 0.625rem;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--lg-faint);
  margin-top: 0.125rem;
}

.row dd {
  margin: 0;
}

.proof {
  margin: 0 0 0.25rem;
  font-size: 0.875rem;
  color: var(--lg-muted);
  font-weight: 300;
  line-height: 1.5;
}

.proof:last-child {
  margin-bottom: 0;
}
</style>
