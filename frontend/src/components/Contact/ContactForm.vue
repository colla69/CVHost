<template>
<v-container>
  <div id="contact-form" class="contact-form">
    <h1 class="contact-form_title">Contact</h1>
    <p>Feel free to reach out via email:</p>
    <a class="mailto-link" href="mailto:a.colarietitosti@googlemail.com">a.colarietitosti@googlemail.com</a>
  </div>
</v-container>
</template>

<script>
export default {
  name: 'Contact'
}
</script>

<style scoped>
.contact-form {
  margin: 5em;
}

.contact-form .form {
  display: flex;
  flex-direction: column;
  font-size: 16px;
}

.contact-form_title {
  text-align: left;
  font-size: 28px;
}

.contact-form input[type="email"],
.contact-form input[type="text"],
.contact-form textarea {
  border: solid 1px #e8e8e8;
  font-family: 'Roboto', sans-serif;
  padding: 10px 7px;
  margin-bottom: 15px;
  outline: none;
}

.contact-form .button {
  background: #da552f;
  border: solid 1px #da552f;
  cursor: pointer;
  padding: 10px 50px;
  text-align: center;
  text-transform: uppercase;
}

.contact-form .button:hover {
  border: solid 1px #ea532a;
}

.contact-form input[type="email"],
.contact-form input[type="text"],
.contact-form textarea,
.contact-form .button {
  font-size: 15px;
  border-radius: 3px
}
</style>
