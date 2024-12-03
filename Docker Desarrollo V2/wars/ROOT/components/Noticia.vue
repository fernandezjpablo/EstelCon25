<template>
  <div id="noticias">
    <div v-for="(noticia, index) in noticias" :key="index">
      <div>
        <span>{{ noticia.fecha }}</span>
        <span @click="toggleNoticia(noticia.id)">{{ noticia.titulo }}</span>
      </div>
      <div v-if="noticia.visible">
        <a :href="'viewpost.html?id=' + noticia.id" target="_blank">Abrir en otra ventana</a>
        <p>{{ noticia.cuerpo }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Noticia',
  data() {
    return {
      noticias: []
    };
  },
  mounted() {
    this.cargarListado();
  },
  methods: {
    cargarListado() {
      // Simulamos una carga de noticias
      this.noticias = [
        { id: 1, fecha: '2024-11-28', titulo: 'Noticia 1', cuerpo: 'Contenido de la noticia 1', visible: false },
        { id: 2, fecha: '2024-11-29', titulo: 'Noticia 2', cuerpo: 'Contenido de la noticia 2', visible: false }
      ];
    },
    toggleNoticia(id) {
      const noticia = this.noticias.find(n => n.id === id);
      if (noticia) {
        noticia.visible = !noticia.visible;
      }
    }
  }
};
</script>

<style scoped>
/* Estilos específicos para las noticias */
</style>
