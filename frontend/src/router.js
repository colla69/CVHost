import { watch } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { lang, tr } from '@/i18n'
import { pageLabel } from '@/nav'
import Home from '@/components/Home'
import aboutMe from '@/components/CV/AboutMe'
import projectInfos from '@/components/projectInfos/projectInfos'
import news from '@/components/news/news'
import experienceAndEducation from '@/components/CV/ExperienceAndEducation'
import contactForm from '@/components/Contact/ContactForm'
import qualifications from '@/components/CV/Qualifications'

const SITE = 'Andrea Colarieti Tosti'

const router = createRouter({
  history: createWebHistory(),
  // Land at the top of a new page; keep the position when going back.
  scrollBehavior (to, from, savedPosition) {
    return savedPosition || { top: 0 }
  },
  // A page's tab title is its menu label, read from nav.js, except where the
  // two deliberately differ (the home page and the contact page).
  routes: [
    { path: '/', component: Home, meta: { title: { en: 'Senior IT Consultant & Tech Lead', de: 'Senior IT Consultant & Tech Lead', it: 'Senior IT Consultant & Tech Lead' } } },
    { path: '/aboutMe', component: aboutMe, meta: { title: pageLabel('/aboutMe') } },
    { path: '/experience', component: experienceAndEducation, meta: { title: pageLabel('/experience') } },
    { path: '/projectInfos', component: projectInfos, meta: { title: pageLabel('/projectInfos') } },
    { path: '/news', component: news, meta: { title: pageLabel('/news') } },
    { path: '/contact', component: contactForm, meta: { title: { en: 'Get in touch', de: 'Kontakt aufnehmen', it: 'Mi contatti' } } },
    { path: '/qualifications', component: qualifications, meta: { title: pageLabel('/qualifications') } },

    // otherwise redirect to home
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

function applyTitle (route) {
  const title = tr(route.meta.title)
  document.title = title ? title + ' — ' + SITE : SITE
}

router.afterEach(to => applyTitle(to))

// Switching language retitles the open tab, not just the next navigation.
watch(() => lang.code, () => applyTitle(router.currentRoute.value))

export default router
