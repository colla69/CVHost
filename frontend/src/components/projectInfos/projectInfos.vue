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
          />

          <v-card-title>
            {{item.Name}}
          </v-card-title>

          <v-card-text style="display: flex;">
            <img :src="item.company_logo" id="companyLogo"/>
            <a :href="item.company_link" target="_blank">
              {{item.company_name}}
            </a>
          </v-card-text>
        </div>
<!--        <div>-->
<!--          <v-expansion-panels flat>-->
<!--            <v-expansion-panel>-->
<!--              <v-expansion-panel-header>Show description</v-expansion-panel-header>-->
<!--              <v-expansion-panel-content>-->
<!--                <div>-->
<!--                  The Project was using the following tools:<br>-->
<!--                  {{item.lang}}-->
<!--                  &lt;!&ndash;&lt;!&ndash;            <span v-html="item.company"></span>&ndash;&gt;&ndash;&gt;-->
<!--                  &lt;!&ndash;            {{item.startDate}}<br>&ndash;&gt;-->
<!--                  &lt;!&ndash;            {{item.endDate}}&ndash;&gt;-->
<!--                </div><br>-->
<!--                <span v-html="item.description">-->
<!--                </span>-->
<!--              </v-expansion-panel-content>-->
<!--            </v-expansion-panel>-->
<!--          </v-expansion-panels>-->
<!--        </div>-->
      </v-card>
    </div>

</template>

<script>
import data from './project_infos.json'

const sortedData = data.reverse()

export default {
  name: 'projectInfos',
  data () {
    return {
      info: sortedData
    }
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
