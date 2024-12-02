module.exports = {
  publicPath: '/ValimarFrontal/',
  devServer: {
    proxy: {
      '/Valimar': {
        target: 'http://localhost:8085',
        changeOrigin: true,
      },
    },
  },
};