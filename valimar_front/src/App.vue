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
          <button
            class="btn btn-primary"
            @click="inicioGrupos"
            :disabled="isDisabled"
            :style="{ cursor: isDisabled ? 'not-allowed' : 'pointer' }"
          >
            Inscripci&oacute;n General
          </button>
          <button
            class="btn btn-secondary"
            @click="inicioIndividual"
            :disabled="isDisabled"
            :style="{ cursor: isDisabled ? 'not-allowed' : 'pointer' }"
          >
            Inscripci&oacute;n Aleatoria
          </button>
          <button
            class="btn btn-warning"
            @click="inicioListaEspera"
            v-show="showListaEspera"
          >
            Apuntarse a la lista de espera
          </button>
          <button class="btn btn-danger" @click="reiniciarProceso">
            Reiniciar proceso
          </button>
        </div>
      </div>
      <div class="main-content">
        <Paso1 :formData="formData" :baseUrl="formData.baseUrl" @nextStep="handleNextStep" v-if="currentStep === 1" ref="paso1Component" />
        <Paso2 :formData="formData" :baseUrl="formData.baseUrl" @nextStep="handleNextStep" @prevStep="handlePrevStep" v-if="currentStep === 2" ref="paso2Component" />
        <Paso3
          :formData="formData"
          @nextStep="handleNextStep"
          @prevStep="handlePrevStep"
          v-if="currentStep === 3"
        />
        <Paso4
          :formData="formData"
          @prevStep="handlePrevStep"
          @submitForm="handleSubmit"
          v-if="currentStep === 4"
        />
        <ControlComponent
          :visible="true"
          :isDisabled="isDisabled"
          @validar="handleValidar"
          @enviar="handleEnviar"
        />
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
      },
      isDisabled: false,
      showListaEspera: false,
    };
  },
  methods: {
    handleNextStep(data) {
      console.log("Datos del siguiente paso:", data);
      this.formData = { ...this.formData, ...data };
      if (this.currentStep < 4) {
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
      const capacidad = this.formData.comboHabitaciones.split(',')[0];
      const camas = this.formData.comboHabitaciones.split(',')[1];

      axios.post(`${this.formData.baseUrl}/Valimar/BloquearHabitacion`, {
        capacidad: capacidad,
        camas: camas
      })
      .then((response) => {
        const paso2Component = this.$refs.paso2Component;
        if (paso2Component) {
          if (response.data) {
            this.formData.idhabitacion = response.data;
            paso2Component.$refs.paso2.innerHTML = `Tiene bloqueada temporalmente la habitación ${response.data}. Dispone de 10 minutos para formalizar la reserva antes de que la habitación vuelva a liberarse.`;
            this.currentStep = 2;
          } else {
            paso2Component.$refs.paso2.innerHTML = "Las habitaciones de esta capacidad ya no están disponibles.";
          }
        } else {
          console.error("El componente Paso2 no está disponible.");
        }
      })
      .catch((error) => {
        console.error("Error al bloquear la habitación:", error);
      });
    },
    reiniciarProceso() {
      this.currentStep = 1;
      this.isDisabled = false;
      console.log("Reiniciar proceso");
    },
    enviarInscripcion() {
      const lista = []; // Define y llena esta lista según tus datos
      axios
        .post(this.formData.baseUrl + "/Valimar/RegistrarInscripcion", {
          habitacion: this.formData.idhabitacion,
          datos: JSON.stringify(lista),
        })
        .then((response) => {
          console.log("Inscripción registrada:", response.data);
        })
        .catch((error) => {
          console.error("Error al enviar la inscripción:", error);
        });
    },
  },
};
</script>

<style>
body {
  font-family: "Roboto", sans-serif;
}

.header,
.footer {
  background-color: #343a40;
  color: white;
  padding: 10px 0;
}

.header h1,
.footer p {
  margin: 0;
}

.barramenu {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.margin_button button {
  margin: 5px;
}

.main-content {
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>
