<template>
  <div>
    <header class="header text-center">
      <div class="container">
        <h1>Formulario de Varias Etapas</h1>
      </div>
    </header>
    <main class="container mt-5">
      <div class="main-content">
        <Paso1 @nextStep="handleNextStep" v-if="currentStep === 1" />
        <Paso2 :formData="formData" @nextStep="handleNextStep" @prevStep="handlePrevStep" v-if="currentStep === 2" />
        <Paso3 :formData="formData" @nextStep="handleNextStep" @prevStep="handlePrevStep" v-if="currentStep === 3" />
        <Paso4 :formData="formData" @prevStep="handlePrevStep" @submitForm="handleSubmit" v-if="currentStep === 4" />
        <ControlComponent :visible="true" :isDisabled="isDisabled" @validar="handleValidar" @enviar="handleEnviar" />
      </div>
    </main>
    <footer class="footer text-center mt-5">
      <div class="container">
        <p>&copy; 2024 Formulario de Varias Etapas. Todos los derechos reservados.</p>
      </div>
    </footer>
  </div>
</template>

<script>
import Paso1 from './components/Paso1.vue';
import Paso2 from './components/Paso2.vue';
import Paso3 from './components/Paso3.vue';
import Paso4 from './components/Paso4.vue';
import ControlComponent from './components/ControlComponent.vue';

export default {
  name: 'App',
  components: {
    Paso1,
    Paso2,
    Paso3,
    Paso4,
    ControlComponent
  },
  data() {
    return {
      currentStep: 1,
      formData: {
        comboHabitaciones: "", // Inicialmente vacío
        idhabitacion: ""
      },
      isDisabled: false
    };
  },
  methods: {
    handleNextStep(data) {
      this.formData = { ...this.formData, ...data };
      if (this.currentStep < 4) {
        this.currentStep++;
      }
    },
    handlePrevStep() {
      if (this.currentStep > 1) {
        this.currentStep--;
      }
    },
    handleValidar() {
      // Lógica de validación
      console.log('Validar');
    },
    handleEnviar() {
      // Lógica de envío
      console.log('Enviar');
    },
    handleSubmit() {
      // Lógica de envío final
      console.log('Formulario enviado', this.formData);
    }
  }
};
</script>

<style>
#app {
  text-align: center;
  margin-top: 20px;
}
</style>