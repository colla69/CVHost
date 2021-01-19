<template>
  <v-container>
    <work-experience></work-experience>
    <education></education>
  </v-container>
</template>

<script>

import WorkExperience from '@/components/CV/WorkExperience'
import Education from '@/components/CV/Education'

export default {
  name: 'ExperienceAndEducation',
  components: { Education, WorkExperience }
}
</script>

<style scoped>

</style>
