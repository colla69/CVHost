<template>
  <div class="home">

    <!-- ── Hero ──────────────────────────────────────────────── -->
    <section class="hero lg-page">
      <div class="lg-inner">
        <p class="lg-eyebrow">{{ t.eyebrow }}</p>
        <h1 class="lg-display hero-name">Andrea Colarieti&nbsp;Tosti</h1>
        <!-- Hand-authored copy from COPY below; never external input. -->
        <p class="lg-prose hero-pitch" v-html="t.pitch(yearsEngineering)"></p>
        <p class="lg-prose hero-ai" v-html="t.agents"></p>
        <p class="lg-prose hero-seeking">{{ t.seeking(yearsConsulting) }}</p>
        <div class="hero-cta">
          <v-btn :href="$lang.language.cv" download size="large" variant="flat" color="primary">
            {{ t.downloadCv }}
          </v-btn>
          <v-btn
            href="mailto:a.colarietitosti@googlemail.com"
            size="large"
            variant="outlined"
            class="btn-quiet"
          >
            {{ t.getInTouch }}
          </v-btn>
        </div>
      </div>
    </section>

    <!-- ── Clients ───────────────────────────────────────────── -->
    <section class="strip lg-page">
      <div class="lg-inner strip-inner">
        <span class="strip-label">{{ t.deliveredFor }}</span>
        <span v-for="client in clients" :key="$tr(client)" class="strip-item">{{ $tr(client) }}</span>
      </div>
    </section>

    <!-- ── Figures ───────────────────────────────────────────── -->
    <section class="figures lg-page">
      <div class="lg-inner figures-grid">
        <div v-for="figure in figures" :key="figure.label" class="figure">
          <b class="lg-tnum">{{ figure.value }}</b>
          <span>{{ figure.label }}</span>
        </div>
      </div>
    </section>

    <!-- ── Selected work ─────────────────────────────────────── -->
    <section class="band lg-page">
      <div class="lg-inner">
        <div class="band-head">
          <h2 class="lg-heading">{{ t.selectedWork }}</h2>
          <router-link to="/projectInfos" class="band-more">
            {{ t.allProjects(projectCount) }}
            <v-icon icon="mdi-arrow-right" size="16"></v-icon>
          </router-link>
        </div>

        <ul class="worklist">
          <li v-for="project in featured" :key="project.id" class="work">
            <span class="work-title">{{ $tr(project.Name) }}</span>
            <span class="work-client">{{ $tr(project.client || project.company_name) }}</span>
            <span class="work-stack">{{ $tr(project.lang) }}</span>
            <span class="work-years lg-tnum">{{ years(project) }}</span>
          </li>
        </ul>
      </div>
    </section>

    <!-- ── Notes ─────────────────────────────────────────────── -->
    <section class="band lg-page">
      <div class="lg-inner">
        <div class="band-head">
          <h2 class="lg-heading">{{ t.latestNotes }}</h2>
          <router-link to="/news" class="band-more">
            {{ t.allNotes }}
            <v-icon icon="mdi-arrow-right" size="16"></v-icon>
          </router-link>
        </div>

        <div class="notes">
          <article v-for="item in latestNotes" :key="item.id" class="note">
            <v-img :src="item.img_link" :alt="$tr(item.title)" height="150" cover class="note-img"></v-img>
            <p class="note-date lg-tnum">{{ noteDate(item.release_date) }}</p>
            <h3 class="note-title">{{ $tr(item.title) }}</h3>
          </article>
        </div>
      </div>
    </section>

    <!-- ── Close ─────────────────────────────────────────────── -->
    <section class="closer lg-page">
      <div class="lg-inner closer-inner">
        <div>
          <h2 class="lg-heading">{{ t.closerHeading }}</h2>
          <p class="lg-prose closer-text">{{ t.closerText }}</p>
        </div>
        <div class="hero-cta">
          <v-btn :href="$lang.language.cv" download size="large" variant="flat" color="primary">
            {{ t.downloadCv }}
          </v-btn>
          <v-btn to="/contact" size="large" variant="outlined" class="btn-quiet">
            {{ $tr(contactLabel) }}
          </v-btn>
        </div>
      </div>
    </section>

  </div>
</template>

<script>
import { pick } from '@/i18n'
import { pageLabel } from '@/nav'
import projects from '@/components/projectInfos/project_infos.json'
import news from '@/components/news/news.json'

// Careers started in 2013, consulting in 2021. Deriving the figures keeps the
// page from quietly going stale the way the old hardcoded copy did.
const CAREER_START = 2013
const CONSULTING_START = 2021

// pitch and agents are HTML (rendered with v-html); the rest is plain text.
const COPY = {
  en: {
    eyebrow: 'Senior IT Consultant · Tech Lead · München',
    pitch: n => '<b>' + n + ' years</b> building software, and most of it comes down to one ' +
      'habit: I learn a system well enough to see its structure, then encode that structure ' +
      'once, so nobody has to do the manual work or keep watch over it again.',
    agents: 'I replaced hand-assembled releases with an automated delivery system. A one-off ' +
      'cloud migration also gave me a reusable template for every migration after it. I handed ' +
      'recurring engineering work to <b>a set of agents that now do it</b>.',
    seeking: n => 'I have spent ' + n + ' of those years as a consultant for Porsche, BMW ' +
      'Financial Services, Volkswagen Financial Services, Krones and a public-sector client in ' +
      'healthcare. Now I am looking for work with end-to-end ownership of a system, where ' +
      'whoever designs it stays responsible for it, and where making the right thing automatic is part of the ' +
      'job rather than something done in the gaps.',
    downloadCv: 'Download CV',
    getInTouch: 'Get in touch',
    deliveredFor: 'Delivered for',
    yearsEngineering: 'Years engineering',
    yearsConsulting: 'Years consulting',
    clientsAndTeams: 'Clients & teams',
    awsArchitect: 'AWS certified architect',
    selectedWork: 'Selected work',
    allProjects: n => 'All ' + n + ' projects',
    latestNotes: 'Latest notes',
    allNotes: 'All notes',
    closerHeading: 'Hiring for a tech lead role?',
    closerText: 'The full CV is one download, in English, German or Italian. Certificates and ' +
      'references are on the site as well.'
  },
  de: {
    eyebrow: 'Senior IT Consultant · Tech Lead · München',
    pitch: n => '<b>' + n + ' Jahre</b> Softwareentwicklung, und fast immer läuft es auf ' +
      'dieselbe Gewohnheit hinaus: Ich arbeite mich so weit in ein System ein, dass ich seine ' +
      'Struktur sehe, und schreibe sie einmal fest, damit danach niemand mehr von Hand arbeiten ' +
      'oder aufpassen muss.',
    agents: 'Von Hand zusammengestellte Releases habe ich durch ein automatisiertes ' +
      'Auslieferungssystem ersetzt. Aus einer einmaligen Cloud-Migration habe ich zusätzlich ' +
      'eine wiederverwendbare Vorlage für jede weitere gewonnen. Wiederkehrende ' +
      'Entwicklungsarbeit habe ich <b>Agenten übergeben, die sie heute erledigen</b>.',
    seeking: n => n + ' dieser Jahre war ich Consultant für Porsche, BMW Financial Services, ' +
      'Volkswagen Financial Services, Krones und einen öffentlichen Auftraggeber im ' +
      'Gesundheitswesen. Jetzt suche ich Arbeit mit End-to-End-Verantwortung für ein System: Wer ' +
      'es entwirft, bleibt dafür zuständig, und dafür zu sorgen, dass das Richtige automatisch ' +
      'passiert, gehört zur Aufgabe und läuft nicht nebenbei.',
    downloadCv: 'Lebenslauf herunterladen',
    getInTouch: 'Kontakt aufnehmen',
    deliveredFor: 'Projekte für',
    yearsEngineering: 'Jahre Entwicklung',
    yearsConsulting: 'Jahre Beratung',
    clientsAndTeams: 'Kunden & Teams',
    awsArchitect: 'AWS-zertifizierter Architekt',
    selectedWork: 'Ausgewählte Projekte',
    allProjects: n => 'Alle ' + n + ' Projekte',
    latestNotes: 'Neueste Notizen',
    allNotes: 'Alle Notizen',
    closerHeading: 'Sie suchen einen Tech Lead?',
    closerText: 'Den vollständigen Lebenslauf können Sie mit einem Klick auf Englisch, Deutsch oder Italienisch ' +
      'herunterladen. Zeugnisse und Zertifikate finden Sie ebenfalls hier auf der Website.'
  },
  it: {
    eyebrow: 'Senior IT Consultant · Tech Lead · Monaco di Baviera',
    pitch: n => '<b>' + n + ' anni</b> di sviluppo software e quasi sempre la stessa abitudine: ' +
      'capisco un sistema abbastanza a fondo da vederne la struttura, poi la scrivo una volta ' +
      'sola, così nessuno deve più lavorare a mano o restare di guardia.',
    agents: 'Ho sostituito le release assemblate a mano con un sistema di consegna automatico. ' +
      'Da una migrazione cloud una tantum ho ricavato anche un modello riutilizzabile per tutte ' +
      'quelle successive. Il lavoro di sviluppo ricorrente l’ho affidato ad <b>agenti che oggi ' +
      'lo svolgono</b>.',
    seeking: n => 'Per ' + n + ' di quegli anni ho lavorato come consulente per Porsche, BMW ' +
      'Financial Services, Volkswagen Financial Services, Krones e un committente pubblico nella ' +
      'sanità. Ora cerco lavoro con responsabilità end-to-end su un sistema: chi lo progetta ' +
      'continua a occuparsene, e fare in modo che la cosa giusta avvenga in automatico fa parte ' +
      'del mestiere, non dei ritagli di tempo.',
    downloadCv: 'Scarica il CV',
    getInTouch: 'Mi contatti',
    deliveredFor: 'Progetti per',
    yearsEngineering: 'Anni di sviluppo',
    yearsConsulting: 'Anni di consulenza',
    clientsAndTeams: 'Clienti e team',
    awsArchitect: 'Architetto certificato AWS',
    selectedWork: 'Progetti selezionati',
    allProjects: n => 'Tutti i ' + n + ' progetti',
    latestNotes: 'Note recenti',
    allNotes: 'Tutte le note',
    closerHeading: 'Sta cercando un Tech Lead?',
    closerText: 'Può scaricare il CV completo con un clic in inglese, tedesco o italiano. Sul sito trova ' +
      'anche certificati e referenze.'
  }
}

export default {
  name: 'Home',
  data () {
    const thisYear = new Date().getFullYear()
    return {
      yearsEngineering: thisYear - CAREER_START,
      yearsConsulting: thisYear - CONSULTING_START,
      projectCount: projects.length,
      contactLabel: pageLabel('/contact'),
      clients: [
        'Porsche',
        'BMW Financial Services',
        'Volkswagen Financial Services',
        'Krones',
        'Techem',
        'Schwarz IT',
        'Pioneer Investments',
        { en: 'Public sector · healthcare', de: 'Öffentlicher Sektor · Gesundheitswesen', it: 'Settore pubblico · sanità' }
      ],
      // Slice before reverse: the JSON import is a shared module-level array.
      featured: projects
        .filter(project => project.featured)
        .slice()
        .sort((a, b) => b.start_date.localeCompare(a.start_date)),
      latestNotes: news.slice(-3).reverse()
    }
  },
  computed: {
    t () {
      return pick(COPY)
    },
    figures () {
      return [
        { value: this.yearsEngineering, label: this.t.yearsEngineering },
        { value: this.yearsConsulting, label: this.t.yearsConsulting },
        { value: '20+', label: this.t.clientsAndTeams },
        { value: 'SA–A', label: this.t.awsArchitect }
      ]
    }
  },
  methods: {
    years (project) {
      const start = new Date(project.start_date).getFullYear()
      if (!project.end_date) return start + '—'
      const end = new Date(project.end_date).getFullYear()
      return start === end ? String(start) : start + '—' + String(end).slice(2)
    },
    noteDate (value) {
      return new Date(value).toLocaleDateString(this.$lang.language.dates, { year: 'numeric', month: 'short' })
    }
  }
}
</script>

<style scoped>
/* ── Hero ─────────────────────────────────────────────────── */

.hero {
  padding-top: clamp(2.5rem, 7vw, 5.5rem);
  padding-bottom: clamp(2rem, 5vw, 3.5rem);
}

.hero-name {
  font-size: clamp(2.5rem, 9vw, 5.5rem);
  margin: 0.5rem 0 1.25rem;
  max-width: 14ch;
}

.hero-pitch {
  font-size: clamp(1.0625rem, 2.2vw, 1.3125rem);
  margin: 0 0 1rem;
}

.hero-pitch :deep(b) {
  color: var(--lg-ink);
  font-weight: 600;
}

.hero-ai {
  font-size: clamp(0.9375rem, 1.9vw, 1.0625rem);
  margin: 0 0 1.25rem;
}

.hero-ai :deep(b) {
  color: var(--lg-accent);
  font-weight: 600;
}

.hero-seeking {
  font-size: clamp(0.9375rem, 1.8vw, 1.0625rem);
  margin: 0 0 2rem;
  padding-left: 1rem;
  border-left: 2px solid var(--lg-accent);
}

.hero-cta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.btn-quiet {
  border-color: var(--lg-rule-strong);
  color: var(--lg-ink);
}

/* ── Client strip ─────────────────────────────────────────── */

.strip {
  border-top: 1px solid var(--lg-rule);
  border-bottom: 1px solid var(--lg-rule);
  padding-top: 1.125rem;
  padding-bottom: 1.125rem;
}

.strip-inner {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 0.5rem 1.75rem;
  font-family: var(--lg-mono);
  font-size: 0.75rem;
  letter-spacing: 0.13em;
  text-transform: uppercase;
}

.strip-label {
  color: var(--lg-accent);
  font-weight: 500;
}

.strip-item {
  color: var(--lg-faint);
}

/* ── Figures ──────────────────────────────────────────────── */

.figures {
  border-bottom: 1px solid var(--lg-rule);
}

.figures-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

@media (min-width: 760px) {
  .figures-grid {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}

.figure {
  padding: 1.5rem 1.25rem 1.75rem;
  border-left: 1px solid var(--lg-rule);
  border-top: 1px solid var(--lg-rule);
}

/* Only the leftmost cell of each row loses its left rule. */
.figure:nth-child(odd) {
  border-left: none;
  padding-left: 0;
}

.figure:nth-child(-n + 2) {
  border-top: none;
}

@media (min-width: 760px) {
  .figure:nth-child(odd) {
    border-left: 1px solid var(--lg-rule);
    padding-left: 1.25rem;
  }

  .figure:first-child {
    border-left: none;
    padding-left: 0;
  }

  .figure {
    border-top: none;
  }
}

.figure b {
  display: block;
  font-family: var(--lg-display);
  font-size: clamp(2.25rem, 5vw, 3.25rem);
  font-weight: 700;
  letter-spacing: -0.045em;
  line-height: 1;
  color: var(--lg-accent);
}

.figure span {
  display: block;
  margin-top: 0.625rem;
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.11em;
  text-transform: uppercase;
  color: var(--lg-faint);
  line-height: 1.5;
  /* German compounds outgrow the narrow figure cells at 320px. */
  hyphens: auto;
}

/* ── Bands ────────────────────────────────────────────────── */

.band {
  padding-top: var(--lg-band);
}

.band-head {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  justify-content: space-between;
  gap: 0.75rem;
  border-top: 2px solid var(--lg-ink);
  padding-top: 0.875rem;
  margin-bottom: 1.5rem;
}

.band-head h2 {
  font-size: clamp(1.375rem, 3.4vw, 1.875rem);
  margin: 0;
}

.band-more {
  display: inline-flex;
  align-items: center;
  gap: 0.375rem;
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--lg-accent);
  text-decoration: none;
  white-space: nowrap;
}

.band-more:hover {
  opacity: 0.72;
}

/* ── Selected work ────────────────────────────────────────── */

.worklist {
  list-style: none;
  margin: 0;
  padding: 0;
}

.work {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.125rem;
  padding: 1rem 0;
  border-top: 1px solid var(--lg-rule);
}

.work:first-child {
  border-top: none;
}

@media (min-width: 800px) {
  .work {
    grid-template-columns: minmax(0, 1.5fr) minmax(0, 1fr) minmax(0, 1.2fr) auto;
    gap: 1.5rem;
    align-items: baseline;
  }
}

.work-title {
  font-weight: 600;
  color: var(--lg-ink);
  font-size: 1.0625rem;
}

.work-client {
  color: var(--lg-accent);
  font-size: 0.9375rem;
}

.work-stack {
  color: var(--lg-faint);
  font-size: 0.8125rem;
  font-family: var(--lg-mono);
  line-height: 1.5;
}

.work-years {
  font-family: var(--lg-mono);
  font-size: 0.8125rem;
  color: var(--lg-faint);
  white-space: nowrap;
}

/* ── Notes ────────────────────────────────────────────────── */

.notes {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}

@media (min-width: 700px) {
  .notes {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

.note-img {
  border: 1px solid var(--lg-rule);
  border-radius: 3px;
  background: var(--lg-sunk);
}

.note-date {
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.11em;
  text-transform: uppercase;
  color: var(--lg-faint);
  margin: 0.875rem 0 0.25rem;
}

.note-title {
  font-family: var(--lg-display);
  font-weight: 600;
  font-size: 1.0625rem;
  letter-spacing: -0.015em;
  line-height: 1.3;
  color: var(--lg-ink);
  margin: 0;
}

/* ── Closer ───────────────────────────────────────────────── */

.closer {
  margin-top: var(--lg-band);
  border-top: 2px solid var(--lg-ink);
  padding-top: 2rem;
}

.closer-inner {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  justify-content: space-between;
  gap: 1.5rem;
}

.closer-text {
  margin: 0.5rem 0 0;
  max-width: 46ch;
}
</style>
