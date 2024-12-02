<template>
  <div class="container">
    <h2 class="mb-4">Paso 1</h2>
    <div class="row">
      <div class="col-md-6">
        <!-- Contenedor dinámico para las habitaciones -->
        <div ref="paso1" v-html="comboHabitaciones"></div>
        <button class="btn btn-primary mt-3" @click="nextStep">Siguiente</button>
      </div>
      <div class="col-md-6" v-if="selectedHabitacion">
        <h3>Detalles de la Habitación Seleccionada</h3>
        <p><strong>Capacidad:</strong> {{ selectedHabitacion.capacidad }}</p>
        <p><strong>Camas:</strong> {{ selectedHabitacion.camas }}</p>
      </div>
    </div>
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
      comboHabitaciones: null, // Almacena el contenido del combo
      selectedHabitacion: null, // Detalles de la habitación seleccionada
    };
  },
  methods: {
    // Método para cargar habitaciones
    cargarComboPaso1(callback) {
      console.log("Cargando habitaciones desde:", this.baseUrl);
      axios
        .get(`${this.baseUrl}/Valimar/GetComboHabitacionesDisponibles`)
        .then((response) => {
          console.log("Respuesta del servidor (habitaciones):", response.data);

          // Procesar HTML dinámico y eliminar botones innecesarios
          const parser = new DOMParser();
          const doc = parser.parseFromString(response.data, "text/html");
          const buttons = doc.querySelectorAll('input[type="button"]');
          buttons.forEach((button) => button.remove());

          // Asignar el HTML procesado al componente
          this.comboHabitaciones = doc.body.innerHTML;

          // Llamar al callback después de cargar
          if (callback) callback();
        })
        .catch((error) => {
          console.error("Error al cargar el combo de habitaciones:", error.response || error.message);
        });
    },
    // Método para manejar la selección de una habitación
    handleSelection(event) {
      const selectedOption = event.target.value;
      if (selectedOption.includes(",")) {
        const [capacidad, camas] = selectedOption.split(",");
        this.selectedHabitacion = { capacidad, camas };
      } else {
        console.error("Formato inesperado para la opción seleccionada:", selectedOption);
      }
    },
    // Método para pasar al siguiente paso
    nextStep() {
      const selectElement = this.$refs.paso1.querySelector("select");
      if (selectElement) {
        const selectedOption = selectElement.value;
        if (!selectedOption.includes(",")) {
          console.error("Formato de la opción seleccionada no válido:", selectedOption);
          return;
        }
        const [capacidad, camas] = selectedOption.split(",");
        console.log("Capacidad seleccionada:", capacidad);
        console.log("Camas seleccionadas:", camas);

        // Enviar datos al servidor para bloquear la habitación
        axios
          .post(`${this.baseUrl}/BloquearHabitacion`, { capacidad, camas })
          .then((response) => {
            console.log("Habitación bloqueada con ID:", response.data);
            this.$emit("nextStep", { comboHabitaciones: selectedOption });
          })
          .catch((error) => {
            console.error("Error al bloquear la habitación:", error.response || error.message);
          });
      } else {
        console.error("El elemento <select> no está disponible.");
      }
    },
  },
  mounted() {
    console.log("Montando el componente Paso1...");
    this.cargarComboPaso1(() => {
      this.$nextTick(() => {
        const selectElement = this.$refs.paso1.querySelector("select");
        if (selectElement) {
          selectElement.addEventListener("change", this.handleSelection);
        } else {
          console.error("No se encontró el elemento <select> después de cargar el combo.");
        }
      });
    });
  },
};
</script>
