// The site's pages in menu order, as the app bar, the drawer and the footer
// list them. Each label is written here once and read with $tr, so a link that
// names its page says the same thing everywhere and is translated only once.
export const PAGES = [
  { to: '/', label: { en: 'Home', de: 'Start', it: 'Home' }, icon: 'mdi-home-outline' },
  { to: '/projectInfos', label: { en: 'Work', de: 'Projekte', it: 'Progetti' }, icon: 'mdi-briefcase-outline', footer: true },
  { to: '/experience', label: { en: 'Experience', de: 'Werdegang', it: 'Esperienza' }, icon: 'mdi-timeline-outline', footer: true },
  { to: '/qualifications', label: { en: 'Certificates', de: 'Zertifikate', it: 'Certificati' }, icon: 'mdi-certificate-outline', footer: true },
  { to: '/news', label: { en: 'Notes', de: 'Notizen', it: 'Note' }, icon: 'mdi-note-text-outline', footer: true },
  { to: '/aboutMe', label: { en: 'About', de: 'Über mich', it: 'Chi sono' }, icon: 'mdi-account-outline', footer: true },
  { to: '/contact', label: { en: 'Contact', de: 'Kontakt', it: 'Contatti' }, icon: 'mdi-email-outline' }
]

// The { en, de, it } label of the page at `to`, for links outside the menus.
export function pageLabel (to) {
  const page = PAGES.find(page => page.to === to)
  if (!page) throw new Error('nav.js: no page at ' + to)
  return page.label
}
