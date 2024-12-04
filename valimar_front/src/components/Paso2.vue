<template>
  <h2 class="mb-4">Paso 2 - Bloqueo de Habitación</h2>

  <!-- Mensaje de éxito -->
  <div
    v-if="mensaje && !error"
    class="alert alert-success"
    role="alert"
    v-html="mensaje"
  ></div>

  <!-- Mensaje de error -->
  <div v-if="error" class="alert alert-warning" role="alert">
    {{ error }}
  </div>

  <!-- Botón para avanzar al paso 3 -->
  <button
    class="btn btn-primary mt-3"
    @click="nextStep"
    v-if="habitacionBloqueada"
  >
    Continuar al Paso 3
  </button>
</template>

<script>
import axios from "axios";

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
      mensaje: "", // Mensaje de éxito
      error: "", // Mensaje de error
      habitacionBloqueada: false, // Indica si se permite avanzar
    };
  },
  mounted() {
    // Inicializar los mensajes
    this.mensaje = "";
    this.error = "";
  },
  methods: {
    nextStep() {
      console.log("Botón 'Continuar al Paso 3' clicado.");
      this.$emit("nextStep");
    },
    mostrarMensaje(mensaje) {
      if (mensaje) {
        console.log("Mensaje recibido:", mensaje);
        this.mensaje = mensaje;
        this.error = "";
        this.habitacionBloqueada = true;
        console.log("Habitación bloqueada:", this.habitacionBloqueada);
      } else {
        console.warn("El mensaje proporcionado está vacío o no es válido.");
      }
    },
    mostrarError(mensajeError) {
      if (mensajeError) {
        console.log("Error recibido:", mensajeError);
        this.error = mensajeError;
        this.mensaje = "";
        this.habitacionBloqueada = false;
        console.log("Habitación bloqueada:", this.habitacionBloqueada);
      } else {
        console.warn("El mensaje de error proporcionado está vacío o no es válido.");
      }
    },
  },
  watch: {
    mensaje(newMensaje) {
      if (newMensaje) {
        console.log("Watcher detectó cambio en mensaje: ", newMensaje);
        this.habitacionBloqueada = true;
      }
    },
    error(newError) {
      if (newError) {
        console.log("Watcher detectó cambio en error: ", newError);
        this.habitacionBloqueada = false;
      }
    },
  },
};
</script>
