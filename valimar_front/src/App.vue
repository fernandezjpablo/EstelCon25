<template>
  <div id="app">
    <h1>Formulario de Varias Etapas</h1>
    <!-- Paso 1: Formulario inicial -->
    <Paso1 @nextStep="handleNextStep" v-if="currentStep === 1" />
    
    <!-- Paso 2: Formulario de detalles adicionales -->
    <Paso2 :formData="formData" v-if="currentStep === 2" />

    <!-- Agregar más pasos si es necesario -->

  </div>
</template>

<script>
import Paso1 from './components/Paso1.vue';
import Paso2 from './components/Paso2.vue';

export default {
  name: 'App',
  components: {
    Paso1,
    Paso2
  },
  data() {
    return {
      currentStep: 1, // Controla el paso actual del formulario
      formData: null,  // Almacena los datos del formulario de cada paso
    };
  },
  methods: {
    handleNextStep(data) {
      // Almacena los datos del paso 1
      this.formData = data;
      // Cambia al siguiente paso
      this.currentStep = 2;
    },
  },
};
</script>

<style>
/* Estilos opcionales para tu aplicación */
#app {
  text-align: center;
  margin-top: 20px;
}
</style>
