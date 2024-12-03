import { createApp } from 'vue';
import App from './App.vue';
import '/src/assets/styles/global.css';
import $ from 'jquery';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

document.addEventListener('DOMContentLoaded', () => {
    const appElement = document.querySelector('#app');
    console.log(appElement); // Verifica si appElement es null
    if (appElement && !appElement.__vue_app__) {
        console.log('Montando la aplicación...');
        createApp(App).mount('#app');
    } else {
        console.log('La aplicación ya está montada.');
    }
});