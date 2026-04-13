# WebView (In-App Web and Hybrid)

This prompt defines WebView security, JavaScript interface, native–web communication, and performance. It applies to apps that embed H5, hybrid pages, or need two-way communication with web. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Loading web or local HTML in **WebView**; **addJavascriptInterface** for JS↔Native; certificate handling, file picker, download, and division of work between hybrid and native. Does not cover server-side rendering or full PWA; only client WebView usage.

---

## Security

- **HTTPS** — In production load only HTTPS or `file://` (app-owned resources). Do not load untrusted HTTP without validation. Set WebSettings.mixedContentMode as needed; avoid insecure downgrade.
- **Capabilities** — Disable what you do not need: setJavaScriptEnabled only when JS is required; turn off setAllowFileAccess, setAllowContentAccess when not needed. Do not load untrusted HTML via loadDataWithBaseURL with an arbitrary baseUrl (scheme/path bypass).
- **XSS** — Do not inject user input into HTML/JS without escaping or whitelist. If using evaluateJavascript for dynamic script, only run controlled content. Apply CSP or server-side filtering for server HTML.
- **Exposed components** — If the Activity that hosts WebView receives external links via intent-filter, validate URL host/path against a whitelist to avoid opening arbitrary URLs (phishing, malicious script).

---

## JavaScript interface (Native ↔ JS)

- **Annotation** — Methods exposed to JS must have **@JavascriptInterface** and run off the main thread (system requirement). Do not do heavy work or touch View directly in that method; post to main thread if needed.
- **Types** — Pass only serializable, simple types (String, primitives, JSON string). Do not pass Context, View, etc. Return JSON or an agreed string format to JS.
- **Naming** — Use a clear object name (e.g. AndroidBridge) to avoid global clashes; align method names with JS. If the interface can be called from any loaded page, validate origin (e.g. only from allowed domain).
- **Cleanup** — In Activity/Fragment onDestroy call webView.removeJavascriptInterface("xxx") and stop loading so WebView does not hold the Activity and leak.

---

## Certificates and SSL

- **Default** — Do not override onReceivedSslError to ignore errors and continue. Only for self-signed or special certs (e.g. enterprise) use a controlled setup with pinning or a limited whitelist and log.
- **Client cert** — For mutual TLS use WebViewClient.shouldInterceptRequest or custom WebResourceResponse to inject; read keys from Keystore securely.

---

## Lifecycle and memory

- **Binding** — Tie WebView to Activity/Fragment lifecycle. In onDestroy call webView.stopLoading(), webView.destroy(). In Fragment do this in onDestroyView and remove from layout before destroy to avoid double destroy.
- **Process** — WebView can spawn a separate process; avoid creating many WebView instances in one process; consider reuse or pooling per product needs.
- **Cache** — Set WebSettings.setCacheMode as needed; when clearing use WebView.clearCache(), CookieManager, etc.; clear cookies/cache on logout.

---

## Division of responsibility (Native vs Web)

- **Navigation** — When H5 navigates in-app, use JS interface to call native routing (e.g. AndroidBridge.navigate(path)); native handles with Navigation/Intent; avoid embedding complex native UI inside WebView.
- **Data** — Sensitive data (e.g. token) is injected by native or passed via a secure channel to H5; do not pass via URL or untrusted page. When H5 submits or reports, validate and sign on native side.
- **Capabilities** — File picker, camera, location, etc. that need permissions: JS asks native via interface; native requests permission and returns result; do not expose system APIs directly from WebView.

---

## Performance and UX

- **First paint** — Load main document and critical resources first; e.g. setBlockNetworkImage(false) as needed; lazy-load large images/scripts or optimize on server.
- **Hardware acceleration** — On by default; if rendering is wrong, try setLayerType on a single WebView for debugging, not globally off.
- **Debug** — Use WebView.setWebContentsDebuggingEnabled(true) only in debug with Chrome remote debugging; disable in release.

---

## Quality checklist (self-check or code review)

- [ ] Is only HTTPS or controlled file/baseUrl loaded; is untrusted HTML avoided?
- [ ] Are JS interfaces minimal and parameter types controlled; are they removed in onDestroy?
- [ ] Is default SSL error handling not ignored (except controlled cases with logging)?
- [ ] Are WebView stopLoading and destroy called at the right time to avoid leaks?
- [ ] Is the boundary between hybrid and native clear for navigation, data, and permissions?
