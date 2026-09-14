// CloudFront Function, viewer-request, runtime cloudfront-js-2.0.
//
// vue-router runs in createWebHistory mode, so /qualifications is a real URL
// that S3 has no object for. Without this the edge returns S3's 403 and a
// refresh or a shared deep link dies -- which is exactly the bug the Strato
// nginx has today.
//
// Only extensionless paths are rewritten. A genuinely missing /data/foo.pdf
// still 404s instead of being answered with the SPA shell and a misleading
// 200, which is what a blanket 403-to-index.html error page would do.
function handler(event) {
  var request = event.request
  var uri = request.uri

  if (uri.endsWith('/')) {
    request.uri = '/index.html'
    return request
  }

  var last = uri.substring(uri.lastIndexOf('/') + 1)
  if (last.indexOf('.') === -1) {
    request.uri = '/index.html'
  }

  return request
}
