<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ info.length }} projects &middot; 2011 to today</p>
        <h1 class="lg-heading page-title">Work</h1>
        <p class="lg-prose page-intro">
          Every project I have delivered, newest first &mdash; from a Delphi chess game written as an
          apprentice to regulatory platforms and AWS estates for banks and manufacturers.
        </p>
      </header>

      <div class="grid">
        <article v-for="item in info" :key="item.id" class="card">
          <v-img
            :src="item.image"
            :alt="item.Name"
            :aspect-ratio="16 / 10"
            cover
            class="card-img"
          ></v-img>

          <div class="card-body">
            <p class="card-client">{{ item.client || item.company_name }}</p>
            <h2 class="card-title">{{ item.Name }}</h2>

            <dl class="card-meta">
              <div>
                <dt>Role</dt>
                <dd>{{ item.role_name }}</dd>
              </div>
              <div>
                <dt>Dates</dt>
                <dd class="lg-tnum">{{ span(item) }}</dd>
              </div>
            </dl>

            <p class="card-stack">{{ item.lang }}</p>

            <v-expansion-panels flat class="card-panels">
              <v-expansion-panel elevation="0">
                <v-expansion-panel-title class="card-toggle">What I did</v-expansion-panel-title>
                <v-expansion-panel-text>
                  <!-- Hand-authored copy from project_infos.json; never external input. -->
                  <div class="card-desc lg-prose" v-html="item.description"></div>
                  <a
                    v-if="item.company_link"
                    :href="item.company_link"
                    target="_blank"
                    rel="noopener noreferrer"
                    class="card-link"
                  >
                    {{ item.company_name }}
                    <v-icon icon="mdi-open-in-new" size="14"></v-icon>
                  </a>
                </v-expansion-panel-text>
              </v-expansion-panel>
            </v-expansion-panels>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<script>
import data from './project_infos.json'

// Copy before reversing: the JSON import is a module-level array shared with
// every other component that imports it.
const newestFirst = data.slice().reverse()

export default {
  name: 'projectInfos',
  data () {
    return {
      info: newestFirst
    }
  },
  methods: {
    span (item) {
      const start = new Date(item.start_date).getFullYear()
      if (!item.end_date) return start + ' — today'
      const end = new Date(item.end_date).getFullYear()
      return start === end ? String(start) : start + ' — ' + end
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

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(min(100%, 20rem), 1fr));
  gap: 1.75rem 1.5rem;
}

.card {
  display: flex;
  flex-direction: column;
  background: var(--lg-surface);
  border: 1px solid var(--lg-rule);
  border-radius: 4px;
  overflow: hidden;
}

.card-img {
  background: var(--lg-sunk);
  border-bottom: 1px solid var(--lg-rule);
}

.card-body {
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 1.125rem 1.25rem 0.5rem;
}

.card-client {
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.11em;
  text-transform: uppercase;
  color: var(--lg-accent);
  margin: 0 0 0.5rem;
}

.card-title {
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: 1.125rem;
  letter-spacing: -0.02em;
  line-height: 1.25;
  color: var(--lg-ink);
  margin: 0 0 0.875rem;
  text-wrap: balance;
}

.card-meta {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 0.5rem 1rem;
  margin: 0 0 0.75rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid var(--lg-rule);
}

.card-meta dt {
  font-family: var(--lg-mono);
  font-size: 0.5625rem;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--lg-faint);
  margin-bottom: 0.125rem;
}

.card-meta dd {
  margin: 0;
  font-size: 0.8125rem;
  color: var(--lg-muted);
}

.card-meta div:last-child {
  text-align: right;
}

.card-stack {
  font-family: var(--lg-mono);
  font-size: 0.75rem;
  line-height: 1.55;
  color: var(--lg-faint);
  margin: 0;
  flex: 1;
}

.card-panels {
  margin: 0.75rem -1.25rem 0;
  background: transparent;
}

.card-toggle {
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  color: var(--lg-accent);
  min-height: 44px;
}

.card-desc {
  font-size: 0.875rem;
}

.card-desc :deep(p) {
  margin: 0 0 0.75rem;
}

.card-desc :deep(p:last-child) {
  margin-bottom: 0;
}

.card-link {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  margin-top: 0.75rem;
  font-size: 0.8125rem;
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}
</style>
