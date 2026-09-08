<template>
  <v-card id="pos" flat>
      <v-card-title>
        Personal Info
      </v-card-title>
      <v-card-text>
        <v-list-item
          v-for="item in infos"
          v-bind:key="item.name"
        >
          <v-list-item-subtitle>{{ item.name }}</v-list-item-subtitle>
          <v-list-item-title>{{ item.value }}</v-list-item-title>
        </v-list-item>
      </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'PersonalInfo',
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
