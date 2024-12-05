<template>
  <div>
    <h3>Paso 4: Revisión de los Inscritos</h3>

    <!-- Mostrar resumen de los inscritos -->
    <div v-for="(inscrito, index) in inscritos" :key="index" class="card my-3">
      <div class="card-body">
        <h5 class="card-title">Inscrito {{ index + 1 }}</h5>

        <!-- Resumen de los datos del inscrito -->
        <p><strong>Nombre:</strong> {{ inscrito.nombre }}</p>
        <p>
          <strong>Pseudónimo:</strong>
          {{ inscrito.pseudonimo || "No proporcionado" }}
        </p>
        <p><strong>Apellidos:</strong> {{ inscrito.apellidos }}</p>
        <p><strong>Email:</strong> {{ inscrito.email }}</p>
        <p><strong>Teléfono:</strong> {{ inscrito.telefono }}</p>
        <p><strong>NIF/NIE/Pasaporte:</strong> {{ inscrito.nif }}</p>

        <!-- Datos del menor de 2 años si aplica -->
        <div v-if="inscrito.menorDeDos">
          <h6>Datos del menor de 2 años:</h6>
          <p><strong>Nombre:</strong> {{ inscrito.menor.nombre }}</p>
          <p>
            <strong>Año de Nacimiento:</strong> {{ inscrito.anioNacimiento }}
          </p>
        </div>

        <!-- Confirmación de aceptación de condiciones -->
        <p v-if="inscrito.aceptaCondiciones">
          <strong>Acepta las condiciones</strong>
        </p>

        <p v-if="!inscrito.aceptaCondiciones && !inscrito.menorDeDos">
          <strong>No ha aceptado las condiciones</strong>
        </p>
      </div>
      <!-- Paso 4 del formulario -->
      <div id="registro">
        <!-- Aquí van los campos de datos que ya tienes, por ejemplo, los nombres de los inscritos, habitaciones, etc. -->
        <button
          type="button"
          @click="registrarInscripcion"
          class="btn btn-primary"
        >
          REGISTRAR
        </button>
      </div>
    </div>

    <!-- Botón de retroceso al Paso 3 -->
    <button class="btn btn-secondary mb-3" @click="previousStep">
      Regresar a Paso 3
    </button>

    <!-- Botón para finalizar inscripción -->
    <button class="btn btn-success" @click="submitForm">
      Finalizar Inscripción
    </button>
  </div>
</template>

<script>
import axios from "axios"; // Asegúrate de tener axios instalado

export default {
  name: "Paso4",
  props: {
    inscritos: {
      type: Array,
      required: true,
    },
    habitacion: {
      type: String,
      required: true,
    },
  },
  methods: {
    previousStep() {
      this.$emit("previousStep", 3); // Regresar al paso 3
    },

    async registrarInscripcion() {
      try {
        // Recopilar los datos de los inscritos
        const datosInscritos = this.inscritos.map((inscrito) => ({
          iden: inscrito.nif,
          nombre: inscrito.nombre,
          apellidos: inscrito.apellidos,
          telefono: inscrito.telefono,
          email: inscrito.email,
          pseudonimo: inscrito.pseudonimo,
          menor: inscrito.menorDeDos,
          anioNacimiento: inscrito.anioNacimiento,
          aceptaCondiciones: inscrito.aceptaCondiciones,
        }));
console.log(datosInscritos);

        // Hacer la solicitud POST con axios
        const response = await axios.post("/Valimar/RegistrarInscripcion", {
          datos: JSON.stringify(datosInscritos), // Enviamos los datos como string JSON
          habitacion: this.habitacion, // Enviamos la habitación seleccionada
        });

        // Manejar la respuesta del servidor
        alert("Inscripción registrada con éxito: " + response.data);
      } catch (error) {
        console.error("Error al registrar la inscripción:", error);
        alert(
          "Hubo un error al registrar la inscripción. Por favor, intente nuevamente."
        );
      }
    },
    submitForm() {
      // Aquí puedes manejar el envío del formulario, por ejemplo, mediante una API
      alert("Formulario enviado con éxito.");
      // Emite el evento para indicar que el proceso ha finalizado.
      this.$emit("finish");
    },
  },
};
</script>

<style scoped>
/* Puedes agregar estilos personalizados aquí */
</style>
