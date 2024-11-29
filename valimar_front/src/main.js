import { createApp } from 'vue';
import App from './App.vue';
import '/src/assets/styles/global.css';
import $ from 'jquery';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

// Evitar múltiples montajes durante el desarrollo con HMR
if (!document.querySelector('#app').__vue_app__) {
  console.log('Montando la aplicación...');
  createApp(App).mount('#app');
} else {
  console.log('La aplicación ya está montada.');
}
