<template>
  <v-container fluid>

    <div id="split2">
      <v-container >
        <img
          src="../../assets/foto.png"
        >
      </v-container>
      <v-container >
        <v-expansion-panels
          v-model="panel"
          multiple
        >
        <v-expansion-panel flat >
          <v-expansion-panel-header>About Me</v-expansion-panel-header>
          <v-expansion-panel-content>
            I was born in Rome in August '91, I can remember how hot it was and the deep feeling that something was off.... my computer was missing!
            Since then I nurtured my passion for computers and electronics in any way I could think of.<br>
            As a kid I wrote an interactive tourist guide about places in Rome as I was learning about them in school, then my glorious career continued Through very small private projects hand tuning configuration files and simple administration of my home Network.
            After that, I stopped programming as my "gaming phase" kicked-in, and I started programming again after high school with my first trainee job.
            With the first projects I learned about loops and data structures and everything started making sense.
            I was ready to take on my first project! Writing a chess game, mostly to understand how to use all the newly learned notions.
            After a lot of failed tries it was finally working! I will never forget that feeling... I am deeply thankful to be able to continue to pursue that feeling in my day by day, working as a programmer.
            The story continues with many productive projects, the biggest ones being part of the mediafusion project.
            Soon enough my need to learn more lead me to search for new challenges, and motivated me to continue my studies at the Ludwig-Maximilian-Universität in Munich.
            Did I poke your interest?
          </v-expansion-panel-content>
        </v-expansion-panel>
        </v-expansion-panels>
      </v-container>
    </div>
    <v-expansion-panels
      v-model="panel"
      multiple
    >
      <v-container fluid id="split">
        <PersonalInfo></PersonalInfo>
        <Languages></Languages>
      </v-container>
    </v-expansion-panels>

  </v-container>
</template>

<script>
import PersonalInfo from '@/components/CV/PersonalInfo'
import Languages from '@/components/CV/Languages'

export default {
  name: 'aboutMe',
  components: { Languages, PersonalInfo },
  data () {
    return {
      panel: [0, 1, 2, 3]
    }
  }
}
</script>

<style scoped>
img {
  height: 300px
}

#split {
  display: grid;
  grid-template-columns: auto auto;
  width: 100%;
}

#split2 {
  display: grid;
  grid-template-columns: 100px auto;
  width: 100%;
}

</style>
