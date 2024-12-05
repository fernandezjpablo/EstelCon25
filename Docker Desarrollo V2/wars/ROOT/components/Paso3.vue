<template>
  <div>
    <div v-if="currentStep === 3" class="container">
      <p>¡Estás en el paso 3!</p>
      <!-- Mensaje visual para que se vea en la UI -->
      <div id="paso3Datos" class="row">
        <div class="col-12" v-for="(inscrito, index) in inscritos" :key="index">
          <div class="card mb-4">
            <div class="card-body">
              <h2>Datos del inscrito número {{ index + 1 }}</h2>

              <div class="mb-3">
                <label :for="'nombre_' + index" class="form-label">Nombre</label>
                <input type="text" :id="'nombre_' + index" class="form-control" v-model="inscrito.nombre"
                  @input="disableEnviar" required />
              </div>

              <div class="mb-3">
                <label :for="'apellidos_' + index" class="form-label">Apellidos</label>
                <input type="text" :id="'apellidos_' + index" class="form-control" v-model="inscrito.apellidos"
                  @input="disableEnviar" required />
              </div>

              <div class="mb-3">
                <label :for="'pseudonimo_' + index" class="form-label">Pseudónimo (opcional)</label>
                <input type="text" :id="'pseudonimo_' + index" class="form-control" v-model="inscrito.pseudonimo"
                  @input="disableEnviar" />
              </div>

              <div class="mb-3">
                <label :for="'email_' + index" class="form-label">Email</label>
                <input type="email" :id="'email_' + index" class="form-control" v-model="inscrito.email"
                  @input="disableEnviar" required />
              </div>

              <div class="mb-3">
                <label :for="'telefono_' + index" class="form-label">Teléfono</label>
                <input type="tel" :id="'telefono_' + index" class="form-control" v-model="inscrito.telefono"
                  @input="disableEnviar" required />
              </div>

              <div class="mb-3">
                <label :for="'iden_' + index" class="form-label">NIF/NIE/Pasaporte</label>
                <input type="text" :id="'iden_' + index" class="form-control" v-model="inscrito.nif"
                  @input="disableEnviar" required />
              </div>

              <div class="form-check">
                <input type="checkbox" :id="'lopd_' + index" class="form-check-input"
                  v-model="inscrito.aceptaCondiciones" @change="disableEnviar" />
                <label class="form-check-label" :for="'lopd_' + index">
                  He leído y acepto las
                  <a href="terminos.html" target="_blank">condiciones de inscripción al evento</a>
                  (incluidas las referentes a uso de mi imagen)
                </label>
              </div>

              <div v-if="index === 0" class="mt-3">
                <input type="checkbox" :id="'con_bebes_' + index" @click="toggleBebe(index)" class="form-check-input" />
                <label :for="'con_bebes_' + index" class="form-check-label">
                  Voy con menor/es de 2 años (no ocupan plaza)
                </label>
                <div v-if="showBebe[index]" class="mt-2">
                  <select :id="'fecha_bebe_' + index" class="form-select" v-model="inscrito.fechaBebe">
                    <option value="0">(Año de nacimiento del menor)</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                    <option value="2024">2024</option>
                  </select>
                </div>
              </div>

              <div v-if="index > 0" class="mt-3">
                <input type="checkbox" :id="'es_menor_' + index" v-model="inscrito.menor" class="form-check-input" />
                <label :for="'es_menor_' + index" class="form-check-label">
                  Menor entre 2 y 12 años
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button class="btn btn-primary" @click="nextStep" :disabled="isEnviarDisabled">Siguiente paso</button>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';


export default {
  name: "Paso3",
  props: {
    formData: {
      type: Object,
      required: true,
    },
    baseUrl: {
      type: String,
      required: true,
    },
    currentStep: {
      type: Number,
      required: true,
    },
    inscritos: {
      type: Array,
      required: true,
    }
  },
  data() {
    return {
      isEnviarDisabled: true,
      showBebe: [],
    };
  },
  mounted() {
    console.log("Mounted Paso3.vue, currentStep:", this.currentStep);
  },
  watch: {
    currentStep(newStep) {
      console.log("Cambio de paso:", newStep);
      if (newStep === 3) {
        console.log("Estamos en el paso 3");
      }
    },
  },
  methods: {
    prevStep() {
      this.$emit("prevStep");
    },
    validar() {
      const errores = [];

      this.inscritos.forEach((inscrito, index) => {
        if (!inscrito.nombre) errores.push(`Nombre ${index + 1} en blanco.`);
        if (!inscrito.apellidos) errores.push(`Apellidos ${index + 1} en blanco.`);
        if (!inscrito.email) errores.push(`Email ${index + 1} en blanco.`);
        if (!inscrito.telefono) errores.push(`Teléfono ${index + 1} en blanco.`);
        if (!inscrito.nif && !inscrito.menor)
          errores.push(`NIF/NIE ${index + 1} en blanco.`);
        if (!inscrito.aceptaCondiciones)
          errores.push(
            `Debe aceptar las condiciones de inscripción para el inscrito ${index + 1}.`
          );
      });

      if (errores.length > 0) {
        alert(errores.join("\n"));
      } else {
        this.isEnviarDisabled = false;
      }
    },
    async enviarInscripcion() {
      try {
        const response = await fetch(`${this.baseUrl}/Valimar/RegistrarInscripcion`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            habitacion: this.formData.idhabitacion,
            datos: this.inscritos,
          }),
        });

        const respuesta = await response.text();
        document.getElementById("paso4").innerHTML = respuesta;
      } catch (error) {
        console.error("Error al enviar la inscripción:", error);
        alert("Hubo un error al enviar la inscripción. Por favor, inténtalo de nuevo.");
      }
    },
    disableEnviar() {
      this.isEnviarDisabled = true;
    },
    toggleBebe(index) {
      Vue.set(this.showBebe, index, !this.showBebe[index]);
    },
    toggleMenor(index) {
      Vue.set(this.inscritos[index], 'menor', !this.inscritos[index].menor);
    },
    nextStep() {
      this.validar();
      if (!this.isEnviarDisabled) {
        this.$emit("nextStep");
      }
    },
  },
};
</script>