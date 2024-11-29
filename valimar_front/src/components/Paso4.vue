<template>
  <div>
    <h2>Paso 4</h2>
    <div id="paso4"></div>
    <button @click="prevStep">Anterior</button>
    <button @click="submitForm">Enviar</button>
  </div>
</template>

<script>
import $ from 'jquery';
export default {
  props: ['formData'],
  methods: {
    prevStep() {
      this.$emit('prevStep');
    },
    submitForm() {
      this.enviarInscripcion();
    },
    enviarInscripcion() {
      var numItems = this.formData.comboHabitaciones.split(',')[0];
      var lista = [];
      for (var i = 0; i < numItems; i++) {
        var objeto = {};
        objeto["iden"] = this.formData[`iden_${i + 1}`];
        objeto["nombre"] = this.formData[`nombre_${i + 1}`];
        objeto["apellidos"] = this.formData[`apellidos_${i + 1}`];
        objeto["pseudonimo"] = this.formData[`pseudonimo_${i + 1}`];
        objeto["email"] = this.formData[`email_${i + 1}`];
        objeto["telefono"] = this.formData[`telefono_${i + 1}`];
        objeto["lopd"] = this.formData[`lopd_${i + 1}`];
        if (i == 0) {
          objeto["menor"] = false;
          objeto["con_bebes"] = this.formData[`fecha_bebe_${i + 1}`];
        } else {
          objeto["menor"] = this.formData[`es_menor_${i + 1}`];
          objeto["con_bebes"] = 0;
        }
        lista.push(objeto);
      }
      $.ajax({
        url: "/Valimar/RegistrarInscripcion",
        type: "POST",
        data: "habitacion=" + this.formData.idhabitacion + "&datos=" + JSON.stringify(lista),
        success: function(respuesta, estados) {
          $("#paso3").hide();
          $("#paso2").hide();
          $("#paso4").show();
          $("#paso4").html(respuesta);
        }
      });
    }
  }
};
</script>