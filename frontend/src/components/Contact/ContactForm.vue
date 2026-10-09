<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ t.eyebrow }}</p>
        <h1 class="lg-heading page-title">{{ t.heading }}</h1>
        <p class="lg-prose page-intro">{{ t.intro }}</p>
      </header>

      <section class="channels">
        <a
          v-for="channel in channels"
          :key="channel.href"
          :href="channel.href"
          :target="channel.external ? '_blank' : null"
          :rel="channel.external ? 'noopener noreferrer' : null"
          class="channel"
        >
          <v-icon :icon="channel.icon" size="20" class="channel-icon"></v-icon>
          <span class="channel-label lg-eyebrow">{{ $tr(channel.label) }}</span>
          <span class="channel-value">{{ channel.value }}</span>
        </a>
      </section>

      <section class="downloads">
        <h2 class="section-head lg-heading">{{ t.cvHeading }}</h2>
        <p class="lg-prose downloads-intro">{{ t.cvIntro }}</p>
        <!-- Each CV is named in its own language, like the language switcher. -->
        <div class="download-row">
          <v-btn
            v-for="language in languages"
            :key="language.code"
            :href="language.cv"
            :lang="language.code"
            download
            variant="outlined"
            size="large"
            class="download"
            prepend-icon="mdi-file-download-outline"
          >
            {{ language.name }}
          </v-btn>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { LANGUAGES, pick } from '@/i18n'

const COPY = {
  en: {
    eyebrow: 'Open to tech lead and team lead roles',
    heading: 'Get in touch',
    intro: 'Email is the quickest way to reach me, and I answer every message that is not a ' +
      'mailshot. If you are hiring, the CV below has the detail this site summarises.',
    cvHeading: 'Curriculum vitae',
    cvIntro: 'The same document in three languages. It carries the personal details kept off ' +
      'this public page — address, phone number and date of birth.'
  },
  de: {
    eyebrow: 'Offen für Rollen als Tech Lead und Teamleiter',
    heading: 'Kontakt aufnehmen',
    intro: 'Per E-Mail erreichen Sie mich am schnellsten, und ich beantworte jede Nachricht, die ' +
      'kein Massenversand ist. Wenn Sie eine Stelle besetzen, finden Sie im Lebenslauf unten die ' +
      'Details, die diese Website nur zusammenfasst.',
    cvHeading: 'Lebenslauf',
    cvIntro: 'Dasselbe Dokument in drei Sprachen. Es enthält die persönlichen Angaben, die auf dieser ' +
      'öffentlichen Seite bewusst fehlen — Adresse, Telefonnummer und Geburtsdatum.'
  },
  it: {
    eyebrow: 'Disponibile per ruoli da Tech Lead e Team Leader',
    heading: 'Mi contatti',
    intro: 'L’e-mail è il modo più rapido per raggiungermi, e rispondo a ogni messaggio che non ' +
      'sia un invio di massa. Se sta assumendo, il CV qui sotto contiene i dettagli che questo ' +
      'sito riassume.',
    cvHeading: 'Curriculum vitae',
    cvIntro: 'Lo stesso documento in tre lingue. Contiene i dati personali che restano fuori da ' +
      'questa pagina pubblica — indirizzo, numero di telefono e data di nascita.'
  }
}

export default {
  name: 'Contact',
  data () {
    return {
      languages: LANGUAGES,
      channels: [
        {
          label: { en: 'Email', de: 'E-Mail', it: 'E-mail' },
          value: 'a.colarietitosti@googlemail.com',
          href: 'mailto:a.colarietitosti@googlemail.com',
          icon: 'mdi-email-outline'
        },
        {
          label: 'LinkedIn',
          value: 'andrea-colarieti',
          href: 'https://www.linkedin.com/in/andrea-colarieti-662032197/',
          icon: 'mdi-linkedin',
          external: true
        },
        {
          label: 'GitHub',
          value: 'github.com/colla69',
          href: 'https://github.com/colla69',
          icon: 'mdi-github',
          external: true
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
.page-head {
  padding-top: clamp(2rem, 5vw, 3.5rem);
  padding-bottom: 1.5rem;
  border-bottom: 2px solid var(--lg-ink);
  margin-bottom: 2rem;
}

.page-title {
  font-size: clamp(2rem, 6vw, 3.25rem);
  margin: 0.375rem 0 0.75rem;
}

.page-intro {
  margin: 0;
  font-size: 1.0625rem;
}

.channels {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(100%, 15rem), 1fr));
  gap: 1px;
  background: var(--lg-rule);
  border: 1px solid var(--lg-rule);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: var(--lg-band);
}

.channel {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 1.25rem;
  background: var(--lg-surface);
  text-decoration: none;
  transition: background 0.15s ease;
}

.channel:hover {
  background: var(--lg-accent-soft);
}

.channel-icon {
  color: var(--lg-accent);
  margin-bottom: 0.375rem;
}

.channel-value {
  color: var(--lg-ink);
  font-size: 0.9375rem;
  overflow-wrap: anywhere;
}

.section-head {
  font-size: clamp(1.375rem, 3.4vw, 1.875rem);
  border-top: 2px solid var(--lg-ink);
  padding-top: 0.875rem;
  margin: 0 0 0.75rem;
}

.downloads-intro {
  margin: 0 0 1.25rem;
  font-size: 0.9375rem;
}

.download-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.download {
  border-color: var(--lg-rule-strong);
  color: var(--lg-ink);
}
</style>
