import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import aboutMe from '@/components/CV/cv'
import experience from '@/components/CV/WorkExperience'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/', component: HelloWorld },
    { path: '/aboutMe', component: aboutMe },
    { path: '/experience', component: experience },

    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
})

export default router
