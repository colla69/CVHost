<template>
  <v-app>
    <v-content>
      <v-container fluid>
        <Menu></Menu>
        <router-view></router-view>
      </v-container>
    </v-content>
  </v-app>
</template>

<script>

import Menu from '@/components/Menu'

export default {
  name: 'App',
  components: {
    Menu
  }
}
</script>

<style>
</style>
