<template>
  <div>
    <h2 class="mb-4">Paso 1</h2>
    <div id="paso1"></div>
    <button class="btn btn-primary mt-3" @click="nextStep">Siguiente</button>
  </div>
</template>

<script>
import $ from 'jquery';

export default {
  methods: {
    nextStep() {
      this.cargarComboPaso1();
      this.$emit('nextStep', { comboHabitaciones: this.comboHabitaciones });
    },
    cargarComboPaso1() {
      $.ajax({
        url: "/Valimar/GetComboHabitacionesDisponibles",
        type: "GET",
        success: function(respuesta, estados) {
          $('#paso1').html(respuesta);
        }
      });
    }
  },
  mounted() {
    this.cargarComboPaso1();
  }
};
</script>