<template>
  <div class="barramenu">
    <p>Mereth Aldaron Enyali&euml; - Formulario de Inscripci&oacute;n</p>
    <div class="margin_button">
      <input type="button" id="btnGrupo" class="box_button" @click="inicioGrupos" value="Inscripci&oacute;n General" />
      <input type="button" id="btnIndividual" class="box_button" @click="inicioIndividual" value="Inscripci&oacute;n Aleatoria" />
      <input type="button" id="btnListaEspera" class="box_button" @click="inicioListaEspera" value="Apuntarse a la lista de espera" style="display:none" />
      <input type="button" class="box_button" @click="reiniciarProceso" value="Reiniciar proceso" />
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    inicioGrupos() {
      console.log('Botón "Inscripción General" presionado');
      this.$emit('inicio', 'grupos');
    },
    inicioIndividual() {
      console.log('Botón "Inscripción Aleatoria" presionado');
      this.$emit('inicio', 'individual');
    },
    inicioListaEspera() {
      console.log('Botón "Apuntarse a la lista de espera" presionado');
      this.$emit('inicio', 'listaEspera');
    },
    reiniciarProceso() {
      console.log('Botón "Reiniciar proceso" presionado');
      this.$emit('reiniciar');
    },
  },
};
</script>

<style scoped>
/* Puedes agregar aquí los estilos específicos de este componente */
</style>
