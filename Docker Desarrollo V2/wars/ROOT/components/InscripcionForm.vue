<template>
  <div class="container mt-4">
    <h1 class="text-center">Formulario de Inscripción</h1>
    <div class="d-flex justify-content-center">
      <button class="btn btn-primary" @click="inscripcionGeneral">Inscripción General</button>
      <button class="btn btn-success ms-2" @click="inscripcionAleatoria">Inscripción Aleatoria</button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    inscripcionGeneral() {
      alert("Inscripción General");
    },
    inscripcionAleatoria() {
      alert("Inscripción Aleatoria");
    }
  }
};
</script>

<style scoped>
  .container {
    max-width: 600px;
  }
</style>
