<template>
  <div>
    <h2>Paso 3</h2>
    <div id="paso3Datos"></div>
    <button @click="prevStep">Anterior</button>
    <button @click="validar">Validar datos de inscripción</button>
    <button @click="enviarInscripcion" :disabled="isEnviarDisabled">Enviar inscripción</button>
  </div>
</template>

<script>
import $ from 'jquery';
export default {
  props: ['formData'],
  data() {
    return {
      isEnviarDisabled: true
    };
  },
  methods: {
    prevStep() {
      this.$emit('prevStep');
    },
    validar() {
      var numItems = this.formData.comboHabitaciones.split(',')[0];
      var resultado_validacion = "";
      var lista = [];
      for (var i = 0; i < numItems; i++) {
        var objeto = {};
        objeto["iden"] = this.formData[`iden_${i + 1}`];
        objeto["nombre"] = this.formData[`nombre_${i + 1}`];
        objeto["apellidos"] = this.formData[`apellidos_${i + 1}`];
        objeto["email"] = this.formData[`email_${i + 1}`];
        objeto["telefono"] = this.formData[`telefono_${i + 1}`];
        if (i == 0) {
          objeto["menor"] = false;
        } else {
          objeto["menor"] = this.formData[`es_menor_${i + 1}`];
        }
        if (this.formData[`nombre_${i + 1}`] == '') resultado_validacion += `Nombre ${i + 1} en blanco.\n`;
        if (this.formData[`apellidos_${i + 1}`] == '') resultado_validacion += `Apellidos ${i + 1} en blanco.\n`;
        if (this.formData[`email_${i + 1}`] == '') resultado_validacion += `Email ${i + 1} en blanco.\n`;
        if (this.formData[`telefono_${i + 1}`] == '') resultado_validacion += `Teléfono ${i + 1} en blanco.\n`;
        if ((this.formData[`iden_${i + 1}`] == '') && (!this.formData[`es_menor_${i + 1}`])) resultado_validacion += `NIF/NIE ${i + 1} en blanco.\n`;
        if ((this.formData[`fecha_bebe_${i + 1}`] == 0) && (this.formData[`con_bebes_${i + 1}`])) resultado_validacion += `Si va con menores de 4 años debe seleccionar el año de nacimiento.\n`;
        if ((this.formData[`lopd_${i + 1}`] == '') && (!this.formData[`lopd_${i + 1}`])) resultado_validacion += `Debe leer y aceptar las condiciones de inscripción (LOPD) para el inscrito ${i + 1}.\n`;
        lista.push(objeto);
      }
      if (resultado_validacion == "") {
        $.ajax({
          url: "/Valimar/CheckInscrito",
          type: "POST",
          data: "datos=" + JSON.stringify(lista),
          success: function(respuesta, estados) {
            var respuestaArr = JSON.parse(respuesta);
            resultado_validacion = "";
            for (var j = 0; j < respuestaArr.length; j++) {
              if (respuestaArr[j]["existe"]) {
                resultado_validacion += `El NIF ${respuestaArr[j]["clave"]} ya figura inscrito en el evento.\n`;
              }
            }
            if (resultado_validacion == "") {
              this.bloquearCampos();
              this.isEnviarDisabled = false;
            } else {
              alert(resultado_validacion);
            }
          }.bind(this)
        });
      } else {
        alert(resultado_validacion);
      }
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
    },
    bloquearCampos() {
      var numItems = this.formData.comboHabitaciones.split(',')[0];
      for (var i = 0; i < numItems; i++) {
        this.formData[`iden_${i + 1}`] = true;
        this.formData[`apellidos_${i + 1}`] = true;
        this.formData[`nombre_${i + 1}`] = true;
        this.formData[`pseudonimo_${i + 1}`] = true;
        this.formData[`telefono_${i + 1}`] = true;
        this.formData[`email_${i + 1}`] = true;
        this.formData[`iden_${i + 1}`] = true;
        this.formData[`es_menor_${i + 1}`] = true;
        this.formData[`lopd_${i + 1}`] = true;
        this.formData[`con_bebes_${i + 1}`] = true;
        this.formData[`fecha_bebe_${i + 1}`] = true;
      }
    },
    generarPaso3(capacidad, idhabitacion) {
      var elemDatos = document.getElementById("paso3Datos");
      $("#paso3Datos").html("");
      for (var i = 0; i < capacidad; i++) {
        var divinscrito = document.createElement("div");
        var linea_menor = "";
        if (i > 0) {
          linea_menor = "<input type='checkbox' id='es_menor_" + (i + 1) + "' onClick=toggle('" + (i + 1) + "');>Menor entre 2 y 12 años";
        }
        if (i == 0) {
          linea_menor = "<input type='checkbox' id='con_bebes_" + (i + 1) + "' onClick=\"$('#fecha_bebe_" + (i + 1) + "').toggle();\">Voy con menor/es de 2 años (no ocupan plaza)";
          linea_menor += "<br><select style=\"display:none\" id=\"fecha_bebe_" + (i + 1) + "\"><option value=0>(Año de nacimiento del menor)</option><option value=2022>2022</option><option value=2023>2023</option><option value=2024>2024</option></select>";
        }
        divinscrito.innerHTML = "<h2>Datos del inscrito número " + (i + 1) + "</h2><br>" +
          "Nombre: <input type='text' id='nombre_" + (i + 1) + "' onChange='disableEnviar();'>*<br>" +
          "Apellidos: <input type='text' id='apellidos_" + (i + 1) + "' onChange='disableEnviar();'>*<br>" +
          "Pseudónimo (opcional): <input type='text' id='pseudonimo_" + (i + 1) + "' onChange='disableEnviar();'><br>" +
          "email: <input type='text' id='email_" + (i + 1) + "' onChange='disableEnviar();'>*<br>" +
          "Teléfono: <input type='text' id='telefono_" + (i + 1) + "' onChange='disableEnviar();'>*<br>" +
          "NIF/NIE/Pasaporte: <input type='text' id='iden_" + (i + 1) + "' onChange='disableEnviar();'>*<br>" +
          "<input type='checkbox' id='lopd_" + (i + 1) + "' onChange='disableEnviar();'>He leído y acepto las <a href=\"terminos.html\" target=\"_blank\">condiciones de inscripción al evento</a> (incluidas las referentes a uso de mi imagen)*<br>" +
          linea_menor +
          "<br>(*) Campos obligatorios (salvo NIF en menores de 12 años)<br>";
        elemDatos.appendChild(divinscrito);
      }
      $('#paso3').show();
    }
  },
  mounted() {
    this.generarPaso3(this.formData.comboHabitaciones.split(',')[0], this.formData.idhabitacion);
  }
};
</script>