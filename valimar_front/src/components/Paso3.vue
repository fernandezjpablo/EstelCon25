<template>
  <div>
    <div class="container">


      <div id="paso3Datos" class="row">
        <div class="col-12" v-for="(inscrito, index) in inscritos" :key="index">
          <div class="card mb-4">
            <div class="card-body">
              <h2>Datos del inscrito número {{ index + 1 }}</h2>

              <!-- Nombre y Pseudónimo en la misma línea -->
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label :for="'nombre_' + index" class="form-label">Nombre</label>
                  <input type="text" :id="'nombre_' + index" class="form-control" v-model="inscrito.nombre"
                    @input="disableEnviar" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label :for="'pseudonimo_' + index" class="form-label">Pseudónimo (opcional)</label>
                  <input type="text" :id="'pseudonimo_' + index" class="form-control" v-model="inscrito.pseudonimo"
                    @input="disableEnviar" />
                </div>
              </div>

              <!-- Email y Teléfono en la misma línea -->
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label :for="'email_' + index" class="form-label">Email</label>
                  <input type="email" :id="'email_' + index" class="form-control" v-model="inscrito.email"
                    @input="disableEnviar" required />
                </div>
                <div class="col-md-6 mb-3">
                  <label :for="'telefono_' + index" class="form-label">Teléfono</label>
                  <input type="tel" :id="'telefono_' + index" class="form-control" v-model="inscrito.telefono"
                    @input="disableEnviar" required />
                </div>
              </div>

              <!-- NIF/NIE/Pasaporte -->
              <div class="mb-3">
                <label :for="'iden_' + index" class="form-label">NIF/NIE/Pasaporte</label>
                <input type="text" :id="'iden_' + index" class="form-control" v-model="inscrito.nif"
                  @input="disableEnviar" required />
              </div>

              <!-- Aceptación de condiciones -->
              <div class="form-check mb-3">
                <input type="checkbox" :id="'lopd_' + index" class="form-check-input"
                  v-model="inscrito.aceptaCondiciones" @change="disableEnviar" />
                <label class="form-check-label" :for="'lopd_' + index">
                  He leído y acepto las
                  <a href="terminos.html" target="_blank">condiciones de inscripción al evento</a>
                  (incluidas las referentes a uso de mi imagen)
                </label>
              </div>

              <!-- Checkbox: Voy con menores de 2 años -->
              <div class="form-check mb-3 d-flex align-items-center">
                <input type="checkbox" :id="'con_bebes_' + index" @click="toggleBebe(index)"
                  class="form-check-input me-2" />
                <label :for="'con_bebes_' + index" class="form-check-label">
                  Voy con menor/es de 2 años (no ocupan plaza)
                </label>
              </div>
              <div v-if="showBebe[index]" class="mt-2">
                <select :id="'fecha_bebe_' + index" class="form-select" v-model="inscrito.fechaBebe">
                  <option value="0">(Año de nacimiento del menor)</option>
                  <option value="2022">2022</option>
                  <option value="2023">2023</option>
                  <option value="2024">2024</option>
                </select>
              </div>

              <!-- Checkbox: Menor entre 2 y 12 años -->
              <div v-if="index > 0" class="form-check mt-3">
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
  methods: {
    addInscrito() {
      this.inscritos.push({
        nombre: '',
        apellidos: '',
        pseudonimo: '',
        email: '',
        telefono: '',
        nif: '',
        aceptaCondiciones: false,
        menor: false,
        fechaBebe: null,
      });
    },
    removeInscrito(index) {
      if (index > 0) this.inscritos.splice(index, 1);
    },
   toggleBebe(index) {
  // Verifica si estamos activando o desactivando el checkbox
  if (this.showBebe[index]) {
    // Si se desactiva, elimina al inscrito adicional
    if (this.inscritos.length > index + 1) {
      this.inscritos.splice(index + 1, 1);
    }
  } else {
    // Si se activa, agrega un inscrito adicional con valores iniciales
    this.inscritos.splice(index + 1, 0, {
      nombre: "",
      apellidos: "",
      pseudonimo: "",
      email: "",
      telefono: "",
      nif: "",
      aceptaCondiciones: false,
      fechaBebe: null,
    });
  }

  // Alternar el estado del checkbox
  this.showBebe[index] = !this.showBebe[index];
}

    ,
    disableEnviar() {
      this.isEnviarDisabled = true;
    },
    nextStep() {
      this.$emit("nextStep");
    },
  },
};
</script>
