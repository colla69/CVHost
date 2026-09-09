<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">Rome → Munich</p>
        <h1 class="lg-heading page-title">About</h1>
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
          <p class="lg-prose bio-lead">
            I am a software engineer with thirteen years behind me, the last five spent consulting
            for Porsche, BMW Financial Services, Volkswagen Financial Services, Krones, a European
            asset manager and a public-sector client in healthcare.
          </p>
          <p class="lg-prose">
            I work across the full stack — Java and Jakarta EE backends through React, Angular and
            TypeScript front ends — with AWS infrastructure, Terraform and CI/CD as the constant
            thread. Most of what I enjoy sits at the seam between the two: taking requirements
            straight from a business department and turning them into process design, estimates and
            documentation that developers can actually build from.
          </p>
          <p class="lg-prose">
            Since 2025, AI-assisted engineering has been a standing part of how I deliver rather
            than an experiment: GitHub Copilot and Claude in the daily development loop, agentic
            workflows I built myself for the work that repeats, and getting a project team
            productive with both.
          </p>
          <p class="lg-prose">
            I have carried technical responsibility for small teams on both the consultancy and the
            client side, and mentored junior developers in both. What I am looking for now is a tech
            lead role with end-to-end ownership of a system and of the team around it, somewhere
            engineering decisions are made close to the business.
          </p>
          <p class="lg-prose">
            I was born in Rome and moved to Munich in 2008. Italian is my first language; I work in
            German and English every day.
          </p>
        </div>
      </section>

      <section class="section">
        <h2 class="section-head lg-heading">What I work with</h2>
        <div class="skills">
          <div v-for="group in skills" :key="group.name" class="skill-group">
            <h3 class="skill-name lg-eyebrow">{{ group.name }}</h3>
            <p class="skill-items">{{ group.items }}</p>
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
import PersonalInfo from '@/components/CV/PersonalInfo'
import Languages from '@/components/CV/Languages'
import portrait from '@/assets/foto.jpg'

export default {
  name: 'aboutMe',
  components: { Languages, PersonalInfo },
  data () {
    return {
      portrait,
      skills: [
        {
          name: 'Backend / JVM',
          items: 'Java · Spring · Spring Boot · Jakarta EE · Hibernate · WildFly · Payara'
        },
        {
          name: 'AI-assisted engineering',
          items: 'GitHub Copilot · Claude · agentic development workflows · prompt patterns · ' +
            'tool-assisted refactoring and test generation · enabling a team to work this way'
        },
        {
          name: 'Frontend',
          items: 'TypeScript · React · Angular · RxJS · Vue.js · Pinia · JSF 2.0 · Vaadin · Material UI'
        },
        {
          name: 'Node / APIs',
          items: 'NestJS · Express · Prisma · REST · backend-for-frontend architectures'
        },
        {
          name: 'Cloud & IaC',
          items: 'AWS (Lambda, DynamoDB, SNS, SQS, EKS, Fargate, Cognito, VPC, Route 53, ' +
            'CodePipeline, CodeArtifact) · Terraform · AWS CDK · Azure AD / SAML'
        },
        {
          name: 'Containers & ops',
          items: 'Docker · Kubernetes · Linux · nginx'
        },
        {
          name: 'CI/CD',
          items: 'GitLab CI · Jenkins · Bamboo · Concourse CI · GitHub'
        },
        {
          name: 'Data & messaging',
          items: 'PostgreSQL · Oracle · MS SQL Server · DynamoDB · Kafka · SQL'
        },
        {
          name: 'Testing & quality',
          items: 'JUnit · Jest · Cypress · Supertest · Cucumber · Selenium · SonarQube · K6'
        },
        {
          name: 'Ways of working',
          items: 'Technical responsibility for teams of 2–3 · mentoring juniors · requirements ' +
            'workshops · effort estimation · release planning · Scrum · DevSecOps'
        }
      ]
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
