<template>
  <v-container fluid style="display: flex; width: 100%">
    <div style="width: 800px; height: 100%">
      <h1>Welcome to my Homepage! :)</h1>
      I'm a developer
    </div>
    <v-divider vertical></v-divider>
    <div style="width: 70em">
      <h3>Read My News:</h3>
      <news></news>
    </div>
  </v-container>
</template>

<script>
import news from '@/components/news/news'

export default {
  name: 'Home',
  components: {
    news
  },
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
