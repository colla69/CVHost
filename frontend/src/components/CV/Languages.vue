<template>
  <v-expansion-panel readonly>
    <v-expansion-panel-header expand-icon="">
      Languages
    </v-expansion-panel-header>
    <v-expansion-panel-content>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>Italian:</v-list-item-title>
          <v-list-item-subtitle>first language</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>English:</v-list-item-title>
          <v-list-item-subtitle>fluently spoken and written</v-list-item-subtitle>
          <v-list-item-subtitle>2005 - B1 at the British Council in Rome</v-list-item-subtitle>
          <v-list-item-subtitle>2004 - A2 at the British Council in Rome</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title>German: </v-list-item-title>
          <v-list-item-subtitle>fluently spoken and written</v-list-item-subtitle>
          <v-list-item-subtitle>2010 - TestDaF at Goethe Institute in Munich</v-list-item-subtitle>
          <v-list-item-subtitle>2004 - B1 at Goethe Institute in Rome</v-list-item-subtitle>
          <v-list-item-subtitle>2003 - A2 at Goethe Institute in Rome</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-expansion-panel-content>
  </v-expansion-panel>
</template>

<script>

export default {
  name: 'Languages',
  data () {
    return {
      infos: [
        {
          name: 'Italian:',
          value: 'first language'
        },
        {
          name: 'English:',
          value: 'fluently spoken and written,\n' +
            '2005 - B1 at the British Council in Rome\n' +
            '2004 - A2 at the British Council in Rome'
        },
        {
          name: 'german:',
          value: 'fluently spoken and written,\n' +
            '2010 - TestDaF at Goethe Institute in Munich\n' +
            'Reading skills: 5/5\n' +
            'Listening skills: 4/5\n' +
            'Writing skills: 4/5\n' +
            'Speaking skills: 5/5\n' +
            '2004 - B1 at Goethe Institute in Rome\n' +
            '2003 - A2 at Goethe Institute in Rome'
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
.v-list-item__content{
  text-align: left;
}
</style>
