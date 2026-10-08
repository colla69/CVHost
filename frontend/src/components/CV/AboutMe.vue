<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ t.eyebrow }}</p>
        <h1 class="lg-heading page-title">{{ t.heading }}</h1>
      </header>

      <section class="intro">
        <img
          :src="portrait"
          alt="Andrea Colarieti Tosti"
          class="portrait"
          width="742"
          height="900"
        >
        <div class="bio">
          <p class="lg-prose bio-lead">{{ t.bioLead }}</p>
          <p class="lg-prose">{{ t.bioHabit }}</p>
          <p class="lg-prose">{{ t.bioClients }}</p>
          <p class="lg-prose">{{ t.bioStack }}</p>
          <!-- Hand-authored copy from COPY below; never external input. -->
          <p class="lg-prose" v-html="t.bioAi"></p>
          <p class="lg-prose">{{ t.bioLeading }}</p>
          <p class="lg-prose">{{ t.bioOrigin }}</p>
        </div>
      </section>

      <section class="section">
        <h2 class="section-head lg-heading">{{ t.skillsHeading }}</h2>
        <div class="skills">
          <div v-for="group in skills" :key="$tr(group.name)" class="skill-group">
            <h3 class="skill-name lg-eyebrow">{{ $tr(group.name) }}</h3>
            <p class="skill-items">{{ $tr(group.items) }}</p>
          </div>
        </div>
      </section>

      <section class="section">
        <div class="split">
          <PersonalInfo></PersonalInfo>
          <Languages></Languages>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { pick } from '@/i18n'
import PersonalInfo from '@/components/CV/PersonalInfo'
import Languages from '@/components/CV/Languages'
import portrait from '@/assets/foto.jpg'

// One key per bio paragraph, so each falls back to English on its own.
// bioAi is HTML (rendered with v-html); the rest is plain text.
const COPY = {
  en: {
    eyebrow: 'Rome → Munich',
    heading: 'About',
    bioLead: 'Thirteen years in, most of my work comes back to the same move: understand a ' +
      'system well enough to see its structure, then write that structure down once so the ' +
      'manual work — or the vigilance — never has to happen again.',
    bioHabit: 'Hand-assembled releases became an automated delivery system. A one-off cloud ' +
      'migration also produced the reusable template for every migration after it. Recurring ' +
      'engineering work became a set of agents that now do it. I have been doing some version ' +
      'of this since I was drawing motion paths in PowerPoint because it looked like programming.',
    bioClients: 'Five of those years consulting for Porsche, BMW Financial Services, Volkswagen ' +
      'Financial Services, Krones, a European asset manager and a public-sector client in ' +
      'healthcare.',
    bioStack: 'I work across the full stack — Java and Jakarta EE backends through React, ' +
      'Angular and TypeScript front ends — with AWS infrastructure, Terraform and CI/CD as the ' +
      'constant thread. Most of what I enjoy sits at the seam between business and ' +
      'engineering: taking requirements straight from a business department and turning them ' +
      'into process design, estimates and documentation that developers can actually build from.',
    bioAi: 'Since 2025, AI-assisted engineering has been a standing part of how I deliver rather ' +
      'than an experiment: GitHub Copilot and Claude in the daily development loop, agentic ' +
      'workflows I built myself for the work that repeats, and getting a project team ' +
      'productive with both. The sharpest version of it is off the clock — ' +
      '<a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, a live trading system whose real ' +
      'subject is the eleven agents built around it.',
    bioLeading: 'I have carried technical responsibility for small teams on both the ' +
      'consultancy and the client side, and mentored junior developers in both. What I am ' +
      'looking for now is a tech lead role with end-to-end ownership of a system and of the ' +
      'team around it, somewhere engineering decisions are made close to the business.',
    bioOrigin: 'I was born in Rome and moved to Munich in 2008. Italian is my first language; ' +
      'I work in German and English every day.',
    skillsHeading: 'What I work with'
  },
  de: {
    eyebrow: 'Rom → München',
    heading: 'Über mich',
    bioLead: 'Nach dreizehn Jahren läuft der Großteil meiner Arbeit auf dieselbe Bewegung hinaus: ' +
      'ein System so weit durchdringen, dass seine Struktur sichtbar wird, und diese Struktur ' +
      'einmal festschreiben, damit die Handarbeit — oder die Wachsamkeit — danach nicht mehr ' +
      'nötig ist.',
    bioHabit: 'Aus von Hand zusammengestellten Releases wurde ein automatisiertes ' +
      'Auslieferungssystem. Aus einer einmaligen Cloud-Migration wurde zusätzlich die ' +
      'wiederverwendbare Vorlage für jede weitere. Aus wiederkehrender Entwicklungsarbeit wurden ' +
      'Agenten, die sie heute erledigen. In irgendeiner Form mache ich das, seit ich in PowerPoint ' +
      'Animationspfade gezeichnet habe, weil es nach Programmieren aussah.',
    bioClients: 'Fünf dieser Jahre als Consultant für Porsche, BMW Financial Services, ' +
      'Volkswagen Financial Services, Krones, einen europäischen Asset Manager und einen ' +
      'öffentlichen Auftraggeber im Gesundheitswesen.',
    bioStack: 'Ich arbeite über den gesamten Stack — von Java- und Jakarta-EE-Backends bis zu ' +
      'Frontends mit React, Angular und TypeScript — und durchgehend mit AWS-Infrastruktur, ' +
      'Terraform und CI/CD. Das meiste, was mir an der Arbeit Freude macht, liegt an der ' +
      'Nahtstelle zwischen beidem: Anforderungen direkt mit dem Fachbereich aufnehmen und in ' +
      'Prozesskonzeption, Schätzungen und Dokumentation überführen, die Entwickler tatsächlich ' +
      'umsetzen können.',
    bioAi: 'Seit 2025 ist KI-gestützte Entwicklung fester Bestandteil meiner Lieferung und kein ' +
      'Experiment mehr: GitHub Copilot und Claude im täglichen Entwicklungsablauf, selbst gebaute ' +
      'agentische Workflows für die Arbeit, die sich wiederholt, und die Befähigung eines ' +
      'Projektteams, mit beidem produktiv zu arbeiten. Am weitesten getrieben habe ich das ' +
      'außerhalb der Arbeitszeit — ' +
      '<a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, ein Handelssystem im Live-Betrieb, dessen ' +
      'eigentliches Thema das Review-Gremium aus elf Agenten darum herum ist.',
    bioLeading: 'Ich habe auf Berater- wie auf Kundenseite die fachliche Verantwortung für kleine ' +
      'Teams getragen und auf beiden Seiten Juniorentwickler betreut. Was ich jetzt suche, ist ' +
      'eine Rolle als Tech Lead mit End-to-End-Verantwortung für ein System und für das Team ' +
      'darum, in einem Umfeld, in dem technische Entscheidungen nah am Fachbereich fallen.',
    bioOrigin: 'Ich bin in Rom geboren und 2008 nach München gezogen. Italienisch ist meine ' +
      'Muttersprache; auf Deutsch und Englisch arbeite ich jeden Tag.',
    skillsHeading: 'Womit ich arbeite'
  },
  it: {
    eyebrow: 'Roma → Monaco di Baviera',
    heading: 'Chi sono',
    bioLead: 'Dopo tredici anni, gran parte del mio lavoro torna alla stessa mossa: capire un ' +
      'sistema abbastanza a fondo da vederne la struttura, e poi scrivere quella struttura una ' +
      'volta sola, così che il lavoro manuale — o l’attenzione costante — non serva più.',
    bioHabit: 'Da release assemblate a mano è nato un sistema di consegna automatico. Da una ' +
      'migrazione cloud una tantum è nato anche il modello riutilizzabile per tutte quelle ' +
      'successive. Dal lavoro di sviluppo ricorrente sono nati agenti che oggi lo svolgono. Faccio ' +
      'una qualche versione di questo da quando disegnavo percorsi di animazione in PowerPoint ' +
      'perché sembrava programmazione.',
    bioClients: 'Cinque di quegli anni come consulente per Porsche, BMW Financial Services, ' +
      'Volkswagen Financial Services, Krones, un asset manager europeo e un committente pubblico ' +
      'nella sanità.',
    bioStack: 'Lavoro sull’intero stack — dai backend Java e Jakarta EE ai frontend in React, ' +
      'Angular e TypeScript — con l’infrastruttura AWS, Terraform e CI/CD come costante. Gran ' +
      'parte di ciò che mi piace di questo lavoro sta nel punto di giunzione tra le due cose: ' +
      'raccogliere i requisiti direttamente dall’unità di business e tradurli in progettazione dei ' +
      'processi, stime e documentazione su cui gli sviluppatori possano davvero costruire.',
    bioAi: 'Dal 2025 lo sviluppo assistito dall’IA è una parte stabile del mio modo di consegnare, ' +
      'non più un esperimento: GitHub Copilot e Claude nel flusso di sviluppo quotidiano, workflow ' +
      'agentici costruiti da me per il lavoro che si ripete, e un team di progetto messo in ' +
      'condizione di essere produttivo con entrambi. La versione più spinta è fuori dall’orario ' +
      'di lavoro — ' +
      '<a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, un sistema di trading in esercizio il cui ' +
      'vero tema è il comitato di revisione di undici agenti che lo circonda.',
    bioLeading: 'Ho avuto responsabilità tecnica su piccoli team sia lato consulenza sia lato ' +
      'cliente, e in entrambi ho affiancato sviluppatori junior. Ora cerco un ruolo da Tech Lead ' +
      'con responsabilità end-to-end su un sistema e sul team che lo circonda, in un contesto dove ' +
      'le decisioni tecniche si prendono vicino al business.',
    bioOrigin: 'Sono nato a Roma e nel 2008 mi sono trasferito a Monaco di Baviera. L’italiano è ' +
      'la mia lingua madre; lavoro ogni giorno in tedesco e in inglese.',
    skillsHeading: 'Con cosa lavoro'
  }
}

export default {
  name: 'aboutMe',
  components: { Languages, PersonalInfo },
  data () {
    return {
      portrait,
      // Lists made only of product names stay plain strings; anything with
      // words in it is a { en, de, it } map.
      skills: [
        {
          name: { en: 'Backend / JVM', de: 'Backend / JVM', it: 'Backend / JVM' },
          items: 'Java · Spring · Spring Boot · Jakarta EE · Hibernate · WildFly · Payara'
        },
        {
          name: { en: 'AI-assisted engineering', de: 'KI-gestützte Entwicklung', it: 'Sviluppo assistito dall’IA' },
          items: {
            en: 'GitHub Copilot · Claude · agentic development workflows · prompt patterns · ' +
              'tool-assisted refactoring and test generation · enabling a team to work this way',
            de: 'GitHub Copilot · Claude · agentische Entwicklungs-Workflows · Prompt-Patterns · ' +
              'werkzeuggestütztes Refactoring und Testerstellung · Befähigung des Teams',
            it: 'GitHub Copilot · Claude · workflow di sviluppo agentici · prompt pattern · ' +
              'refactoring e generazione di test assistiti · affiancamento del team'
          }
        },
        {
          name: { en: 'Frontend', de: 'Frontend', it: 'Frontend' },
          items: 'TypeScript · React · Angular · RxJS · Vue.js · Pinia · JSF 2.0 · Vaadin · Material UI'
        },
        {
          name: { en: 'Node / APIs', de: 'Node / APIs', it: 'Node / API' },
          items: {
            en: 'NestJS · Express · Prisma · REST · backend-for-frontend architectures',
            de: 'NestJS · Express · Prisma · REST · Backend-for-Frontend-Architekturen',
            it: 'NestJS · Express · Prisma · REST · architetture backend-for-frontend'
          }
        },
        {
          name: { en: 'Cloud & IaC', de: 'Cloud & IaC', it: 'Cloud & IaC' },
          items: 'AWS (Lambda, DynamoDB, SNS, SQS, EKS, Fargate, Cognito, VPC, Route 53, ' +
            'CodePipeline, CodeArtifact) · Terraform · AWS CDK · Azure AD / SAML'
        },
        {
          name: { en: 'Containers & ops', de: 'Container & Betrieb', it: 'Container & operations' },
          items: 'Docker · Kubernetes · Linux · nginx'
        },
        {
          name: { en: 'CI/CD', de: 'CI/CD', it: 'CI/CD' },
          items: 'GitLab CI · Jenkins · Bamboo · Concourse CI · GitHub'
        },
        {
          name: { en: 'Data & messaging', de: 'Daten & Messaging', it: 'Dati & messaging' },
          items: 'PostgreSQL · Oracle · MS SQL Server · DynamoDB · Kafka · SQL'
        },
        {
          name: { en: 'Testing & quality', de: 'Test & Qualität', it: 'Test & qualità' },
          items: 'JUnit · Jest · Cypress · Supertest · Cucumber · Selenium · SonarQube · K6'
        },
        {
          name: { en: 'Ways of working', de: 'Arbeitsweise', it: 'Metodo di lavoro' },
          items: {
            en: 'Technical responsibility for teams of 2–3 · mentoring juniors · requirements ' +
              'workshops · effort estimation · release planning · Scrum · DevSecOps',
            de: 'Fachliche Verantwortung für Teams von 2–3 · Betreuung von Junioren · ' +
              'Anforderungsworkshops · Aufwandsschätzung · Release-Planung · Scrum · DevSecOps',
            it: 'Responsabilità tecnica su team di 2–3 persone · affiancamento di junior · workshop ' +
              'sui requisiti · stime di effort · pianificazione delle release · Scrum · DevSecOps'
          }
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
  margin: 0.375rem 0 0;
}

.intro {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.75rem;
  margin-bottom: var(--lg-band);
}

@media (min-width: 800px) {
  .intro {
    grid-template-columns: minmax(0, 17rem) minmax(0, 1fr);
    gap: 3rem;
    align-items: start;
  }
}

.portrait {
  display: block;
  width: 100%;
  max-width: 17rem;
  height: auto;
  border: 1px solid var(--lg-rule);
  border-radius: 3px;
  background: var(--lg-sunk);
}

.bio-lead {
  font-size: clamp(1.0625rem, 2.2vw, 1.25rem);
  color: var(--lg-ink);
  font-weight: 400;
}

.bio p + p {
  margin-top: 1rem;
}

.bio p {
  margin-bottom: 0;
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

.skills {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(100%, 17rem), 1fr));
  gap: 1.5rem 2rem;
}

.skill-group {
  border-top: 1px solid var(--lg-rule);
  padding-top: 0.75rem;
}

.skill-name {
  color: var(--lg-accent);
  margin-bottom: 0.5rem;
}

.skill-items {
  margin: 0;
  font-size: 0.9375rem;
  color: var(--lg-muted);
  line-height: 1.65;
  font-weight: 300;
}

.split {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2.5rem;
}

@media (min-width: 800px) {
  .split {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 3rem;
  }
}
</style>
