<template>
  <div>
    <h2 class="mb-4">Paso 1</h2>
    <div ref="paso1" v-html="comboHabitaciones"></div>
    <button class="btn btn-primary mt-3" @click="nextStep">Siguiente</button>
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
      comboHabitaciones: null, // Almacena la respuesta de la API
    };
  },
  methods: {
    // Método para pasar al siguiente paso
    nextStep() {
      const selectElement = this.$refs.paso1.querySelector('select');
      if (selectElement) {
        const selectedOption = selectElement.value;
        this.$emit("nextStep", { comboHabitaciones: selectedOption });
      } else {
        console.error("El elemento select no está disponible.");
      }
    },
    // Método para cargar el combo de habitaciones disponibles
    cargarComboPaso1(callback) {
      console.log("Base URL:", this.baseUrl); // Depuración

      axios.get(`${this.baseUrl}/Valimar/GetComboHabitacionesDisponibles`)
        .then((response) => {
          console.log("Respuesta AJAX:", response.data);
          this.comboHabitaciones = response.data; // Guardar datos en el componente

          // Llamar al callback después de la respuesta, si existe
          if (callback) callback();
        })
        .catch((error) => {
          console.error("Error en la petición AJAX:", error);
        });
    },
  },
  // Montaje del componente: inicialización
  mounted() {
    console.log("Montando el componente Paso1...");
    this.cargarComboPaso1();
  },
};
</script>