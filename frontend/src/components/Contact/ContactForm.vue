<template>
<v-container>
  <div id="contact-form" class="contact-form">
    <h1 class="contact-form_title">Contact Form</h1>
<!--    <div class="separator"></div>-->

    <form class="form" @submit="onSubmit">
      <input required name="name" v-model='contact.name' placeholder="Name" type="text" autocomplete="off">
      <input required name="email" v-model="contact.email" placeholder="E-mail" type="email" autocomplete="off">
      <textarea required name="message" v-model="contact.message" rows="4" placeholder="Message"></textarea>
      <button class="button">Send</button>
    </form>
  </div>
</v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Contact',

  data: () => {
    return {
      contact: {
        name: '',
        email: '',
        message: ''
      },
      isSending: false
    }
  },
  methods: {
    clearForm () {
      for (const field in this.contact) {
        this.contact[field] = ''
      }
    },
    onSubmit (evt) {
      evt.preventDefault()
      this.isSending = true

      axios.post('/backend/saveMessage',
        {
          sender: this.contact.name,
          email: this.contact.email,
          message: this.contact.message
        })
    }
  }
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
