<template>
  <v-expansion-panel>
    <info-list
      title="PERSONAL INFORMATION"
      v-bind:data="infos"
    >
    </info-list>
  </v-expansion-panel>
</template>

<script>
import InfoList from '@/components/CV/infoList'

export default {
  name: 'PersonalInfo',
  components: { InfoList },
  data () {
    return {
      infos: [
        {
          name: 'Surname:',
          value: 'Colarieti Tosti'
        },
        {
          name: 'Name:',
          value: 'Andrea'
        },
        {
          name: 'Address:',
          value: 'Lungstr. 2, 81549 München'
        },
        {
          name: 'Cellphone:',
          value: '+49 157 84 89 14 09'
        },
        {
          name: 'E-Mail:',
          value: 'a.colarietitosti@googlemail.com'
        },
        {
          name: 'Birthday:',
          value: '14.08.1991'
        },
        {
          name: 'Birthplace:',
          value: 'Rome (Italy)'
        },
        {
          name: 'Marital status:',
          value: 'single'
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
