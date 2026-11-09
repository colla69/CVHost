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
    bioLead: 'Thirteen years in, most of my work comes back to the same habit: I learn a system ' +
      'well enough to see its structure, then write that structure down once, so nobody has to ' +
      'do the manual work or keep watch over it again.',
    bioHabit: 'I replaced hand-assembled releases with an automated delivery system. A one-off ' +
      'cloud migration also gave me a reusable template for every migration after it. I handed ' +
      'recurring engineering work to a set of agents that now do it. I have been doing some ' +
      'version of this since I was drawing motion paths in PowerPoint because it looked like ' +
      'programming.',
    bioClients: 'I spent five of those years consulting for Porsche, BMW Financial Services, ' +
      'Volkswagen Financial Services, Krones, a European asset manager and a public-sector ' +
      'client in healthcare.',
    bioStack: 'I work across the full stack, from Java and Jakarta EE backends to React, Angular ' +
      'and TypeScript front ends, with AWS infrastructure, Terraform and CI/CD as the constant ' +
      'thread. Most of what I enjoy sits between business and engineering: taking requirements ' +
      'straight from a business department and turning them into process design, estimates and ' +
      'documentation that developers can actually build from.',
    bioAi: 'Since 2025, AI-assisted engineering has been a regular part of how I work rather ' +
      'than an experiment: GitHub Copilot in the daily development loop, agentic workflows I ' +
      'built myself for the work that repeats, and getting a project team productive with both. ' +
      'I take it furthest in my own time, with ' +
      '<a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, a live trading system whose real subject ' +
      'is the eleven agents I built around it.',
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
    bioLead: 'Nach dreizehn Jahren läuft der Großteil meiner Arbeit auf dieselbe Gewohnheit ' +
      'hinaus: Ich arbeite mich so weit in ein System ein, dass ich seine Struktur sehe, und ' +
      'schreibe sie einmal fest, damit danach niemand mehr von Hand arbeiten oder aufpassen muss.',
    bioHabit: 'Von Hand zusammengestellte Releases habe ich durch ein automatisiertes ' +
      'Auslieferungssystem ersetzt. Aus einer einmaligen Cloud-Migration habe ich zusätzlich ' +
      'eine wiederverwendbare Vorlage für jede weitere gewonnen. Wiederkehrende ' +
      'Entwicklungsarbeit habe ich Agenten übergeben, die sie heute erledigen. In irgendeiner ' +
      'Form mache ich das, seit ich in PowerPoint Animationspfade gezeichnet habe, weil es nach ' +
      'Programmieren aussah.',
    bioClients: 'Fünf dieser Jahre war ich Consultant für Porsche, BMW Financial Services, ' +
      'Volkswagen Financial Services, Krones, einen europäischen Asset Manager und einen ' +
      'öffentlichen Auftraggeber im Gesundheitswesen.',
    bioStack: 'Ich arbeite über den gesamten Stack, von Java- und Jakarta-EE-Backends bis zu ' +
      'Frontends mit React, Angular und TypeScript, und durchgehend mit AWS-Infrastruktur, ' +
      'Terraform und CI/CD. Das meiste, was mir an der Arbeit Freude macht, liegt zwischen ' +
      'Fachbereich und Technik: Anforderungen direkt mit dem Fachbereich aufnehmen und daraus ' +
      'Prozesskonzepte, Schätzungen und Dokumentation machen, nach denen Entwickler tatsächlich ' +
      'bauen können.',
    bioAi: 'Seit 2025 ist KI-gestützte Entwicklung fester Bestandteil meiner Arbeit und kein ' +
      'Experiment mehr: GitHub Copilot im täglichen Entwicklungsablauf, selbst gebaute ' +
      'agentische Workflows für die Arbeit, die sich wiederholt, und ein Projektteam, das ich so ' +
      'anleite, dass es mit beidem produktiv arbeitet. Am weitesten treibe ich das in meiner ' +
      'Freizeit, mit <a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, einem Handelssystem im Live-Betrieb, ' +
      'dessen eigentliches Thema die elf Agenten sind, die ich darum herum gebaut habe.',
    bioLeading: 'Ich habe auf Berater- wie auf Kundenseite die fachliche Verantwortung für ' +
      'kleine Teams getragen und auf beiden Seiten Juniorentwickler betreut. Jetzt suche ich ' +
      'eine Rolle als Tech Lead mit End-to-End-Verantwortung für ein System und sein Team, in ' +
      'einem Umfeld, in dem technische Entscheidungen nah am Fachbereich fallen.',
    bioOrigin: 'Ich bin in Rom geboren und 2008 nach München gezogen. Italienisch ist meine ' +
      'Muttersprache; auf Deutsch und Englisch arbeite ich jeden Tag.',
    skillsHeading: 'Womit ich arbeite'
  },
  it: {
    eyebrow: 'Roma → Monaco di Baviera',
    heading: 'Chi sono',
    bioLead: 'Dopo tredici anni, gran parte del mio lavoro torna alla stessa abitudine: capisco ' +
      'un sistema abbastanza a fondo da vederne la struttura, poi la scrivo una volta sola, così ' +
      'nessuno deve più lavorare a mano o restare di guardia.',
    bioHabit: 'Ho sostituito le release assemblate a mano con un sistema di consegna automatico. ' +
      'Da una migrazione cloud una tantum ho ricavato anche un modello riutilizzabile per tutte ' +
      'quelle successive. Il lavoro di sviluppo ricorrente l’ho affidato ad agenti che oggi lo ' +
      'svolgono. Faccio una qualche versione di questo da quando disegnavo percorsi di ' +
      'animazione in PowerPoint perché sembrava programmazione.',
    bioClients: 'Per cinque di quegli anni ho lavorato come consulente per Porsche, BMW ' +
      'Financial Services, Volkswagen Financial Services, Krones, un asset manager europeo e un ' +
      'committente pubblico nella sanità.',
    bioStack: 'Lavoro sull’intero stack, dai backend Java e Jakarta EE ai frontend in React, ' +
      'Angular e TypeScript, e sempre con infrastruttura AWS, Terraform e CI/CD. Gran parte di ' +
      'ciò che mi piace di questo lavoro sta tra business e sviluppo: raccogliere i requisiti ' +
      'direttamente dall’unità di business e tradurli in progettazione dei processi, stime e ' +
      'documentazione su cui gli sviluppatori possano davvero costruire.',
    bioAi: 'Dal 2025 lo sviluppo assistito dall’IA è una parte stabile del mio lavoro, non più ' +
      'un esperimento: GitHub Copilot nel flusso di sviluppo quotidiano, workflow agentici ' +
      'costruiti da me per il lavoro che si ripete, e un team di progetto che affianco perché ' +
      'sia produttivo con entrambi. Dove mi spingo più avanti è nel tempo libero, con ' +
      '<a href="https://github.com/colla69/PlayCryptoWithAI" target="_blank" ' +
      'rel="noopener noreferrer">PlayCryptoWithAI</a>, un sistema di trading in produzione il cui ' +
      'vero tema sono gli undici agenti che gli ho costruito intorno.',
    bioLeading: 'Ho avuto responsabilità tecnica su piccoli team sia lato consulenza sia lato ' +
      'cliente, e in entrambi ho affiancato sviluppatori junior. Ora cerco un ruolo da Tech Lead ' +
      'con responsabilità end-to-end su un sistema e sul suo team, in un contesto dove le ' +
      'decisioni tecniche si prendono vicino al business.',
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
              'werkzeuggestütztes Refactoring und Testerstellung · Anleitung des Teams',
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
