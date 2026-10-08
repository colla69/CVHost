<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ t.eyebrow(qualifications.length) }}</p>
        <h1 class="lg-heading page-title">{{ t.heading }}</h1>
        <p class="lg-prose page-intro">{{ t.intro }}</p>
      </header>

      <section v-for="group in groups" :key="group.key" class="group">
        <h2 class="group-head lg-eyebrow">{{ group.heading }}</h2>

        <div class="grid">
          <article v-for="item in group.items" :key="item.filename" class="cert">
            <button type="button" class="cert-sheet" @click="open(item)">
              <img :src="thumb(item)" :alt="t.firstPageOf($tr(item.name))" loading="lazy">
              <span class="cert-open">
                <v-icon icon="mdi-magnify-plus-outline" size="16"></v-icon>
                {{ t.view }}
              </span>
            </button>

            <div class="cert-meta">
              <h3 class="cert-name">{{ $tr(item.name) }}</h3>
              <p class="cert-issuer">{{ $tr(item.issuer) }}</p>
              <p class="cert-year lg-tnum">{{ item.year }}</p>
            </div>
          </article>
        </div>
      </section>

      <!-- Hand-authored copy from COPY below; never external input. -->
      <p class="lg-prose note" v-html="t.zipNote"></p>
    </div>

    <!-- Desktop gets an inline preview; phones open the file directly, because
         iOS Safari will not render a PDF inside an iframe at all. -->
    <v-dialog v-model="dialog" max-width="900">
      <v-card v-if="active" class="viewer">
        <v-card-title class="viewer-head">
          <span>{{ $tr(active.name) }}</span>
          <v-btn
            icon="mdi-close"
            variant="text"
            size="small"
            :aria-label="t.close"
            @click="dialog = false"
          ></v-btn>
        </v-card-title>
        <iframe :src="source(active)" :title="$tr(active.name)" class="viewer-frame"></iframe>
        <v-card-actions>
          <v-btn :href="source(active)" target="_blank" rel="noopener noreferrer" variant="text">
            {{ t.openInNewTab }}
          </v-btn>
          <v-btn :href="source(active)" download variant="text">{{ t.download }}</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { pick } from '@/i18n'

// zipNote is HTML (rendered with v-html); the rest is plain text. Group
// headings are flat keys rather than a nested object, so pick() can fall back
// and warn for each one.
const COPY = {
  en: {
    eyebrow: n => n + ' documents',
    heading: 'Certificates',
    intro: 'Diplomas, employment references, exam results and language certificates, as ' +
      'issued. Every one opens as a PDF.',
    groupProfessional: 'Professional',
    groupEducation: 'Education',
    groupLanguages: 'Languages',
    firstPageOf: name => 'First page of ' + name,
    view: 'View',
    close: 'Close',
    openInNewTab: 'Open in a new tab',
    download: 'Download',
    zipNote: 'Prefer everything in one file? The ' +
      '<a href="/data/CV_Docs.zip" download>complete document set</a> is a single download.'
  },
  de: {
    eyebrow: n => n + ' Dokumente',
    heading: 'Zeugnisse und Zertifikate',
    intro: 'Abschlusszeugnisse, Arbeitszeugnisse, Prüfungsergebnisse und Sprachzertifikate, so wie ' +
      'sie ausgestellt wurden. Jedes Dokument öffnet sich als PDF.',
    groupProfessional: 'Beruf',
    groupEducation: 'Schulbildung',
    groupLanguages: 'Sprachen',
    firstPageOf: name => 'Erste Seite: ' + name,
    view: 'Ansehen',
    close: 'Schließen',
    openInNewTab: 'In neuem Tab öffnen',
    download: 'Herunterladen',
    zipNote: 'Lieber alles in einer Datei? Die ' +
      '<a href="/data/CV_Docs.zip" download>vollständigen Unterlagen</a> gibt es als einen einzigen Download.'
  },
  it: {
    eyebrow: n => n + ' documenti',
    heading: 'Certificati',
    intro: 'Diplomi, referenze di lavoro, esiti d’esame e certificati linguistici, così come sono ' +
      'stati rilasciati. Ognuno si apre come PDF.',
    groupProfessional: 'Lavoro',
    groupEducation: 'Istruzione',
    groupLanguages: 'Lingue',
    firstPageOf: name => 'Prima pagina di ' + name,
    view: 'Visualizza',
    close: 'Chiudi',
    openInNewTab: 'Apri in una nuova scheda',
    download: 'Scarica',
    zipNote: 'Preferisce avere tutto in un unico file? La ' +
      '<a href="/data/CV_Docs.zip" download>documentazione completa</a> si scarica in una volta sola.'
  }
}

export default {
  name: 'qualifications',
  data: function () {
    return {
      dialog: false,
      active: null,
      // `group` is an English filter key, never shown; its heading is in COPY.
      // Official certificate titles and issuer names stay plain strings;
      // anything with words in it is a { en, de, it } map.
      qualifications: [
        {
          name: 'AWS Certified Solutions Architect — Associate',
          issuer: {
            en: 'Amazon Web Services · recertification · score 755/1000',
            de: 'Amazon Web Services · Rezertifizierung · Ergebnis 755/1000',
            it: 'Amazon Web Services · ricertificazione · punteggio 755/1000'
          },
          year: '2026',
          group: 'Professional',
          filename: 'AWS_SAA_2026.pdf'
        },
        {
          name: 'AWS Certified Solutions Architect — Associate',
          issuer: {
            en: 'Amazon Web Services · first certification · score 736/1000',
            de: 'Amazon Web Services · Erstzertifizierung · Ergebnis 736/1000',
            it: 'Amazon Web Services · prima certificazione · punteggio 736/1000'
          },
          year: '2022',
          group: 'Professional',
          filename: 'AWS_SAA_2022.pdf'
        },
        {
          name: 'Python (Basic)',
          issuer: 'HackerRank',
          year: '2020',
          group: 'Professional',
          filename: 'Python_cert.pdf'
        },
        {
          name: { en: 'Employment reference', de: 'Arbeitszeugnis', it: 'Referenza di lavoro (Arbeitszeugnis)' },
          issuer: '3Points Software GmbH',
          year: '2018',
          group: 'Professional',
          filename: 'ArbeitsZeugnis.pdf'
        },
        {
          name: {
            en: 'Fachinformatiker Anwendungsentwicklung',
            de: 'Fachinformatiker für Anwendungsentwicklung',
            it: 'Fachinformatiker für Anwendungsentwicklung'
          },
          issuer: { en: 'IHK München · final grade 71/100', de: 'IHK München · Abschlussnote 71/100', it: 'IHK München · voto finale 71/100' },
          year: '2013',
          group: 'Professional',
          filename: 'AUSB_IHK_Zeugnis.pdf'
        },
        {
          name: { en: 'Apprenticeship reference', de: 'Ausbildungszeugnis', it: 'Referenza di apprendistato (Ausbildungszeugnis)' },
          issuer: '3Points Software GmbH',
          year: '2013',
          group: 'Professional',
          filename: 'AUSB_3P_Zeugnis.pdf'
        },
        {
          name: { en: 'Diploma di Liceo Scientifico', de: 'Diploma di Liceo Scientifico (italienische Hochschulreife)', it: 'Diploma di Liceo Scientifico' },
          issuer: {
            en: 'Liceo Scientifico “Voltaire” · final grade 70/100',
            de: 'Liceo Scientifico „Voltaire“ · Abschlussnote 70/100',
            it: 'Liceo Scientifico “Voltaire” · voto finale 70/100'
          },
          year: '2011',
          group: 'Education',
          filename: 'ABI.pdf'
        },
        {
          name: 'TestDaF',
          issuer: 'Goethe-Institut München',
          year: '2010',
          group: 'Languages',
          filename: 'TESTDAF.pdf'
        },
        {
          name: { en: 'German B1 — Zertifikat Deutsch', de: 'Deutsch B1 — Zertifikat Deutsch', it: 'Tedesco B1 — Zertifikat Deutsch' },
          issuer: { en: 'Goethe-Institut Rome', de: 'Goethe-Institut Rom', it: 'Goethe-Institut Roma' },
          year: '2004',
          group: 'Languages',
          filename: 'ZD.pdf'
        },
        {
          name: { en: 'German A2 — Fit in Deutsch 2', de: 'Deutsch A2 — Fit in Deutsch 2', it: 'Tedesco A2 — Fit in Deutsch 2' },
          issuer: { en: 'Goethe-Institut Rome', de: 'Goethe-Institut Rom', it: 'Goethe-Institut Roma' },
          year: '2003',
          group: 'Languages',
          filename: 'FID2.pdf'
        },
        {
          name: { en: 'English B1 — PET', de: 'Englisch B1 — PET', it: 'Inglese B1 — PET' },
          issuer: { en: 'British Council Rome', de: 'British Council Rom', it: 'British Council Roma' },
          year: '2005',
          group: 'Languages',
          filename: 'PET.pdf'
        },
        {
          name: { en: 'English A2 — KET', de: 'Englisch A2 — KET', it: 'Inglese A2 — KET' },
          issuer: { en: 'British Council Rome', de: 'British Council Rom', it: 'British Council Roma' },
          year: '2004',
          group: 'Languages',
          filename: 'KET.pdf'
        }
      ]
    }
  },
  computed: {
    t () {
      return pick(COPY)
    },
    groups () {
      // In page order; a group with no entries is left out.
      const headings = {
        Professional: this.t.groupProfessional,
        Education: this.t.groupEducation,
        Languages: this.t.groupLanguages
      }
      return Object.keys(headings).map(key => ({
        key,
        heading: headings[key],
        items: this.qualifications.filter(item => item.group === key)
      })).filter(group => group.items.length)
    }
  },
  methods: {
    source (item) {
      return '/data/' + item.filename
    },
    thumb (item) {
      return '/img/certs/' + item.filename.replace(/\.pdf$/, '.jpg')
    },
    open (item) {
      if (this.$vuetify.display.smAndDown) {
        window.open(this.source(item), '_blank', 'noopener')
        return
      }
      this.active = item
      this.dialog = true
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

.group {
  margin-bottom: 2.5rem;
}

.group-head {
  border-top: 1px solid var(--lg-rule-strong);
  padding-top: 0.75rem;
  margin-bottom: 1.25rem;
  color: var(--lg-accent);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(min(100%, 13rem), 1fr));
  gap: 1.75rem 1.25rem;
}

.cert {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.cert-sheet {
  position: relative;
  display: block;
  width: 100%;
  padding: 0;
  border: 1px solid var(--lg-rule);
  border-radius: 3px;
  background: var(--lg-surface);
  overflow: hidden;
  cursor: pointer;
  box-shadow: var(--lg-shadow);
  transition: transform 0.15s ease, border-color 0.15s ease;
}

.cert-sheet:hover {
  transform: translateY(-2px);
  border-color: var(--lg-accent);
}

.cert-sheet img {
  display: block;
  width: 100%;
  height: auto;
  /* Keep the sheet's top edge aligned across mixed page sizes. */
  aspect-ratio: 1 / 1.36;
  object-fit: cover;
  object-position: top center;
}

.cert-open {
  position: absolute;
  right: 0.5rem;
  bottom: 0.5rem;
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.5rem;
  border-radius: 2px;
  background: var(--lg-accent);
  color: var(--lg-accent-ink);
  font-family: var(--lg-mono);
  font-size: 0.625rem;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  opacity: 0;
  transition: opacity 0.15s ease;
}

.cert-sheet:hover .cert-open,
.cert-sheet:focus-visible .cert-open {
  opacity: 1;
}

.cert-name {
  font-family: var(--lg-display);
  font-weight: 600;
  font-size: 0.9375rem;
  letter-spacing: -0.015em;
  line-height: 1.3;
  color: var(--lg-ink);
  margin: 0 0 0.25rem;
}

.cert-issuer {
  font-size: 0.8125rem;
  color: var(--lg-muted);
  margin: 0;
  line-height: 1.45;
}

.cert-year {
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.1em;
  color: var(--lg-faint);
  margin: 0.25rem 0 0;
}

.note {
  border-top: 1px solid var(--lg-rule);
  padding-top: 1.25rem;
  font-size: 0.9375rem;
}

.note :deep(a) {
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}

.viewer-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  font-family: var(--lg-display);
  font-size: 1rem;
  font-weight: 600;
}

.viewer-frame {
  width: 100%;
  height: min(72vh, 900px);
  border: 0;
  border-top: 1px solid var(--lg-rule);
  border-bottom: 1px solid var(--lg-rule);
  background: var(--lg-sunk);
}
</style>
