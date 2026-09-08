module.exports = {
  assetsDir: 'static',
  publicPath: '/',
  devServer: {
    allowedHosts: 'all'
  },
  css: {
    loaderOptions: {
      css: {
        // Root-relative urls point at public/ (fonts, images) and are served
        // as-is. Without this css-loader tries to resolve them through webpack
        // and fails. Relative urls still go through the normal asset pipeline.
        url: {
          filter: url => !url.startsWith('/')
        }
      }
    }
  }
}
