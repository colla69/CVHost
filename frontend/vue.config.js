module.exports = {
  outputDir: "target/dist",
  assetsDir: "static",
  publicPath: '/',
  devServer: {
    disableHostCheck: true,
    proxy: {
      '^/backend': {
        target: 'http://localhost:8999/backend/',
        pathRewrite: { '^/backend': '' },
        changeOrigin: true
      }
    }
  }
}
