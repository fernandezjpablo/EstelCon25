module.exports = {
  publicPath: '/ValimarFrontal/',
  devServer: {
    proxy: {
      '/Valimar': {
        target: 'http://10.178.169.94:8080/',
        changeOrigin: true,
      },
    },
  },
};