<template>
  <v-toolbar dense  id="menu" flat>
    <v-avatar>
      <img src="../assets/foto.png" alt="Avatar">
    </v-avatar>
    <router-link to="/">
      <v-btn id="menuBtn" >
        Home
      </v-btn>
    </router-link>
    <router-link to="/experience">
      <v-btn id="menuBtn" >
        Experience
      </v-btn>
    </router-link>
    <router-link to="/aboutMe">
      <v-btn id="menuBtn" >
        About Me
      </v-btn>
    </router-link>
    <a href="mailto:a.colarietitosti@googlemail.com">
      <v-btn id="menuBtn" >
        Hire Me!
      </v-btn>
    </a>
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
