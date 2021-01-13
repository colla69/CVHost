module.exports = {
  publicPath: '/',
  devServer: {
    disableHostCheck: true,
    proxy: {
      '^/backend': {
        target: 'http://localhost:8999',
        pathRewrite: { '^/backend': '' },
        changeOrigin: true
      }
    }
  }
}
