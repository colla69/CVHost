<template>
  <section class="card">
    <h2 class="card-head lg-eyebrow">{{ t.heading }}</h2>
    <dl class="rows">
      <div v-for="item in infos" :key="$tr(item.name)" class="row">
        <dt>{{ $tr(item.name) }}</dt>
        <dd>
          <a v-if="item.href" :href="item.href" :target="item.external ? '_blank' : null"
             :rel="item.external ? 'noopener noreferrer' : null">{{ $tr(item.value) }}</a>
          <span v-else>{{ $tr(item.value) }}</span>
        </dd>
      </div>
    </dl>
    <!-- The link's href is bound, so the sentence is split around it, not v-html. -->
    <p class="card-note">
      {{ t.noteBefore }}<a :href="$lang.language.cv" download>{{ t.noteLink }}</a>{{ t.noteAfter }}
    </p>
  </section>
</template>

<script>
import { pick } from '@/i18n'

// noteBefore and noteAfter carry their own spaces next to the link.
const COPY = {
  en: {
    heading: 'Details',
    noteBefore: 'Address, phone number and date of birth are in the ',
    noteLink: 'CV download',
    noteAfter: ' rather than on this public page.'
  },
  de: {
    heading: 'Eckdaten',
    noteBefore: 'Adresse, Telefonnummer und Geburtsdatum stehen im ',
    noteLink: 'Lebenslauf zum Download',
    noteAfter: ' und nicht auf dieser öffentlichen Seite.'
  },
  it: {
    heading: 'Informazioni',
    noteBefore: 'Indirizzo, numero di telefono e data di nascita sono nel ',
    noteLink: 'CV da scaricare',
    noteAfter: ' e non su questa pagina pubblica.'
  }
}

export default {
  name: 'PersonalInfo',
  data () {
    return {
      infos: [
        {
          name: { en: 'Based in', de: 'Standort', it: 'Con base a' },
          value: { en: 'München, Germany', de: 'München, Deutschland', it: 'Monaco di Baviera, Germania' }
        },
        {
          name: { en: 'Email', de: 'E-Mail', it: 'E-mail' },
          value: 'a.colarietitosti@googlemail.com',
          href: 'mailto:a.colarietitosti@googlemail.com'
        },
        {
          name: 'GitHub',
          value: 'github.com/colla69',
          href: 'https://github.com/colla69',
          external: true
        },
        {
          name: 'LinkedIn',
          value: 'andrea-colarieti',
          href: 'https://www.linkedin.com/in/andrea-colarieti-662032197/',
          external: true
        },
        {
          name: { en: 'Open to', de: 'Offen für', it: 'Disponibile per' },
          value: { en: 'Tech lead and team lead roles', de: 'Rollen als Tech Lead und Teamleiter', it: 'Ruoli da Tech Lead e Team Leader' }
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
  gap: 0.125rem;
  padding: 0.75rem 0;
  border-top: 1px solid var(--lg-rule);
}

.row:first-child {
  border-top: none;
}

@media (min-width: 480px) {
  .row {
    grid-template-columns: 7rem minmax(0, 1fr);
    gap: 1rem;
    align-items: baseline;
  }
}

.row dt {
  font-family: var(--lg-mono);
  font-size: 0.625rem;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--lg-faint);
}

.row dd {
  margin: 0;
  color: var(--lg-ink);
  font-size: 0.9375rem;
  overflow-wrap: anywhere;
}

.row dd a {
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}

.row dd a:hover {
  opacity: 0.75;
}

.card-note {
  margin: 1rem 0 0;
  padding-top: 0.875rem;
  border-top: 1px solid var(--lg-rule);
  font-size: 0.8125rem;
  color: var(--lg-faint);
  line-height: 1.55;
}

.card-note a {
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}
</style>
