// CloudFront Function, viewer-request, runtime cloudfront-js-2.0.
//
// Two jobs, in this order, because a cache behaviour takes only one
// viewer-request function:
//
// 1. Redirect. The apex and www have no content of their own: they answer with a
//    301 to the CV's hostname, keeping the path, so there is one canonical URL --
//    the one printed on the CV. Only the hosts listed in REDIRECT_HOSTS redirect,
//    so the distribution's own *.cloudfront.net name keeps serving the site, which
//    is how a fresh build gets checked before a cutover. The query string is
//    dropped; nothing on this site reads one.
//
// 2. SPA fallback. vue-router runs in createWebHistory mode, so /qualifications
//    is a real URL that S3 has no object for. Without this the edge returns S3's
//    403 and a refresh or a shared deep link dies -- which is exactly the bug the
//    Strato nginx had.
//
//    Only extensionless paths are rewritten. A genuinely missing /data/foo.pdf
//    still fails instead of being answered with the SPA shell and a misleading
//    200, which is what a blanket 403-to-index.html error page would do.
//
// SiteStack fills in both placeholders at synth time, from bin/cvhost.ts, and
// refuses to synth if either is left in.
var SITE_DOMAIN = '__SITE_DOMAIN__'
var REDIRECT_HOSTS = __REDIRECT_HOSTS__

function handler(event) {
  var request = event.request
  var host = request.headers.host ? request.headers.host.value.toLowerCase() : ''

  if (REDIRECT_HOSTS.indexOf(host) !== -1) {
    return {
      statusCode: 301,
      statusDescription: 'Moved Permanently',
      headers: { location: { value: 'https://' + SITE_DOMAIN + request.uri } }
    }
  }

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
