<template>
  <div id="paso3">
    <h2>Formulario Paso 3</h2>
    <div v-for="(persona, index) in personas" :key="index">
      <input v-model="persona.nombre" placeholder="Nombre" />
      <input v-model="persona.apellidos" placeholder="Apellidos" />
      <input v-model="persona.email" placeholder="Email" />
      <!-- Resto de campos -->
    </div>
    <button @click="enviarInscripcion">Enviar Inscripción</button>
  </div>
</template>

<script>
export default {
  name: 'Paso3',
  props: ['personas'],
  methods: {
    enviarInscripcion() {
      // Aquí envías los datos y decides qué hacer después
      this.$emit('pasarAlPaso4');
    }
  }
};
</script>

<style scoped>
/* Estilos específicos para el Paso 3 */
</style>
