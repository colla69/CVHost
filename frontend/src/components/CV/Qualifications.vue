<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">{{ qualifications.length }} documents</p>
        <h1 class="lg-heading page-title">Certificates</h1>
        <p class="lg-prose page-intro">
          Diplomas, employment references, exam results and language certificates, as issued. Every
          one opens as a PDF.
        </p>
      </header>

      <section v-for="group in groups" :key="group.name" class="group">
        <h2 class="group-head lg-eyebrow">{{ group.name }}</h2>

        <div class="grid">
          <article v-for="item in group.items" :key="item.filename" class="cert">
            <button type="button" class="cert-sheet" @click="open(item)">
              <img :src="thumb(item)" :alt="'First page of ' + item.name" loading="lazy">
              <span class="cert-open">
                <v-icon icon="mdi-magnify-plus-outline" size="16"></v-icon>
                View
              </span>
            </button>

            <div class="cert-meta">
              <h3 class="cert-name">{{ item.name }}</h3>
              <p class="cert-issuer">{{ item.issuer }}</p>
              <p class="cert-year lg-tnum">{{ item.year }}</p>
            </div>
          </article>
        </div>
      </section>

      <p class="lg-prose note">
        Prefer everything in one file? The
        <a href="/data/CV_Docs.zip" download>complete document set</a> is a single download.
      </p>
    </div>

    <!-- Desktop gets an inline preview; phones open the file directly, because
         iOS Safari will not render a PDF inside an iframe at all. -->
    <v-dialog v-model="dialog" max-width="900">
      <v-card v-if="active" class="viewer">
        <v-card-title class="viewer-head">
          <span>{{ active.name }}</span>
          <v-btn
            icon="mdi-close"
            variant="text"
            size="small"
            aria-label="Close"
            @click="dialog = false"
          ></v-btn>
        </v-card-title>
        <iframe :src="source(active)" :title="active.name" class="viewer-frame"></iframe>
        <v-card-actions>
          <v-btn :href="source(active)" target="_blank" rel="noopener noreferrer" variant="text">
            Open in a new tab
          </v-btn>
          <v-btn :href="source(active)" download variant="text">Download</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: 'qualifications',
  data: function () {
    return {
      dialog: false,
      active: null,
      qualifications: [
        {
          name: 'AWS Certified Solutions Architect — Associate',
          issuer: 'Amazon Web Services · recertification · score 755/1000',
          year: '2026',
          group: 'Professional',
          filename: 'AWS_SAA_2026.pdf'
        },
        {
          name: 'AWS Certified Solutions Architect — Associate',
          issuer: 'Amazon Web Services · first certification · score 736/1000',
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
          name: 'Employment reference',
          issuer: '3Points Software GmbH',
          year: '2018',
          group: 'Professional',
          filename: 'ArbeitsZeugnis.pdf'
        },
        {
          name: 'Fachinformatiker Anwendungsentwicklung',
          issuer: 'IHK München · final grade 71/100',
          year: '2013',
          group: 'Professional',
          filename: 'AUSB_IHK_Zeugnis.pdf'
        },
        {
          name: 'Apprenticeship reference',
          issuer: '3Points Software GmbH',
          year: '2013',
          group: 'Professional',
          filename: 'AUSB_3P_Zeugnis.pdf'
        },
        {
          name: 'Diploma di Liceo Scientifico',
          issuer: 'Liceo Scientifico “Voltaire” · final grade 70/100',
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
          name: 'German B1 — Zertifikat Deutsch',
          issuer: 'Goethe-Institut Rome',
          year: '2004',
          group: 'Languages',
          filename: 'ZD.pdf'
        },
        {
          name: 'German A2 — Fit in Deutsch 2',
          issuer: 'Goethe-Institut Rome',
          year: '2003',
          group: 'Languages',
          filename: 'FID2.pdf'
        },
        {
          name: 'English B1 — PET',
          issuer: 'British Council Rome',
          year: '2005',
          group: 'Languages',
          filename: 'PET.pdf'
        },
        {
          name: 'English A2 — KET',
          issuer: 'British Council Rome',
          year: '2004',
          group: 'Languages',
          filename: 'KET.pdf'
        }
      ]
    }
  },
  computed: {
    groups () {
      const order = ['Professional', 'Education', 'Languages']
      return order.map(name => ({
        name,
        items: this.qualifications.filter(item => item.group === name)
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

.note a {
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
