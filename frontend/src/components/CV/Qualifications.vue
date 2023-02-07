<template>
  <div class="split">
    <v-list>
      <v-list-item v-for="item in qualifications" v-bind:key="item.name">
        <v-list-item-content>
          <v-btn @click="click(item)">
            {{item.name}}
          </v-btn>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <v-container>
      <iframe :src="htmlSource"
              :key="htmlSource"
              width="100%"
              height="100%"
      />
<!--      <embed :src="htmlSource" :key="htmlSource" alt="pdf" class="pdf"-->
<!--             pluginspage="http://www.adobe.com/products/acrobat/readstep2.html">-->
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'qualifications',
  methods: {
    setHtmlSource: function (filename) {
      this.htmlSource = 'data/' + filename + '#toolbar=0'
    },
    init: function () {
      this.setHtmlSource(this.qualifications[0].filename)
    },
    click: function (item) {
      this.setHtmlSource(item.filename)
      console.log(this.htmlSource)
    }
  },
  mounted () {
    console.log('Component has been created!')
    this.init()
  },
  data: function () {
    return {
      htmlSource: '',
      qualifications: [
        {
          name: 'Hackerrank Certificate',
          filename: 'Python_cert.pdf'
        },
        {
          name: '3Points Reference',
          filename: 'ArbeitsZeugnis.pdf'
        },
        {
          name: 'Apprenticeship certificate',
          filename: 'AUSB_IHK_Zeugnis.pdf'
        },
        {
          name: 'Apprenticeship Reference',
          filename: 'AUSB_3P_Zeugnis.pdf'
        },
        {
          name: 'Abitur',
          filename: 'ABI.pdf'
        },
        {
          name: 'TestDaf',
          filename: 'TESTDAF.pdf'
        },
        {
          name: 'German B1',
          filename: 'ZD.pdf'
        },
        {
          name: 'German A2',
          filename: 'FID2.pdf'
        },
        {
          name: 'English B1',
          filename: 'PET.pdf'
        },
        {
          name: 'English A2',
          filename: 'KET.pdf'
        }
      ]
    }
  }
}
</script>

<style scoped>
.split{
  display: grid;
  grid-template-columns: 20% 80%;
  height: calc(100vh - 100px);
}
.pdf{
  width: 100%;
  height: 100%;
}
.v-btn {
  text-transform:none !important;
}
</style>
