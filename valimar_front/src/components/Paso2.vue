<template>
  <h2 class="mb-4">Paso 2 - Bloqueo de Habitación</h2>
  <!-- Mostramos mensaje dinámico -->
  <div v-if="true" v-html="mensaje"></div>

  <!-- Mostramos mensaje de error -->
  <div v-if="error" class="alert alert-warning" role="alert">
    {{ error }}
  </div>
  <!-- Botón para avanzar al paso 3 -->
  <button class="btn btn-primary mt-3" 
          @click="nextStep" 
          v-if="habitacionBloqueada">
    Continuar al Paso 3
  </button>
</template>

<script>
import axios from "axios"; // Asegúrate de importar Axios
export default {
  name: "Paso2",
  props: {
    currentStep: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      mensaje: "", // Mensaje dinámico
      error: "", // Mensaje de error
      habitacionBloqueada: false, // Indica si se permite continuar
    };
  },
  mounted() {
    this.mensaje = "Prueba"; // Asignamos texto directamente al cargar
  },
  methods: {
    nextStep() {
      this.$emit("nextStep");
    },
    mostrarMensaje(mensaje) {
      console.log("me llega este mensaje: -> " + mensaje);
      this.mensaje = mensaje; // Actualiza el mensaje
      this.error = ""; // Limpia errores previos
      this.habitacionBloqueada = true; // Marca la habitación como bloqueada

      this.$forceUpdate(); // Fuerza el refresco del componente (depuración)
    },
    mostrarError(mensajeError) {
      this.error = mensajeError; // Actualiza el mensaje de error
      this.mensaje = ""; // Limpia mensajes previos
      this.habitacionBloqueada = false; // Marca la habitación como no bloqueada
    }
  },
  watch: {
    mensaje(newMensaje) {
      console.log("Watcher detecta cambio en mensaje: ", newMensaje);
    },
  },
};
</script>