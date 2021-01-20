<template>
  <div>
    <v-container>
      <img src="../../assets/foto.png" >
    </v-container>
    <v-container id="split" >
      <PersonalInfo></PersonalInfo>
      <Languages></Languages>
    </v-container>
  </div>
</template>

<script>
import PersonalInfo from '@/components/CV/PersonalInfo'
import Languages from '@/components/CV/Languages'

export default {
  name: 'aboutMe',
  components: { Languages, PersonalInfo },
  data () {
    return {
      panel: [0, 1]
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
</style>
