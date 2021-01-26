<template>
  <v-expansion-panel
    v-model="panel"
  >
    <v-expansion-panel-header>{{ title }}</v-expansion-panel-header>
    <v-expansion-panel-content>
      <v-list-item

        v-for="item in data"
        v-bind:key="item.name"
      >
        <v-list-item-content>
          <v-list-item-subtitle>{{ item.name }}</v-list-item-subtitle>
          <v-list-item-title>{{ item.value }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-expansion-panel-content>
  </v-expansion-panel>

</template>

<script>
export default {
  name: 'infoList',
  props: ['title', 'data']
}
</script>

<style scoped>

</style>
