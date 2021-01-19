import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import aboutMe from '@/components/CV/AboutMe'
// import experience from '@/components/CV/WorkExperience'
// import education from '@/components/CV/Education'
import projectInfos from '@/components/projectInfos/projectInfos'
import news from '@/components/news/news'
import experienceAndEducation from '@/components/CV/ExperienceAndEducation'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/', component: Home },
    { path: '/aboutMe', component: aboutMe },
    // { path: '/experience', component: experience },
    // { path: '/education', component: education },
    { path: '/experience', component: experienceAndEducation },
    { path: '/projectInfos', component: projectInfos },
    { path: '/news', component: news },

    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
})

export default router
