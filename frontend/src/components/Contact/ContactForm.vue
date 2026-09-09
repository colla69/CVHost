<template>
  <div class="lg-page">
    <div class="lg-inner">
      <header class="page-head">
        <p class="lg-eyebrow">Open to tech lead and team lead roles</p>
        <h1 class="lg-heading page-title">Get in touch</h1>
        <p class="lg-prose page-intro">
          Email is the quickest way to reach me, and I answer every message that is not a mailshot.
          If you are hiring, the CV below has the detail this site summarises.
        </p>
      </header>

      <section class="channels">
        <a
          v-for="channel in channels"
          :key="channel.label"
          :href="channel.href"
          :target="channel.external ? '_blank' : null"
          :rel="channel.external ? 'noopener noreferrer' : null"
          class="channel"
        >
          <v-icon :icon="channel.icon" size="20" class="channel-icon"></v-icon>
          <span class="channel-label lg-eyebrow">{{ channel.label }}</span>
          <span class="channel-value">{{ channel.value }}</span>
        </a>
      </section>

      <section class="downloads">
        <h2 class="section-head lg-heading">Curriculum vitae</h2>
        <p class="lg-prose downloads-intro">
          The same document in three languages. It carries the contact details kept off this public
          page — address, phone number and date of birth.
        </p>
        <div class="download-row">
          <v-btn
            v-for="file in files"
            :key="file.href"
            :href="file.href"
            download
            variant="outlined"
            size="large"
            class="download"
            prepend-icon="mdi-file-download-outline"
          >
            {{ file.label }}
          </v-btn>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Contact',
  data () {
    return {
      channels: [
        {
          label: 'Email',
          value: 'a.colarietitosti@googlemail.com',
          href: 'mailto:a.colarietitosti@googlemail.com',
          icon: 'mdi-email-outline'
        },
        {
          label: 'LinkedIn',
          value: 'andrea-colarieti',
          href: 'https://www.linkedin.com/in/andrea-colarieti-662032197/',
          icon: 'mdi-linkedin',
          external: true
        },
        {
          label: 'GitHub',
          value: 'github.com/colla69',
          href: 'https://github.com/colla69',
          icon: 'mdi-github',
          external: true
        }
      ],
      files: [
        { label: 'English', href: '/data/CV_en.pdf' },
        { label: 'Deutsch', href: '/data/Lebenslauf.pdf' },
        { label: 'Italiano', href: '/data/CV_it.pdf' }
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
  margin: 0.375rem 0 0.75rem;
}

.page-intro {
  margin: 0;
  font-size: 1.0625rem;
}

.channels {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(100%, 15rem), 1fr));
  gap: 1px;
  background: var(--lg-rule);
  border: 1px solid var(--lg-rule);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: var(--lg-band);
}

.channel {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 1.25rem;
  background: var(--lg-surface);
  text-decoration: none;
  transition: background 0.15s ease;
}

.channel:hover {
  background: var(--lg-accent-soft);
}

.channel-icon {
  color: var(--lg-accent);
  margin-bottom: 0.375rem;
}

.channel-value {
  color: var(--lg-ink);
  font-size: 0.9375rem;
  overflow-wrap: anywhere;
}

.section-head {
  font-size: clamp(1.375rem, 3.4vw, 1.875rem);
  border-top: 2px solid var(--lg-ink);
  padding-top: 0.875rem;
  margin: 0 0 0.75rem;
}

.downloads-intro {
  margin: 0 0 1.25rem;
  font-size: 0.9375rem;
}

.download-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.download {
  border-color: var(--lg-rule-strong);
  color: var(--lg-ink);
}
</style>
