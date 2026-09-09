<template>
  <section class="card">
    <h2 class="card-head lg-eyebrow">Details</h2>
    <dl class="rows">
      <div v-for="item in infos" :key="item.name" class="row">
        <dt>{{ item.name }}</dt>
        <dd>
          <a v-if="item.href" :href="item.href" :target="item.external ? '_blank' : null"
             :rel="item.external ? 'noopener noreferrer' : null">{{ item.value }}</a>
          <span v-else>{{ item.value }}</span>
        </dd>
      </div>
    </dl>
    <p class="card-note">
      Address, phone number and date of birth are in the
      <a href="/data/CV_en.pdf" download>CV download</a> rather than on this public page.
    </p>
  </section>
</template>

<script>
export default {
  name: 'PersonalInfo',
  data () {
    return {
      infos: [
        {
          name: 'Based in',
          value: 'München, Germany'
        },
        {
          name: 'Email',
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
          name: 'Open to',
          value: 'Tech lead and team lead roles'
        }
      ]
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
