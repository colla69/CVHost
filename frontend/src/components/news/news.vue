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
      <v-card-text>
        <span v-html="item.description_text">
        </span>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'news',
  data () {
    return {
      news: null
    }
  },
  mounted () {
    this.$http
      .get('backend/news')
      .then(response => (this.news = response.data.reverse()))
  }
}
</script>

<style scoped>
#project{
  width: 80%;
}

</style>
