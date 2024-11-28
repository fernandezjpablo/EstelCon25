<template>
  <div v-if="visible" :class="{ 'disabled': isDisabled }">
    <slot></slot> <!-- Esto permite que se pase contenido desde el componente principal -->
    <button :disabled="isDisabled" @click="validar">Validar</button>
    <button :disabled="isDisabled" @click="enviar">Enviar</button>
  </div>
</template>

<script>
export default {
  props: {
    visible: Boolean,
    isDisabled: Boolean
  },
  methods: {
    validar() {
      this.$emit('validar'); // Emite el evento 'validar' al componente principal
    },
    enviar() {
      this.$emit('enviar'); // Emite el evento 'enviar' al componente principal
    }
  }
};
</script>

<style scoped>
/* Estilos para este componente, por ejemplo */
.disabled {
  opacity: 0.5;
}
</style>
