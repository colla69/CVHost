<template>
  <v-container fluid >
    <h3>Professional Experience</h3>
      <v-timeline dense >
        <v-timeline-item small >
            <v-row>
              <v-col cols="2">
                <p>2020 - now</p>
              </v-col>
              <v-col>
                <v-expansion-panels>
                <v-expansion-panel >
                  <v-expansion-panel-header>
                    <h4>Software Engineer @ Device Insight GmbH</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <ul>
                      <li>Planning and implementing new Features</li>
                      <li></li>
                    </ul>
                  </v-expansion-panel-content>
                </v-expansion-panel>
                </v-expansion-panels>
              </v-col>
            </v-row>
        </v-timeline-item>
        <v-timeline-item small >
          <v-row>
            <v-col cols="2">
              <p>2018 - 2020</p>
            </v-col>
            <v-col>
              <v-expansion-panels>
                <v-expansion-panel>
                  <v-expansion-panel-header>
                    <h4>DevOps developer @ MsgGillardon AG</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <ul>
                      <li>Planning and writing of testcases for Leasing Marketing System</li>
                    </ul>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>
          </v-row>
        </v-timeline-item>
        <v-timeline-item small >
          <v-row >
            <v-col cols="2">
              <v-container>2015 - 2018</v-container>
            </v-col>
            <v-col>
              <v-expansion-panels>
                <v-expansion-panel>
                  <v-expansion-panel-header>
                    <h4>Senior Application developer @ 3Points Software Gmbh</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <ul>
                      <li>Development of new Modules for mediafusion</li>
                      <li>Planning and development of databases</li>
                      <li>Development of Springboot applications</li>
                      <li>Remote Deployments and support</li>
                    </ul>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>
          </v-row>
        </v-timeline-item>
        <v-timeline-item small >
          <v-row >
            <v-col cols="2">
              <v-container>2013 - 2015</v-container>
            </v-col>
            <v-col>
              <v-expansion-panels>
                <v-expansion-panel>
                  <v-expansion-panel-header>
                    <h4>Junior Application developer @ 3Points Software Gmbh</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <ul>
                      <li>Development of new Product Versions</li>
                      <li>Remote Deployments and support</li>
                      <li>Planning and development of databases</li>
                      <li>Development of Delphi applications</li>
                    </ul>
                  </v-expansion-panel-content>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>
          </v-row>
        </v-timeline-item>
      </v-timeline>
  </v-container>
</template>

<script>
export default {
  name: 'WorkExperience'
}
</script>

<style scoped>

</style>
