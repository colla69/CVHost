<template>
  <v-expansion-panel>
    <v-expansion-panel-title>{{ title }}</v-expansion-panel-title>
    <v-expansion-panel-text>
      <v-list-item
        v-for="item in data"
        v-bind:key="item.name"
      >
        <v-list-item-subtitle>{{ item.name }}</v-list-item-subtitle>
        <v-list-item-title>{{ item.value }}</v-list-item-title>
      </v-list-item>
    </v-expansion-panel-text>
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
