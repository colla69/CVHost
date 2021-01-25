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
                    <h5>Used technologies:</h5>
                    <ul>
                      <li>Spring</li>
                      <li>Oracle DB</li>
                      <li>PostgreSQL</li>
                      <li>Wildfly</li>
                      <li>Cucumber</li>
                      <li>JUnit</li>
                      <li>Gitlab CI</li>
                      <li>Hibernate</li>
                      <li>maven</li>
                      <li>linux</li>
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
                    <h5>Used technologies:</h5>
                    <ul>
                      <li>Java EE</li>
                      <li>J2EE</li>
                      <li>Oracle DB</li>
                      <li>Flyway</li>
                      <li>Jenkins</li>
                      <li>HP ALM</li>
                      <li>Payara Server</li>
                      <li>Hibernate</li>
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
              <p>2015 - 2018</p>
            </v-col>
            <v-col>
              <v-expansion-panels>
                <v-expansion-panel>
                  <v-expansion-panel-header>
                    <h4>Senior Application developer @ 3Points Software Gmbh</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <h5>Used technologies:</h5>
                    <ul>
                      <li>Delphi XE</li>
                      <li>MS SQL Server</li>
                      <li>FastReport</li>
                      <li>Spring Boot</li>
                      <li>Vaadin</li>
                      <li>JUnit</li>
                      <li>EclipseLink</li>
                      <li>maven</li>
                      <li>git</li>
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
              <p>2013 - 2015</p>
            </v-col>
            <v-col>
              <v-expansion-panels>
                <v-expansion-panel>
                  <v-expansion-panel-header>
                    <h4>Junior Application developer @ 3Points Software Gmbh</h4>
                  </v-expansion-panel-header>
                  <v-expansion-panel-content>
                    <h5>Used technologies:</h5>
                    <ul>
                      <li>Delphi XE</li>
                      <li>Delphi 5</li>
                      <li>MS SQL Server</li>
                      <li>CVS</li>
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
