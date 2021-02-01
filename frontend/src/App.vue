<template>
  <v-app id="app">
    <v-main>
      <v-container fluid>
        <Menu></Menu>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>

import Menu from '@/components/Menu'

export default {
  name: 'App',
  components: {
    Menu
  },
  mounted: function () {
    debugger
    console.log('access!!')
  }
}
</script>

<style>
</style>
