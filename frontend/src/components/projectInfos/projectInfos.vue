<template>
    <div id="container">
      <v-card id="project"
        v-for="item in info"
        v-bind:key="item.id"
              raised
      >
        <div style="display:block;">

          <v-img :src="item.image"
                 height="240"
                 contain
          >
          </v-img>
          <v-card-title>
            {{item.name}}
          </v-card-title>

          <v-card-text style="display: flex;">
            <img :src="item.companyLogo" id="companyLogo">
            <a :href="item.companyLink" target="_blank">
              {{item.companyName}}
            </a>
          </v-card-text>
        </div>
        <div>
          <v-expansion-panels flat>
            <v-expansion-panel>
              <v-expansion-panel-header>Show description</v-expansion-panel-header>
              <v-expansion-panel-content>
                <div>
                  The Project was using the following tools:<br>
                  {{item.lang}}
                  <!--&lt;!&ndash;            <span v-html="item.company"></span>&ndash;&gt;-->
                  <!--            {{item.startDate}}<br>-->
                  <!--            {{item.endDate}}-->
                </div><br>
                <span v-html="item.description">
                </span>
              </v-expansion-panel-content>
            </v-expansion-panel>
          </v-expansion-panels>
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
#companyLogo{
  height: 30px;
  width: 30px;
  margin-right: 10px;
}
#project{
  margin: 3px;
  width: 400px;
  padding: 10px;
  /*border-left: solid;*/
  /*border-color: white;*/
}
</style>
