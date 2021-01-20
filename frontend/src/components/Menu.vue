<template>
  <v-toolbar
    id="menu"
    flat
    style="
      display: flex;
      flex-wrap: wrap;
      width: 100%;
    "
  >
    <v-avatar>
      <img src="../assets/foto.png" alt="Avatar">
    </v-avatar>
    <router-link to="/">
      <v-btn id="menuBtn" >
        Home
      </v-btn>
    </router-link>
    <router-link to="/news">
      <v-btn id="menuBtn" >
        News
      </v-btn>
    </router-link>
    <router-link to="/projectInfos">
      <v-btn id="menuBtn" >
        Projects
      </v-btn>
    </router-link>
    <router-link to="/experience">
      <v-btn id="menuBtn" >
        until now
      </v-btn>
    </router-link>
    <router-link to="/aboutMe">
      <v-btn id="menuBtn" >
        About Me
      </v-btn>
    </router-link>
    <router-link to="/contact">
      <v-btn id="menuBtn"
             color="error" >>
        Hire Me!
      </v-btn>
    </router-link>
  </v-toolbar>
</template>

<script>
export default {
  name: 'Menu'
}
</script>

<style scoped>
img {
  max-height: 150px;
  margin-right: 10px;
}
#menu{
  padding: 0px;
  margin-left: 10px;
  margin-right: 10px;
}
#menuBtn{
  margin-right: 5px;
}
</style>
