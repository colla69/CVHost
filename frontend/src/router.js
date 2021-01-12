import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import cv from '@/components/CV/cv'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/', component: HelloWorld },
    { path: '/cv', component: cv },

    // otherwise redirect to home
    { path: '*', redirect: '/' }
  ]
})

export default router
