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
      // A bare year range or company name stays a plain string; anything with
      // words in it is a { en, de, it } map.
      roles: [
        {
          period: { en: '2024 — today', de: '2024 — heute', it: '2024 — oggi' },
          title: { en: 'Senior IT Consultant', de: 'Senior IT Consultant', it: 'Senior IT Consultant' },
          company: 'msg for banking AG · Ismaning',
          detail: {
            en: 'Currently replacing Excel-based processes for objection and damages ' +
              'cases at a public-sector client in healthcare, with AI-assisted engineering as ' +
              'a standing part of delivery. Before that the digital euro showcase for Pioneer ' +
              'Investments, BSI CRM platforms for Techem, the IFRS 9 regulatory platform at ' +
              'Porsche Bank and the GitLab runner migration into AWS.',
            de: 'Derzeit Ablösung Excel-basierter Prozesse für Widerspruchs- und Schadensfälle ' +
              'bei einem öffentlichen Auftraggeber im Gesundheitswesen, mit KI-gestützter ' +
              'Entwicklung als festem Bestandteil der Lieferung. Davor die Showcase-Anwendung zum ' +
              'digitalen Euro für Pioneer Investments, BSI-CRM-Plattformen für Techem, die ' +
              'IFRS-9-Regulatorikplattform bei der Porsche Bank und der Umzug der GitLab-Runner in ' +
              'die AWS-Cloud.',
            it: 'Attualmente sostituisco processi basati su Excel per ricorsi e danni presso un ' +
              'committente pubblico nella sanità, con lo sviluppo assistito dall’IA come parte ' +
              'stabile della consegna. In precedenza l’applicazione showcase per l’euro digitale ' +
              'per Pioneer Investments, le piattaforme CRM BSI per Techem, la piattaforma ' +
              'regolamentare IFRS 9 per Porsche Bank e la migrazione dei GitLab runner su AWS.'
          }
        },
        {
          period: '2021 — 2023',
          title: { en: 'IT Consultant', de: 'IT Consultant', it: 'IT Consultant' },
          company: 'msgGillardonBSM AG · Ismaning',
          detail: {
            en: 'More than 60 AWS microservices at Volkswagen Financial Services, the HEIDI ' +
              'sales portal at BMW Financial Services, and Mobih, an iOS maintenance application ' +
              'for Porsche production.',
            de: 'Mehr als 60 AWS-Microservices bei Volkswagen Financial Services, das ' +
              'HEIDI-Vertriebsportal bei BMW Financial Services und Mobih, eine iOS-Anwendung für ' +
              'die Instandhaltung in der Porsche-Produktion.',
            it: 'Oltre 60 microservizi AWS per Volkswagen Financial Services, il portale di vendita ' +
              'HEIDI per BMW Financial Services e Mobih, un’applicazione iOS per la manutenzione ' +
              'nella produzione Porsche.'
          }
        },
        {
          period: '2020 — 2021',
          title: { en: 'Software Engineer', de: 'Software-Ingenieur', it: 'Software Engineer' },
          company: {
            en: 'Device Insight GmbH (now KUKA Digital) · Munich',
            de: 'Device Insight GmbH (heute KUKA Digital) · München',
            it: 'Device Insight GmbH (oggi KUKA Digital) · Monaco di Baviera'
          },
          detail: {
            en: 'Energy monitoring and reporting for Schwarz IT, and a remote service platform ' +
              'for Krones bottling lines with Azure AD and AWS Cognito login.',
            de: 'Energiemonitoring und Reporting für Schwarz IT sowie eine Remote-Service-Plattform ' +
              'für Abfüllanlagen von Krones mit Login über Azure AD und AWS Cognito.',
            it: 'Monitoraggio energetico e reporting per Schwarz IT, e una piattaforma di ' +
              'teleassistenza per le linee di imbottigliamento Krones con login tramite Azure AD e ' +
              'AWS Cognito.'
          }
        },
        {
          period: '2018 — 2019',
          title: { en: 'DevOps Werkstudent', de: 'DevOps-Werkstudent', it: 'DevOps Werkstudent (studente lavoratore)' },
          company: 'msgGillardon AG',
          detail: {
            en: 'The HEIDI replatforming at BMW Financial Services: test and defect management, ' +
              'Java EE development and Jenkins pipelines.',
            de: 'Das HEIDI-Replatforming bei BMW Financial Services: Test- und Defect-Management, ' +
              'Java-EE-Entwicklung und Jenkins-Pipelines.',
            it: 'Il replatforming di HEIDI per BMW Financial Services: gestione di test e difetti, ' +
              'sviluppo Java EE e pipeline Jenkins.'
          }
        },
        {
          period: '2015 — 2018',
          title: { en: 'Senior Application Developer', de: 'Senior Application Developer', it: 'Senior Application Developer' },
          company: '3Points Software GmbH',
          detail: {
            en: 'Broadcasting rights management for Red Arrow International, and an automated ' +
              'release system in Bamboo and Jira that replaced hand-assembled releases.',
            de: 'Verwaltung von Ausstrahlungsrechten für Red Arrow International und ein ' +
              'automatisiertes Release-System in Bamboo und Jira, das die von Hand ' +
              'zusammengestellten Releases ablöste.',
            it: 'Gestione dei diritti di trasmissione per Red Arrow International e un sistema di ' +
              'release automatico in Bamboo e Jira che ha sostituito le release assemblate a mano.'
          }
        },
        {
          period: '2013 — 2015',
          title: { en: 'Junior Application Developer', de: 'Junior Application Developer', it: 'Junior Application Developer' },
          company: '3Points Software GmbH',
          detail: {
            en: 'Ground-up rebuild of a rights management system on Delphi XE and MS SQL Server.',
            de: 'Neuentwicklung eines Systems zur Rechteverwaltung auf Delphi XE und MS SQL Server.',
            it: 'Ricostruzione da zero di un sistema di gestione dei diritti su Delphi XE e MS SQL Server.'
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
