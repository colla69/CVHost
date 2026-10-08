<template>
  <section class="card">
    <h2 class="card-head lg-eyebrow">{{ t.heading }}</h2>
    <dl class="rows">
      <div v-for="language in languages" :key="$tr(language.name)" class="row">
        <dt>
          <span class="lang-name">{{ $tr(language.name) }}</span>
          <span class="lang-level">{{ $tr(language.level) }}</span>
        </dt>
        <dd>
          <p v-for="proof in language.proof" :key="$tr(proof)" class="proof">{{ $tr(proof) }}</p>
        </dd>
      </div>
    </dl>
  </section>
</template>

<script>
import { pick } from '@/i18n'

const COPY = {
  en: {
    heading: 'Languages'
  },
  de: {
    heading: 'Sprachen'
  },
  it: {
    heading: 'Lingue'
  }
}

export default {
  name: 'Languages',
  data () {
    return {
      languages: [
        {
          name: { en: 'Italian', de: 'Italienisch', it: 'Italiano' },
          level: { en: 'Native', de: 'Muttersprache', it: 'Madrelingua' },
          proof: [
            { en: 'Born in Rome; schooling in Rome and Udine.', de: 'Geboren in Rom; Schulzeit in Rom und Udine.', it: 'Nato a Roma; scuola a Roma e a Udine.' }
          ]
        },
        {
          name: { en: 'German', de: 'Deutsch', it: 'Tedesco' },
          // \u00AD is a soft hyphen: the one long German word has to break to fit
          // the 7rem label column. Keep it.
          level: { en: 'Fluent', de: 'Verhandlungs\u00ADsicher', it: 'Fluente' },
          proof: [
            {
              en: 'Part of secondary school and the vocational training in Germany.',
              de: 'Ein Teil der Sekundarstufe und die Berufsausbildung in Deutschland.',
              it: 'Parte della scuola secondaria e la formazione professionale svolte in Germania.'
            },
            { en: 'TestDaF — Goethe-Institut München, 2010', de: 'TestDaF — Goethe-Institut München, 2010', it: 'TestDaF — Goethe-Institut München, 2010' },
            { en: 'B1 Zertifikat Deutsch — Goethe-Institut Rome, 2004', de: 'B1 Zertifikat Deutsch — Goethe-Institut Rom, 2004', it: 'B1 Zertifikat Deutsch — Goethe-Institut Roma, 2004' }
          ]
        },
        {
          name: { en: 'English', de: 'Englisch', it: 'Inglese' },
          // \u00AD is a soft hyphen: the one long German word has to break to fit
          // the 7rem label column. Keep it.
          level: { en: 'Fluent', de: 'Verhandlungs\u00ADsicher', it: 'Fluente' },
          proof: [
            { en: 'Professional working language.', de: 'Berufliche Arbeitssprache.', it: 'Lingua di lavoro.' },
            { en: 'B1 Preliminary (PET) — British Council Rome, 2005', de: 'B1 Preliminary (PET) — British Council Rom, 2005', it: 'B1 Preliminary (PET) — British Council Roma, 2005' },
            { en: 'A2 Key (KET) — British Council Rome, 2004', de: 'A2 Key (KET) — British Council Rom, 2004', it: 'A2 Key (KET) — British Council Roma, 2004' }
          ]
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
