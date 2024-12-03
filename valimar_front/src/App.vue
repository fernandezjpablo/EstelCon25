<template>
  <div>
    <header class="header text-center">
      <div class="container">
        <h1>Formulario de Varias Etapas</h1>
      </div>
    </header>
    <main class="container mt-5">
      <div class="barramenu mb-4">
        <p>Mereth Aldaron Enyali&euml; - Formulario de Inscripci&oacute;n</p>
        <div class="margin_button d-flex justify-content-around">
          <button class="btn btn-primary" @click="inicioGrupos" :disabled="isDisabled"
            :style="{ cursor: isDisabled ? 'not-allowed' : 'pointer' }">
            Inscripci&oacute;n General
          </button>
          <button class="btn btn-secondary" @click="inicioIndividual" :disabled="isDisabled"
            :style="{ cursor: isDisabled ? 'not-allowed' : 'pointer' }">
            Inscripci&oacute;n Aleatoria
          </button>
          <button class="btn btn-warning" @click="inicioListaEspera" v-show="showListaEspera">
            Apuntarse a la lista de espera
          </button>
          <button class="btn btn-danger" @click="reiniciarProceso">
            Reiniciar proceso
          </button>
        </div>
      </div>
      <div class="main-content">
        <div v-if="isLoading" class="spinner">
          <div class="spinner-container">
            <div class="spinner-circle"></div>
            <div class="spinner-circle"></div>
            <div class="spinner-circle"></div>
            <div class="spinner-circle"></div>
          </div>
          <p>Buscando en el Pony Pisador...</p>
        </div>
        <Paso1 :formData="formData" :baseUrl="formData.baseUrl" @nextStep="handleNextStep"
          v-if="currentStep === 1 && !isLoading" ref="paso1Component" />
        <Paso2 :currentStep="currentStep" :formData="formData" :baseUrl="baseUrl" :mensaje="mensaje"
          v-if="currentStep === 2 " ref="paso2Component" />
        <Paso3 :formData="formData" @nextStep="handleNextStep" @prevStep="handlePrevStep"
          v-if="currentStep === 3 " ref="paso3Component" />
        <Paso4 :formData="formData" @prevStep="handlePrevStep" @submitForm="handleSubmit"
          v-if="currentStep === 4 && !isLoading" />
      </div>
    </main>
    <footer class="footer text-center mt-5">
      <div class="container">
        <p>&copy; 2024 Formulario de Varias Etapas. Todos los derechos reservados.</p>
      </div>
    </footer>
  </div>
</template>

<script>
import Paso1 from "./components/Paso1.vue";
import Paso2 from "./components/Paso2.vue";
import Paso3 from "./components/Paso3.vue";
import Paso4 from "./components/Paso4.vue";
import ControlComponent from "./components/ControlComponent.vue";
import axios from "axios";

export default {
  name: "App",
  components: {
    Paso1,
    Paso2,
    Paso3,
    Paso4,
    ControlComponent,
  },
  data() {
    return {
      currentStep: 1,
      formData: {
        comboHabitaciones: "",
        idhabitacion: "",
        baseUrl: "http://localhost:8080",
        mensaje: ""
      },
      isDisabled: false,
      showListaEspera: false,
      isLoading: false, // Estado de carga
      showInfo: true,
      showPaso3: false,
      showPaso3Individual: false,
      showPaso3EsperaDatos: false,
    };
  },
  watch: {
    // Observa los cambios en 'currentStep' y muestra el nuevo valor en consola
    currentStep(newStep) {
      console.log("Cambio de paso a:", newStep);
    },
  },
  computed: {
    cursorStyle() {
      return this.isDisabled ? 'cursor: not-allowed;' : 'cursor: pointer;';
    },
  },
  methods: {
    handleNextStep(data) {
      console.log("Datos del siguiente paso:", data);
      this.formData = { ...this.formData, ...data };
      if (this.currentStep === 1) {
        console.log("Avanzando al paso 2");
        console.log("Lanzo paso 2");
        this.lanzarPaso2();
      } else if (this.currentStep === 2) {
        console.log("Avanzando al paso 3");
        this.currentStep++;
        this.generarPaso3(
          this.formData.comboHabitaciones.split(",")[0],
          this.formData.idhabitacion
        );
      } else if (this.currentStep < 4) {
        this.currentStep++;
      }
    },
    handlePrevStep() {
      if (this.currentStep > 1) {
        this.currentStep--;
      }
    },
    handleValidar() {
      console.log("Validar");
    },
    handleEnviar() {
      console.log("Enviar");
    },
    handleSubmit() {
      console.log("Formulario enviado", this.formData);
    },
    inicioGrupos() {
      const respuesta = confirm(
        "Este tipo de inscripción es para habitaciones INDIVIDUALES NO COMPARTIDAS o GRUPOS COMPLETOS. Necesitarás TODOS LOS DATOS de los ocupantes. Si es lo que quieres pulsa Aceptar para continuar."
      );
      if (respuesta) {
        this.lanzarPaso1();
        this.isDisabled = true;
      }
    },
    inicioIndividual() {
      const respuesta = confirm(
        "Este tipo de inscripción es para una inscripción individual en una habitación COMPARTIDA con compañer@s seleccionados aleatoriamente. Si es lo que quieres pulsa Aceptar para continuar."
      );
      if (respuesta) {
        this.lanzarPaso1individual();
        this.isDisabled = true;
      }
    },
    inicioListaEspera() {
      this.lanzarPaso1ListaEspera();
      this.isDisabled = true;
    },
    lanzarPaso1() {
      this.cargarComboPaso1();
    },
    cargarComboPaso1() {
      axios
        .get(this.formData.baseUrl + "/Valimar/GetComboHabitacionesDisponibles")
        .then((response) => {
          console.log(this.$refs);
          const paso1Component = this.$refs.paso1Component;
          if (paso1Component) {
            paso1Component.$refs.paso1.innerHTML = response.data; // Usa $refs para acceder al div
            paso1Component.$refs.paso1.style.display = "block"; // Muestra el div
          } else {
            console.error("El componente Paso1 no está disponible.");
          }
        })
        .catch((error) => {
          console.error("Error al cargar las habitaciones disponibles:", error);
        });
    },
    lanzarPaso1individual() {
      // Lógica para lanzar el paso 1 de inscripción individual
      console.log("Lanzar paso 1 individual");
    },
    lanzarPaso1ListaEspera() {
      // Lógica para lanzar el paso 1 de lista de espera
      console.log("Lanzar paso 1 lista de espera");
    },
    lanzarPaso2() {
      console.log("Antes de cambiar currentStep:", this.currentStep); // Log antes de actualizar
      this.currentStep = 2;
      console.log("Después de cambiar currentStep:", this.currentStep); // Log después de actualizar

      const capacidad = this.formData.comboHabitaciones.split(",")[0];
      const camas = this.formData.comboHabitaciones.split(",")[1];
      console.log(`Se llama al servicio de bloquear ${this.formData.baseUrl}/Valimar/BloquearHabitacion`);

      this.isLoading = true; // Inicia el estado de carga

      axios
        .post(`${this.formData.baseUrl}/Valimar/BloquearHabitacion`, {
          capacidad: capacidad,
          camas: camas,
        })
        .then((response) => {
          console.log(this.$refs);
          const paso2Component = this.$refs.paso2Component;
          console.log("paso 2 comp. -> " + this.$refs); // Verifica que la referencia está siendo asignada correctamente
          console.log(this.$refs);

          if (paso2Component) {
            // Verifica si hay respuesta del servicio de bloqueo
            if (response.data) {
              this.formData.idhabitacion = response.data;
              console.log("Preparando mensaje a Paso2");
              const mensaje = `
                <div class="alert alert-info" role="alert">
                  <h4 class="alert-heading">Habitación Bloqueada</h4>
                  <p>Tiene bloqueada temporalmente la habitación <strong>${response.data}</strong>.</p>
                  <hr>
                  <p class="mb-0">Dispone de 10 minutos para formalizar la reserva antes de que la habitación vuelva a liberarse.</p>
                </div>
              `;
              paso2Component.mostrarMensaje(mensaje); // Enviar el mensaje al componente Paso2
              console.log("Mensaje enviado a Paso2");
              this.generarPaso3(capacidad, response.data); // Llamar a generarPaso3
            } else {

              const mensajeError = "Las habitaciones de esta capacidad ya no están disponibles.";
              paso2Component.mostrarError(mensajeError); // Mostrar error en el Paso2
            }
          } else {
            console.error("El componente Paso2 no está disponible. CurrentStepL: "+this.currentStep+" IsLoading: "+isLo);
          }
        })
        .catch((error) => {
          console.error("Error al bloquear la habitación:", error);
        })
        .finally(() => {
          this.isLoading = false; // Finaliza el estado de carga
        });
    },

    generarPaso3(capacidad, idhabitacion) {
      console.log("Generar paso 3 con capacidad:", capacidad, "y idhabitacion:", idhabitacion);

      const elemDatos = document.getElementById("paso3Datos");
      // Limpia el contenido previo
      elemDatos.innerHTML = "";

      // Genera los campos de inscripción para cada ocupante según la capacidad
      for (let i = 0; i < capacidad; i++) {
        const divInscrito = document.createElement("div");
        let lineaMenor = "";

        if (i > 0) {
          lineaMenor = " Menor entre 2 y 12 años";
        }

        if (i === 0) {
          lineaMenor = " Voy con menor/es de 2 años (no ocupan plaza)";
          lineaMenor += "\n(Año de nacimiento del menor)2022 2023 2024";
        }

        divInscrito.innerHTML = `
          <div>
            <h4>Datos del inscrito número ${i + 1}</h4>
            <label>Nombre: *</label>
            <input type="text" name="nombre${i}" required>
            <label>Apellidos: *</label>
            <input type="text" name="apellidos${i}" required>
            <label>Pseudónimo (opcional):</label>
            <input type="text" name="pseudonimo${i}">
            <label>Email: *</label>
            <input type="email" name="email${i}" required>
            <label>Teléfono: *</label>
            <input type="tel" name="telefono${i}" required>
            <label>NIF/NIE/Pasaporte: *</label>
            <input type="text" name="nif${i}" required>
            <label>
              <input type="checkbox" name="condiciones${i}" required>
              He leído y acepto las condiciones de inscripción al evento (incluidas las referentes a uso de mi imagen)*
            </label>
            ${lineaMenor}
          </div>
        `;
        elemDatos.appendChild(divInscrito);
      }

      this.showPaso3 = true;
    },
  },
};
</script>

<style scoped>
.spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
}

.spinner-container {
  display: flex;
  justify-content: space-around;
  width: 50px;
}

.spinner-circle {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #333;
  animation: bounce 1.5s infinite ease-in-out;
}

.spinner-circle:nth-child(2) {
  animation-delay: -0.3s;
}

.spinner-circle:nth-child(3) {
  animation-delay: -0.6s;
}

@keyframes bounce {

  0%,
  80%,
  100% {
    transform: scale(0);
  }

  40% {
    transform: scale(1);
  }
}
</style>