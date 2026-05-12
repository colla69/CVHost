import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/components/Home'
import aboutMe from '@/components/CV/AboutMe'
import projectInfos from '@/components/projectInfos/projectInfos'
import news from '@/components/news/news'
import experienceAndEducation from '@/components/CV/ExperienceAndEducation'
import contactForm from '@/components/Contact/ContactForm'
import qualifications from '@/components/CV/Qualifications'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/aboutMe', component: aboutMe },
    { path: '/experience', component: experienceAndEducation },
    { path: '/projectInfos', component: projectInfos },
    { path: '/news', component: news },
    { path: '/contact', component: contactForm },
    { path: '/qualifications', component: qualifications },

    // otherwise redirect to home
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

export default router
