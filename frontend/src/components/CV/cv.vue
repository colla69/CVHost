<template>
  <v-expansion-panels
    v-model="panel"
    multiple
  >
  <PersonalInfo></PersonalInfo>
    <v-expansion-panel>
      <v-expansion-panel-header>Panel 2</v-expansion-panel-header>
      <v-expansion-panel-content>
        Some content
      </v-expansion-panel-content>
    </v-expansion-panel>

    <v-expansion-panel>
      <v-expansion-panel-header>Panel 3</v-expansion-panel-header>
      <v-expansion-panel-content>
        Some content
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
import PersonalInfo from '@/components/CV/PersonalInfo'

export default {
  name: 'cv',
  components: { PersonalInfo }
}
</script>

<style scoped>

</style>
