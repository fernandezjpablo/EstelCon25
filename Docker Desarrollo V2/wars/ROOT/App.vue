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


        <Paso1 :formData="formData" :baseUrl="formData.baseUrl" @nextStep="handleNextStep"
          v-if="currentStep === 1 && !isLoading" ref="paso1Component" />

        <Paso2 :currentStep="currentStep" :formData="formData" :baseUrl="baseUrl" :mensaje="mensaje"
          @nextStep="handleNextStep" @prevStep="handlePrevStep" v-if="currentStep === 2" ref="paso2Component" />

        <Paso3 :currentStep=3 :formData="formData" :baseUrl="baseUrl" :mensaje="mensaje"
          @nextStep="handleNextStep" @prevStep="handlePrevStep" v-if="currentStep === 2" ref="paso3Component" />

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
        baseUrl: "http://127.0.0.1:8080",
        mensaje: "",
      },
      isDisabled: false,
      showListaEspera: false,
      isLoading: false, // Estado de carga
      showInfo: true,
      showPaso3: false,
      showPaso3Individual: false,
      showPaso3EsperaDatos: false,
      inscritos:[]
    };
  },
  watch: {
    currentStep(newStep) {
      console.log(`[WATCH] Cambio de paso: Paso ${newStep}`);
    },
  },
  methods: {
    handleNextStep(data) {
      console.log("[NEXT STEP] Datos del paso siguiente recibidos:", data);
      this.formData = { ...this.formData, ...data };
      console.log("Step ->" + this.currentStep);
      console.log("formData:", this.formData);

      if (this.currentStep === 1) {
        this.lanzarPaso2();
      } else if (this.currentStep === 2) {
        console.log("[NEXT STEP] Avanzando al paso 3...");
        console.log("comboHabitaciones:", this.formData.comboHabitaciones);
        console.log("idHabitacion:", this.formData.idhabitacion);

        const capacidad = this.formData.comboHabitaciones ? parseInt(this.formData.comboHabitaciones.split(",")[0], 10) : undefined;
        const idhabitacion = this.formData.idhabitacion;

        if (capacidad && idhabitacion) {
          this.generarPaso3(capacidad, idhabitacion);
        } else {
          console.error("Faltan datos para generar los inscritos");
        }
      } else if (this.currentStep < 4) {
        this.currentStep++;
      }
    },
    handlePrevStep() {
      if (this.currentStep > 1) {
        console.log("[PREV STEP] Retrocediendo al paso anterior.");
        this.currentStep--;
      }
    },
    inicioGrupos() {
      const respuesta = confirm(
        "Este tipo de inscripción es para habitaciones INDIVIDUALES NO COMPARTIDAS o GRUPOS COMPLETOS..."
      );
      if (respuesta) {
        console.log("[INICIO GRUPOS] Iniciando inscripción general.");
        this.lanzarPaso1();
        this.isDisabled = true;
      }
    },
    inicioIndividual() {
      const respuesta = confirm(
        "Este tipo de inscripción es para una inscripción individual en una habitación COMPARTIDA..."
      );
      if (respuesta) {
        console.log("[INICIO INDIVIDUAL] Iniciando inscripción individual.");
        this.lanzarPaso1individual();
        this.isDisabled = true;
      }
    },
    cargarComboPaso1() {
      console.log("[PASO 1] Solicitando habitaciones disponibles...");
      this.isLoading = true;
      axios
        .get(`${this.formData.baseUrl}/Valimar/GetComboHabitacionesDisponibles`)
        .then((response) => {
          console.log("[PASO 1] Habitaciones disponibles recibidas:", response.data);
          const paso1Component = this.$refs.paso1Component;
          if (paso1Component) {
            paso1Component.$refs.paso1.innerHTML = response.data;
            paso1Component.$refs.paso1.style.display = "block";
          } else {
            console.error("[PASO 1] Componente Paso1 no encontrado.");
          }
        })
        .catch((error) => {
          console.error("[PASO 1] Error al obtener habitaciones:", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    lanzarPaso2() {
      console.log("[PASO 2] Iniciando solicitud para bloquear habitación...");
      if (!this.formData.comboHabitaciones) {
        console.error("[PASO 2] comboHabitaciones está vacío. Abortando.");
        return;
      }
      this.currentStep = 2;
      this.isLoading = true;
      axios
        .post(`${this.formData.baseUrl}/Valimar/BloquearHabitacion`, {
          capacidad: this.formData.comboHabitaciones.split(",")[0],
          camas: this.formData.comboHabitaciones.split(",")[1],
        })
        .then((response) => {
          console.log("[PASO 2] Respuesta recibida:", response.data);

          const paso2Component = this.$refs.paso2Component;

          // Verificar valores antes de proceder
          console.log("[PASO 2] Comprobando valores:");
          console.log("paso2Component:", paso2Component);
          console.log("response.data:", response.data);

          if (paso2Component && response.data && response.data.trim() !== "") {
            // Todo está bien, actualizar los datos y mostrar mensaje
            this.formData.idhabitacion = response.data;
            const mensaje = `
          <div class="alert alert-info" role="alert">
            <h4 class="alert-heading">Habitación Bloqueada</h4>
            <p>Habitación bloqueada temporalmente: <strong>${response.data}</strong>.</p>
            <hr>
            <p class="mb-0">Dispone de 10 minutos para completar la reserva.</p>
          </div>`;
            paso2Component.mostrarMensaje(mensaje);
          } else {
            // Manejo del error en caso de problema
            console.warn("[PASO 2] No se pudo bloquear habitación o datos incompletos.");
            if (
              this.$refs.paso2Component &&
              typeof this.$refs.paso2Component.mostrarError === "function"
            ) {
              this.$refs.paso2Component.mostrarError(
                "No se pudo bloquear la habitación. Inténtelo de nuevo."
              );
            } else {
              console.error(
                "[PASO 2] No se pudo encontrar el método mostrarError en el componente."
              );
            }
          }
        })
        .catch((error) => {
          console.error("[PASO 2] Error al bloquear habitación:", error);
          if (
            this.$refs.paso2Component &&
            typeof this.$refs.paso2Component.mostrarError === "function"
          ) {
            this.$refs.paso2Component.mostrarError(
              "Error al conectar con el servicio. Inténtelo más tarde."
            );
          }
        });
    },
    generarPaso3(capacidad, idhabitacion) {
      console.log(`[PASO 3] Generando paso 3 con capacidad: ${capacidad}, idHabitacion: ${idhabitacion}`);

      if (!capacidad || !idhabitacion) {
        console.error(`[PASO 3] Datos incompletos. Capacidad: ${capacidad}, idHabitacion: ${idhabitacion}. Abandonando proceso.`);
        return;
      }

      // Convertir capacidad a un número entero
      capacidad = parseInt(capacidad, 10);

      if (isNaN(capacidad)) {
        console.error(`[PASO 3] Capacidad no es un número válido: ${capacidad}. Abandonando proceso.`);
        return;
      }

      this.currentStep = 3;
      const paso3Component = this.$refs.paso3Component;

      if (!paso3Component) {
        console.error("[PASO 3] Referencia a paso3Component no encontrada.");
        return;
      }

      // Inicializar el array inscritos de manera reactiva
      console.log("Valor Inscritos -> "+this.inscritos.toString);
      this.inscritos = [];
      console.log("Current Step:"+this.currentStep)
      // Generar inscritos de manera reactiva
      for (let i = 0; i < capacidad; i++) {
        console.log(`[PASO 3] Creamos usuario: ${i}`);
        Vue.set(this.inscritos, i, {
          nombre: '',
          apellidos: '',
          pseudonimo: '',
          email: '',
          telefono: '',
          nif: '',
          aceptaCondiciones: false,
          menor: i > 0,
          conBebes: i === 0 ? false : null,
          fechaBebe: i === 0 ? '' : null,
        });
      }

      this.$nextTick(() => {
        console.log("La vista debería haberse actualizado ahora para el paso 3.");
      });
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
