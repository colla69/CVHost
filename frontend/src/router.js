import { watch } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { lang, tr } from '@/i18n'
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
  routes: [
    { path: '/', component: Home, meta: { title: { en: 'Senior IT Consultant & Tech Lead' } } },
    { path: '/aboutMe', component: aboutMe, meta: { title: { en: 'About' } } },
    { path: '/experience', component: experienceAndEducation, meta: { title: { en: 'Experience' } } },
    { path: '/projectInfos', component: projectInfos, meta: { title: { en: 'Work' } } },
    { path: '/news', component: news, meta: { title: { en: 'Notes' } } },
    { path: '/contact', component: contactForm, meta: { title: { en: 'Get in touch' } } },
    { path: '/qualifications', component: qualifications, meta: { title: { en: 'Certificates' } } },

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
