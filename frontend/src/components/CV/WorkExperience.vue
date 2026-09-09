<template>
  <section class="section">
    <h2 class="section-head lg-heading">Professional experience</h2>

    <ol class="index">
      <li v-for="role in roles" :key="role.period + role.company" class="entry">
        <span class="entry-period lg-tnum">{{ role.period }}</span>
        <div class="entry-body">
          <h3 class="entry-role">{{ role.title }}</h3>
          <p class="entry-company">{{ role.company }}</p>
          <p v-if="role.detail" class="entry-detail">{{ role.detail }}</p>
        </div>
      </li>
    </ol>
  </section>
</template>

<script>
export default {
  name: 'WorkExperience',
  data () {
    return {
      roles: [
        {
          period: '2024 — today',
          title: 'Senior IT Consultant',
          company: 'msg for banking AG · Ismaning',
          detail: 'Currently replacing Excel-based processes for objection and damages ' +
            'cases at a public-sector client in healthcare, with AI-assisted engineering as ' +
            'a standing part of delivery. Before that the digital euro showcase for Pioneer ' +
            'Investments, BSI CRM platforms for Techem, the IFRS 9 regulatory platform at ' +
            'Porsche Bank and the GitLab runner migration into AWS.'
        },
        {
          period: '2021 — 2023',
          title: 'IT Consultant',
          company: 'msgGillardonBSM AG · Ismaning',
          detail: 'More than 60 AWS microservices at Volkswagen Financial Services, the HEIDI ' +
            'sales portal at BMW Financial Services, and Mobih, an iOS maintenance application ' +
            'for Porsche production.'
        },
        {
          period: '2020 — 2021',
          title: 'Software Engineer',
          company: 'Device Insight GmbH (now KUKA Digital) · Munich',
          detail: 'Energy monitoring and reporting for Schwarz IT, and a remote service platform ' +
            'for Krones bottling lines with Azure AD and AWS Cognito login.'
        },
        {
          period: '2018 — 2019',
          title: 'DevOps Werkstudent',
          company: 'msgGillardon AG',
          detail: 'The HEIDI replatforming at BMW Financial Services: test and defect management, ' +
            'Java EE development and Jenkins pipelines.'
        },
        {
          period: '2015 — 2018',
          title: 'Senior Application Developer',
          company: '3Points Software GmbH',
          detail: 'Broadcasting rights management for Red Arrow International, and an automated ' +
            'release system in Bamboo and Jira that replaced hand-assembled releases.'
        },
        {
          period: '2013 — 2015',
          title: 'Junior Application Developer',
          company: '3Points Software GmbH',
          detail: 'Ground-up rebuild of a rights management system on Delphi XE and MS SQL Server.'
        }
      ]
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
