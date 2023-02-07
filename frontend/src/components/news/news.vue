<template>
  <v-container>
    <v-card id="project"
            v-for="item in news"
            v-bind:key="item.id"
            raised
    >
      <v-img :src="item.img_link"
             height="100"
             contain
             style="margin:5px"
      ></v-img>
      <v-card-title>{{item.title}}</v-card-title>
      <v-card-subtitle style="-webkit-text-fill-color:gray">{{item.release_date}}</v-card-subtitle>
      <v-card-text>
        <span v-html="item.description_text">
        </span>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import data from './news.json'

const sortedData = data.reverse()

export default {
  name: 'news',
  data () {
    return {
      news: sortedData
    }
  }
}
</script>

<style scoped>
  #project{
    width: 80%;
  }
</style>
