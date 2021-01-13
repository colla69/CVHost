import Vue from 'vue'
import App from './App.vue'
import router from '@/router'
import vuetify from '@/plugins/vuetify'
import VueDarkMode from '@growthbunker/vuedarkmode'
import './app.css'

import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueDarkMode)
Vue.use(VueAxios, axios)
Vue.config.productionTip = false

new Vue({
  router: router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
