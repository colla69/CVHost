<template>
  <v-container fluid style="display: flex; width: 100%">
    <div style="width: 800px; height: 100%">
      <h1>Welcome to my Homepage! :)</h1>
      Senior IT Consultant &middot; Tech Lead
      <p>My name is Andrea Colarieti Tosti and I am glad to welcome you to my Guest portal!
        I have been building software for 13 years, the last five as a consultant for Porsche,
        BMW Financial Services, Volkswagen Financial Services, Krones and a European asset manager
        &mdash; from Java and Jakarta EE backends to React and Angular front ends, with AWS, Terraform
        and CI/CD running through all of it.</p>
      <p>This page lets you download my CV, read through the projects I have worked on, and see a little
        of my programming and administration skills along the way.</p>
<!--      <h3>-->
<!--        <a href="">-->
<!--          Read about my fullfilled projects-->
<!--        </a><br><br>-->
<!--      </h3>-->
<!--      <h3>  <a href="https://cv.colarietitosti.info/#!CV">-->
<!--          Read my CV-->
<!--        </a><br/><br/>-->
<!--      </h3>-->
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
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
