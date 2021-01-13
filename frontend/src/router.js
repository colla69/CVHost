import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import aboutMe from '@/components/CV/AboutMe'
import experience from '@/components/CV/WorkExperience'
import education from '@/components/CV/Education'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/', component: Home },
    { path: '/aboutMe', component: aboutMe },
    { path: '/experience', component: experience },
    { path: '/education', component: education },

    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
})

export default router
