import { createRouter, createWebHistory } from 'vue-router'
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
    { path: '/', component: Home, meta: { title: 'Senior IT Consultant & Tech Lead' } },
    { path: '/aboutMe', component: aboutMe, meta: { title: 'About' } },
    { path: '/experience', component: experienceAndEducation, meta: { title: 'Experience' } },
    { path: '/projectInfos', component: projectInfos, meta: { title: 'Work' } },
    { path: '/news', component: news, meta: { title: 'Notes' } },
    { path: '/contact', component: contactForm, meta: { title: 'Get in touch' } },
    { path: '/qualifications', component: qualifications, meta: { title: 'Certificates' } },

    // otherwise redirect to home
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

router.afterEach(to => {
  document.title = to.meta.title ? to.meta.title + ' — ' + SITE : SITE
})

export default router
