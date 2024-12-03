<template>
    <div v-if="visible" :class="{ 'disabled': isDisabled }">
      <slot></slot>
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
        this.$emit('validar');
      },
      enviar() {
        this.$emit('enviar');
      }
    }
  };
  </script>
  
  <style scoped>
  .disabled {
    opacity: 0.5;
  }
  </style>