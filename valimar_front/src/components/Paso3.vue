<template>
  <div>
    <h3>Paso 3: Datos de los inscritos</h3>

    <!-- Lista de inscritos -->

    <div v-for="(inscrito, index) in inscritos" :key="index" class="card my-3">
      <div class="card-body">
        <h5 class="card-title">Inscrito {{ index + 1 }}</h5>

        <!-- Campo Nombre -->

        <div class="mb-3 row">
          <!-- Columna 1: Nombre -->
          <div class="col-md-6">
            <label for="nombre" class="form-label">Nombre</label>
            <input
              type="text"
              v-model="inscrito.nombre"
              class="form-control"
              :class="{
                'is-invalid': !inscrito.nombre && validateAllTriggered,
              }"
              placeholder="Nombre del inscrito"
            />
            <div class="invalid-feedback">El nombre es obligatorio.</div>
          </div>

          <!-- Columna 2: Pseudónimo -->
          <div class="col-md-6">
            <label for="pseudonimo" class="form-label">Pseudónimo</label>
            <input
              type="text"
              v-model="inscrito.pseudonimo"
              class="form-control"
              :class="{}"
              placeholder="Pseudónimo"
            />
          </div>
        </div>

        <div class="mb-3">
          <label for="apellidos" class="form-label">Apellidos</label>
          <input
            type="text"
            v-model="inscrito.apellidos"
            class="form-control"
            :class="{}"
            placeholder="Apellidos del Inscrito"
          />
        </div>
        <!-- Campo Email -->

        <div class="mb-3 row">
          <!-- Columna 1: Correo electrónico -->
          <div class="col-md-6">
            <label for="email" class="form-label">Correo electrónico</label>
            <input
              type="email"
              v-model="inscrito.email"
              class="form-control"
              :class="{
                'is-invalid':
                  !validateEmail(inscrito.email) && validateAllTriggered,
              }"
              placeholder="Correo electrónico"
            />
            <div class="invalid-feedback">El email no es válido.</div>

            <!-- Columna 2: Teléfono -->
            <div class="col-md-6">
              <label for="telefono" class="form-label">Teléfono</label>
              <input
                type="tel"
                v-model="inscrito.telefono"
                class="form-control"
                :class="{
                  'is-invalid': !inscrito.telefono && validateAllTriggered,
                }"
                placeholder="Teléfono"
              />
            </div>
          </div>

          <div class="invalid-feedback">El teléfono es obligatorio.</div>
        </div>

        <!-- Campo NIF/NIE/Pasaporte -->

        <div class="mb-3">
          <label for="nif" class="form-label">NIF/NIE/Pasaporte</label>

          <input
            type="text"
            v-model="inscrito.nif"
            class="form-control"
            :class="{ 'is-invalid': !inscrito.nif && validateAllTriggered }"
            placeholder="NIF/NIE/Pasaporte"
          />

          <div class="invalid-feedback">
            El NIF/NIE/Pasaporte es obligatorio.
          </div>
        </div>

        <!-- Checkbox Menor de 2 años -->

        <div class="form-check mb-3">
          <input
            type="checkbox"
            class="form-check-input"
            v-model="inscrito.menorDeDos"
            :id="'menor_de_dos_' + index"
          />

          <label class="form-check-label" :for="'menor_de_dos_' + index">
            Va acompañado con menor de 2 años
          </label>
        </div>

        <!-- Formulario adicional para menor de 2 años -->

        <div v-if="inscrito.menorDeDos" class="card my-3 bg-light">
          <div class="card-body">
            <h6>Datos del menor de 2 años</h6>

            <div class="mb-3">
              <label for="nombreMenor" class="form-label">Nombre</label>

              <input
                type="text"
                v-model="inscrito.menor.nombre"
                class="form-control"
                placeholder="Nombre del menor"
              />
            </div>

            <div class="mb-3">
              <label for="anioNacimiento" class="form-label"
                >Año de Nacimiento</label
              >

              <select
                v-model="inscrito.anioNacimiento"
                class="form-control"
                id="anioNacimiento"
              >
                <option disabled value="">Seleccionar Año</option>

                <!-- Generamos los tres últimos años -->

                <option
                  v-for="year in lastThreeYears"
                  :key="year"
                  :value="year"
                >
                  {{ year }}
                </option>
              </select>
            </div>
          </div>
        </div>

        <!-- Checkbox Entre 2 y 16 años -->

        <div class="form-check mb-3" v-if="!inscrito.menorDeDos">
          <input
            type="checkbox"
            class="form-check-input"
            v-model="inscrito.entreDosYDieciseis"
            :id="'entre_dos_y_dieciseis_' + index"
          />

          <label
            class="form-check-label"
            :for="'entre_dos_y_dieciseis_' + index"
          >
            Tiene entre 2 y 16 años
          </label>
        </div>

        <!-- Checkbox Aceptar Condiciones -->

        <div class="form-check mb-3" v-if="!inscrito.menorDeDos">
          <input
            type="checkbox"
            class="form-check-input"
            v-model="inscrito.aceptaCondiciones"
            :id="'acepta_' + index"
          />

          <label class="form-check-label" :for="'acepta_' + index">
            Acepto las condiciones
          </label>
        </div>

        <!-- Botón Eliminar Inscrito -->

        <button
          v-if="index > 0"
          class="btn btn-danger btn-sm"
          @click="removeInscrito(index)"
        >
          Eliminar inscrito
        </button>
      </div>
    </div>

    <!-- Botón Añadir Inscrito -->

    <button class="btn btn-primary mb-3" @click="addInscrito">
      Añadir inscrito
    </button>

    <!-- Botón Siguiente -->

    <button
      class="btn btn-success"
      :disabled="!validateAll()"
      @click="nextStep"
    >
      Siguiente
    </button>
  </div>
</template>

<script>
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
    },
  },
  data() {
    return {
      validateAllTriggered: false, // Usado para mostrar mensajes de validación
    };
  },
  methods: {
    getLastThreeYears() {
      const currentYear = new Date().getFullYear();
      return [
        currentYear - 2, // Año actual menos 2
        currentYear - 1, // Año actual menos 1
        currentYear, // Año actual
      ];
    },

    validateEmail(email) {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(email);
    },
    addInscrito() {
      this.inscritos.push({
        nombre: "",
        email: "",
        telefono: "",
        nif: "",
        aceptaCondiciones: false,
        menorDeDos: false,
        entreDosYDieciseis: false,
        menor: {
          nombre: "",
          edad: 0,
        },
      });
    },
    removeInscrito(index) {
      if (index > 0) this.inscritos.splice(index, 1);
    },
    validateInscrito(inscrito) {
      if (!inscrito.nombre) return false;
      if (!this.validateEmail(inscrito.email)) return false;
      if (!inscrito.telefono) return false;
      if (!inscrito.nif) return false;
      if (!inscrito.aceptaCondiciones && !inscrito.menorDeDos) return false;
      return true;
    },
    validateAll() {
      this.validateAllTriggered = true; // Muestra errores si los hay
      return this.inscritos.every(this.validateInscrito);
    },
    nextStep() {
      if (this.validateAll()) {
        this.$emit("nextStep", 4); // Cambiar al paso 4
      } else {
        alert("Por favor, completa todos los campos obligatorios.");
      }
    },
  },
};
</script>

--- ### Detalles adicionales: - El formulario adicional para el menor de 2 años
no incluye "Aceptar condiciones". - Los checks se muestran dinámicamente según
las opciones seleccionadas.
