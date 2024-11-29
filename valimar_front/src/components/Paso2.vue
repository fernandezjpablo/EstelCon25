<template>
    <div>
      <h2 class="mb-4">Paso 2</h2>
      <div id="paso2" class="mb-4"></div>
      <div class="d-flex justify-content-between">
        <button class="btn btn-secondary" @click="prevStep">Anterior</button>
        <button class="btn btn-primary" @click="nextStep">Siguiente</button>
      </div>
    </div>
  </template>
  
  <script>
  import $ from 'jquery';
  
  export default {
    props: ['formData'],
    methods: {
      nextStep() {
        if (this.formData && this.formData.comboHabitaciones) {
          this.bloquearHabitacion();
          this.$emit('nextStep', { /* datos del paso 2 */ });
        } else {
          console.error("comboHabitaciones no está definido en formData");
        }
      },
      prevStep() {
        this.$emit('prevStep');
      },
      bloquearHabitacion() {
        const comboHabitaciones = this.formData.comboHabitaciones.split(',');
        const capacidad = comboHabitaciones[0];
        const camas = comboHabitaciones[1];
  
        $.ajax({
          url: "/Valimar/BloquearHabitacion",
          type: "POST",
          data: `capacidad=${capacidad}&camas=${camas}`,
          success: function(respuesta, estados) {
            if (respuesta != "") {
              $('#paso2').html(`
                <div class="alert alert-success" role="alert">
                  Tiene bloqueada temporalmente la habitación ${respuesta}.<br>
                  <strong>Dispone de 10 minutos</strong> para formalizar la reserva antes de que la habitación vuelva a liberarse.
                </div>
              `);
              this.generarPaso3(capacidad, respuesta);
              $('#btnPaso2').prop('disabled', true);
              $("#idhabitacion").val(respuesta);
            } else {
              $('#paso2').html(`
                <div class="alert alert-danger" role="alert">
                  Las habitaciones de esta capacidad ya no están disponibles.
                </div>
              `);
            }
            $('#paso2').show();
            $('#paso1').hide();
          }.bind(this)
        });
      },
      generarPaso3(capacidad, idhabitacion) {
        var elemDatos = document.getElementById("paso3Datos");
        $("#paso3Datos").html("");
        for (var i = 0; i < capacidad; i++) {
          var divinscrito = document.createElement("div");
          divinscrito.classList.add("mb-4", "p-3", "border", "rounded");
          var linea_menor = "";
          if (i > 0) {
            linea_menor = `
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="es_menor_${i + 1}" onClick="toggle('${i + 1}');">
                <label class="form-check-label" for="es_menor_${i + 1}">Menor entre 2 y 12 años</label>
              </div>
            `;
          }
          if (i == 0) {
            linea_menor = `
              <div class="form-check">
                <input class="form-check-input" type="checkbox" id="con_bebes_${i + 1}" onClick="$('#fecha_bebe_${i + 1}').toggle();">
                <label class="form-check-label" for="con_bebes_${i + 1}">Voy con menor/es de 2 años (no ocupan plaza)</label>
              </div>
              <select style="display:none" id="fecha_bebe_${i + 1}" class="form-control mt-2">
                <option value=0>(Año de nacimiento del menor)</option>
                <option value=2022>2022</option>
                <option value=2023>2023</option>
                <option value=2024>2024</option>
              </select>
            `;
          }
          divinscrito.innerHTML = `
            <h5>Datos del inscrito número ${i + 1}</h5>
            <div class="form-group">
              <label for="nombre_${i + 1}">Nombre</label>
              <input type="text" class="form-control" id="nombre_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-group">
              <label for="apellidos_${i + 1}">Apellidos</label>
              <input type="text" class="form-control" id="apellidos_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-group">
              <label for="pseudonimo_${i + 1}">Pseudónimo (opcional)</label>
              <input type="text" class="form-control" id="pseudonimo_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-group">
              <label for="email_${i + 1}">Email</label>
              <input type="email" class="form-control" id="email_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-group">
              <label for="telefono_${i + 1}">Teléfono</label>
              <input type="tel" class="form-control" id="telefono_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-group">
              <label for="iden_${i + 1}">NIF/NIE/Pasaporte</label>
              <input type="text" class="form-control" id="iden_${i + 1}" onChange="disableEnviar();">
            </div>
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="lopd_${i + 1}" onChange="disableEnviar();">
              <label class="form-check-label" for="lopd_${i + 1}">
                He leído y acepto las <a href="terminos.html" target="_blank">condiciones de inscripción al evento</a> (incluidas las referentes a uso de mi imagen)
              </label>
            </div>
            ${linea_menor}
            <small class="form-text text-muted">(*) Campos obligatorios (salvo NIF en menores de 12 años)</small>
          `;
          elemDatos.appendChild(divinscrito);
        }
        $('#paso3').show();
      }
    }
  };
  </script>