# Security & Best Practices

This prompt defines sensitive data storage, permissions, network safety, and protection against common vulnerabilities, following Android security guidance. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Local storage, network, permission requests, keys, and dependency security. Applies to any code that touches user data or system capabilities.

---

## Sensitive data storage

- **Preferred** — **EncryptedSharedPreferences** (AndroidX Security). If the project uses **MMKV**, ensure encryption is enabled (initialize with encryption key); do not use MMKV in plain-text mode for sensitive data. Do not store tokens, passwords, or payment-related data in plain SharedPreferences.
- **Keys** — Use **Android Keystore** to generate or store encryption keys. Do not hardcode keys or put them in constants that can be decompiled.
- **Logs** — In production builds do not log tokens, user IDs, phone numbers, etc.; control via BuildConfig or logging facade.

---

## Permissions

- **Runtime** — For dangerous permissions use **requestPermissions** and **rationale UI** (explain why). When the user denies and “Don’t ask again”, guide to settings or offer a fallback.
- **Least privilege** — Request only what the feature needs; avoid requesting “might need later”.
- **Result** — Handle in Activity/Fragment via onRequestPermissionsResult or Activity Result API and update UI or retry.

---

## Network and API security

- **Transport** — Use **HTTPS** only; no plain HTTP except in explicitly allowed debug setups.
- **Certificates** — In production consider **Certificate Pinning** (OkHttp CertificatePinner) to reduce MITM risk; plan pin rotation with releases.
- **Request/response** — Do not put sensitive data in URL query; do not log or attach sensitive response fields to crash reports.
- **Token** — Prefer headers; if in body, avoid mixing with logs, screenshots, or cache so it is not leaked.

---

## Common vulnerabilities

- **SQL injection** — Use **Room** or parameterized queries (? or named params); never build SQL with string concatenation.
- **XSS** — In WebView restrict allowed scheme and JavaScript; do not load untrusted HTML via loadUrl; escape or whitelist user input.
- **Unsafe deserialization** — Do not deserialize untrusted data; validate or use safe parsers for Intent/network data.
- **Exposed components** — Set `android:exported` as needed; validate action, package, and extras for components that receive external Intents to avoid malicious invocation.

---

## Dependencies and build

- **Versions** — Update libraries with known vulnerabilities (Gradle dependency checks, security advisories); avoid deprecated libraries without security updates.
- **ProGuard/R8** — Enable obfuscation in release to raise reverse-engineering cost; consider runtime decryption or server-provided values for sensitive strings.
