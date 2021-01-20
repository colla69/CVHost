<template>
  <v-container fluid style="display: flex; width: 100%">
    <div style="width: 800px; height: 100%">
      <h1>Welcome to my Homepage! :)</h1>
      I'm a developer
      <p>My name is Andrea Colarieti Tosti and am glad to welcome you to my Guest portal!
        This page only has one function: it allows you to dowload my CV and enables me to demonstrate
        a little bit of my Programming and Administration skills.</p>
      <h3>
        <a href="">
          Read about my fullfilled projects
        </a><br><br>
      </h3>
      <h3>  <a href="https://cv.colarietitosti.info/#!CV">
          Read my CV
        </a><br/><br/>
      </h3>
      <h3>
        <a href="https://cv.colarietitosti.info/files/CV_Docs.zip">
          Download my CV
        </a>
      </h3>
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
