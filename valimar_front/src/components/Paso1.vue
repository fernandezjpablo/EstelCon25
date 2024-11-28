<template>
  <div>
    <h2>Paso 1: Información Inicial</h2>

    <!-- Formulario con los combos y otros campos -->
    <form @submit.prevent="submitForm" novalidate>
      <div>
        <label for="combo1">Combo 1:</label>
        <select v-model="form.combo1" required>
          <option value="">Seleccione una opción</option>
          <option v-for="option in combo1Options" :key="option" :value="option">
            {{ option }}
          </option>
        </select>
        <div v-if="!form.combo1 && submitted" class="error">Este campo es obligatorio.</div>
      </div>

      <div>
        <label for="combo2">Combo 2:</label>
        <select v-model="form.combo2" required>
          <option value="">Seleccione una opción</option>
          <option v-for="option in combo2Options" :key="option" :value="option">
            {{ option }}
          </option>
        </select>
        <div v-if="!form.combo2 && submitted" class="error">Este campo es obligatorio.</div>
      </div>

      <div>
        <label for="textField">Campo de texto:</label>
        <input
          v-model="form.textField"
          type="text"
          placeholder="Ingrese texto"
          required
        />
        <div v-if="!form.textField && submitted" class="error">Este campo es obligatorio.</div>
      </div>

      <!-- Validación de la forma -->
      <button :disabled="!isFormValid" type="submit">Siguiente Paso</button>
    </form>

    <!-- Muestra un mensaje cuando el formulario ha sido enviado -->
    <div v-if="submitted" v-show="isFormValid">
      <p>Formulario enviado correctamente. Ahora puedes pasar al siguiente paso.</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        combo1: '',
        combo2: '',
        textField: '',
      },
      submitted: false,
      combo1Options: ['Opción 1', 'Opción 2', 'Opción 3'],
      combo2Options: ['Opción A', 'Opción B', 'Opción C'],
    };
  },
  computed: {
    // Computed property para verificar si el formulario es válido
    isFormValid() {
      return this.form.combo1 && this.form.combo2 && this.form.textField;
    },
  },
  methods: {
    submitForm() {
      this.submitted = true;

      // Si el formulario es válido, pasamos al siguiente paso
      if (this.isFormValid) {
        this.$emit('nextStep', this.form);  // Emite un evento para pasar al siguiente paso
      }
    },
  },
};
</script>

<style scoped>
.error {
  color: red;
  font-size: 12px;
}

button:disabled {
  background-color: #ccc;
}
</style>
