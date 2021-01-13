<template>
  <v-layout>
<!--    {{info[0]}}-->
    <v-flex >
      <div
        v-for="item in info"
        v-bind:key="item.id"
        style="margin-bottom: 10px;"
      >
        <div style="display:block;">
          <h4>{{item.name}}</h4>
          <div class="my-4 subtitle-1">
            as {{item.roleName}} @
            <span v-html="item.company"
                  style="margin-top: 32px;"
            ></span>
          </div>
        </div>
        <div>
          <span v-html="item.description">
          </span>
          <div>
            {{item.lang}}
            <span v-html="item.company"></span>
            {{item.startDate}}
            {{item.endDate}}
            {{item.roleName}}
            {{item.name}}
          </div>

        </div>
      </div>
    </v-flex>
  </v-layout>

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

</style>
