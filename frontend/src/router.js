import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import aboutMe from '@/components/CV/AboutMe'
import projectInfos from '@/components/projectInfos/projectInfos'
import news from '@/components/news/news'
import experienceAndEducation from '@/components/CV/ExperienceAndEducation'
import contactForm from '@/components/Contact/ContactForm'
import qualifications from '@/components/CV/Qualifications'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/', component: Home },
    { path: '/aboutMe', component: aboutMe },
    { path: '/experience', component: experienceAndEducation },
    { path: '/projectInfos', component: projectInfos },
    { path: '/news', component: news },
    { path: '/contact', component: contactForm },
    { path: '/qualifications', component: qualifications },

    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
})

export default router
