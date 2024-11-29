<template>
  <div>
    <h2 class="mb-4">Paso 2</h2>
    <div ref="paso2" v-html="contenidoPaso2"></div>
    <button class="btn btn-primary mt-3" @click="nextStep" :disabled="isDisabled">Siguiente</button>
    <button class="btn btn-secondary mt-3" @click="prevStep">Anterior</button>
  </div>
</template>

<script>
import axios from 'axios'; // Asegúrate de importar Axios

export default {
  props: {
    formData: {
      type: Object,
      required: true,
    },
    baseUrl: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      contenidoPaso2: '', // Almacena el contenido del paso 2
      isDisabled: false, // Controla el estado del botón "Siguiente"
    };
  },
  methods: {
    // Método para pasar al siguiente paso
    nextStep() {
      this.$emit("nextStep");
    },
    // Método para regresar al paso anterior
    prevStep() {
      this.$emit("prevStep");
    },
    // Método para cargar el contenido del paso 2
    cargarPaso2() {
      const capacidad = this.formData.comboHabitaciones.split(',')[0];
      const camas = this.formData.comboHabitaciones.split(',')[1];

      console.log("Enviando datos a la API:", { capacidad, camas });

      axios.post(`${this.baseUrl}/Valimar/BloquearHabitacion`, {
        capacidad: capacidad,
        camas: camas
      })
      .then((response) => {
        console.log("Respuesta de la API:", response.data);
        if (response.data) {
          this.formData.idhabitacion = response.data;
          this.contenidoPaso2 = `Tiene bloqueada temporalmente la habitación ${response.data}. Dispone de 10 minutos para formalizar la reserva antes de que la habitación vuelva a liberarse.`;
          this.isDisabled = true; // Deshabilita el botón "Siguiente"
        } else {
          this.contenidoPaso2 = "Las habitaciones de esta capacidad ya no están disponibles.";
        }
      })
      .catch((error) => {
        console.error("Error al bloquear la habitación:", error);
      });
    },
  },
  mounted() {
    this.cargarPaso2();
  },
};
</script>