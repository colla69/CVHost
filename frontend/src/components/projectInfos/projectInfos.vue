<template>
    <div id="container">
      <v-card id="project"
        v-for="item in info"
        v-bind:key="item.id"
      >
        <div style="display:block;">
          <h4>{{item.name}}</h4>
          <div class="my-4 subtitle-1">
            as {{item.roleName}} @
            <a :href="item.companyLink" target="_blank">
              {{item.companyName}}
            </a>
          </div>
        </div>
        <div>
          <span v-html="item.description">
          </span>
          <div>
            {{item.lang}}<br>
<!--            <span v-html="item.company"></span>-->
            {{item.startDate}}<br>
            {{item.endDate}}<br>
            {{item.roleName}}<br>
            {{item.name}}
          </div>
        </div>
      </v-card>
    </div>

</template>

<script>
export default {
  name: 'projectInfos',
  data () {
    return {
      info: null
    }
  },
  mounted () {
    this.$http
      .get('backend/projectInfos')
      .then(response => (this.info = response.data))
  }
}
</script>

<style scoped>
#container{
  display: flex;
  flex-wrap: wrap;
}
#project{
  margin-bottom: 10px;
  width: 450px;
  padding-left: 10px;
  /*border-left: solid;*/
  /*border-color: white;*/
}
</style>
