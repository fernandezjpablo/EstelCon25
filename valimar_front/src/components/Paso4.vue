<template>
  <div id="paso4">
    <h2>Formulario Paso 4 - Confirmación</h2>
    <p>¡Gracias por completar la inscripción!</p>
    <p>Aquí puedes revisar la información que has proporcionado y confirmar la inscripción.</p>
    <button @click="confirmarInscripcion">Confirmar Inscripción</button>
  </div>
</template>

<script>
export default {
  name: 'Paso4',
  methods: {
    confirmarInscripcion() {
      // Aquí iría la lógica de confirmación, como un POST a la API o mostrar mensaje de éxito.
      console.log('Inscripción confirmada');
      // Si necesitas hacer algo después, puedes emitir un evento o redirigir a otra página
    }
  }
};
</script>

<style scoped>
/* Estilos específicos para el Paso 4 */
</style>
