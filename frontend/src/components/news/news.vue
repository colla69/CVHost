<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ t.eyebrow(news.length) }}</p>
        <h1 class="lg-heading page-title">{{ t.heading }}</h1>
        <p class="lg-prose page-intro">{{ t.intro }}</p>
      </header>

      <div class="feed">
        <article v-for="item in news" :key="item.id" class="note">
          <v-img
            :src="item.img_link"
            :alt="$tr(item.title)"
            :aspect-ratio="16 / 9"
            cover
            class="note-img"
          ></v-img>

          <div class="note-body">
            <p class="note-date lg-tnum">{{ formatDate(item.release_date) }}</p>
            <h2 class="note-title">{{ $tr(item.title) }}</h2>
            <!-- Hand-authored copy from news.json; never external input. -->
            <div class="note-text lg-prose" v-html="$tr(item.description_text)"></div>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<script>
import { pick } from '@/i18n'
import data from './news.json'

// title and description_text are { en, de, it } maps; render them through $tr.

// Copy before reversing: reversing the import in place mutated the array for
// every other component that imports news.json.
const newestFirst = data.slice().reverse()

const COPY = {
  en: {
    eyebrow: n => n + ' entries · since 2018',
    heading: 'Notes',
    intro: 'A running log of what I have been learning and building, kept since 2018 — side ' +
      'projects, home infrastructure and the occasional milestone.'
  },
  de: {
    eyebrow: n => n + ' Einträge · seit 2018',
    heading: 'Notizen',
    intro: 'Ein fortlaufendes Logbuch dessen, was ich gelernt und gebaut habe, geführt seit 2018 — ' +
      'Nebenprojekte, die Infrastruktur zu Hause und ab und zu ein Meilenstein.'
  },
  it: {
    eyebrow: n => n + ' note · dal 2018',
    heading: 'Note',
    intro: 'Un diario continuo di ciò che ho imparato e costruito, tenuto dal 2018 — progetti ' +
      'personali, l’infrastruttura di casa e ogni tanto un traguardo.'
  }
}

export default {
  name: 'news',
  data () {
    return {
      news: newestFirst
    }
  },
  computed: {
    t () {
      return pick(COPY)
    }
  },
  methods: {
    formatDate (value) {
      return new Date(value).toLocaleDateString(this.$lang.language.dates, {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
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

.feed {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem 1.75rem;
}

@media (min-width: 900px) {
  .feed {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

.note {
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--lg-rule);
  padding-top: 1.25rem;
}

.note-img {
  border: 1px solid var(--lg-rule);
  border-radius: 3px;
  background: var(--lg-sunk);
}

.note-body {
  padding-top: 1rem;
}

.note-date {
  font-family: var(--lg-mono);
  font-size: 0.6875rem;
  letter-spacing: 0.11em;
  text-transform: uppercase;
  color: var(--lg-faint);
  margin: 0 0 0.375rem;
}

.note-title {
  font-family: var(--lg-display);
  font-weight: 700;
  font-size: clamp(1.125rem, 2.4vw, 1.375rem);
  letter-spacing: -0.02em;
  line-height: 1.25;
  color: var(--lg-ink);
  margin: 0 0 0.75rem;
  text-wrap: balance;
}

.note-text {
  font-size: 0.9375rem;
}

.note-text :deep(a) {
  color: var(--lg-accent);
  text-decoration: none;
  border-bottom: 1px solid currentColor;
}

.note-text :deep(a:hover) {
  opacity: 0.75;
}
</style>
