<template>
  <v-card>
  <img
    src="../../assets/foto.png"
    style="height: 200px"
  >
  <v-expansion-panels
    v-model="panel"
    multiple
  >
    <div id="split">
      <PersonalInfo></PersonalInfo>
      <Languages></Languages>
    </div>
  </v-expansion-panels>
  </v-card>
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
img{
  margin-top: 5px;
}
#split{
  display: grid;
  margin-top: 5px;
  grid-template-columns: auto auto ;
  width: 100%;
}
</style>
