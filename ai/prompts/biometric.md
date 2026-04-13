# Biometric Authentication

This prompt defines how to implement biometric auth on Android. It applies to projects using the modern BiometricPrompt API. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Fingerprint, face, iris, etc. using BiometricPrompt; compatibility, secure storage, and UX. Does not cover server-side auth flows in detail.

---

## API choice

### 1. BiometricPrompt (preferred)

- **API ≥ 28** — Use **BiometricPrompt**; supports fingerprint, face, iris.
- **Backward** — Use AndroidX Biometric (androidx.biometric:biometric) for API ≥ 23.
- **Single API** — Same code regardless of device biometric type.

### 2. Legacy (avoid for new code)

- **FingerprintManager** (API 23–27) — Fingerprint only; superseded by BiometricPrompt.
- **KeyguardManager** — For device lock state; can complement biometric.

---

## Implementation

### 1. Permission and checks

- **Permission** — No special permission in AndroidManifest for biometric.
- **Availability** — Use BiometricManager.canAuthenticate() to check support.
- **Results** — BIOMETRIC_SUCCESS, BIOMETRIC_ERROR_NONE_ENROLLED, BIOMETRIC_ERROR_NO_HARDWARE, BIOMETRIC_ERROR_HW_UNAVAILABLE.

### 2. BiometricPrompt

- **Constructor** — In Activity use BiometricPrompt(activity, executor, callback); in **Fragment** use BiometricPrompt(fragment, executor, callback) so it is tied to Fragment lifecycle and does not hold Activity after destroy.
- **Executor** — Typically ContextCompat.getMainExecutor(context).
- **Callback** — Implement BiometricPrompt.AuthenticationCallback; in onAuthenticationError distinguish recoverable vs non-recoverable; guide to device credential or cancel when needed.

### 3. PromptInfo

- **Title** — setTitle()
- **Subtitle** — setSubtitle() (optional)
- **Description** — setDescription() (optional)
- **Negative** — setNegativeButtonText() for cancel
- **Fallback** — setDeviceCredentialAllowed(true) to allow PIN/pattern/password

### 4. Authenticate

- **Start** — biometricPrompt.authenticate(promptInfo)
- **Cancel** — biometricPrompt.cancelAuthentication()

---

## Security

### 1. Keys

- **Generation** — Use KeyGenParameterSpec for keys protected by biometric.
- **Storage** — Keys in Android Keystore; use only after biometric (or device credential).
- **Usage** — Use keys for encrypt/decrypt; avoid storing sensitive data in plain form.

### 2. Data

- **Encrypt** — Encrypt sensitive data with biometric-protected keys.
- **Temporary** — After auth, keep sensitive data in memory only; clear when app goes to background.
- **Session** — Set a reasonable auth timeout; do not stay “unlocked” indefinitely.

### 3. Errors

- **Repeated failure** — Handle lockout; may need to temporarily disable biometric.
- **Hardware change** — Handle enrollment change; may need re-auth.
- **Fallback** — Always offer PIN/pattern/password as alternative.

---

## UX

### 1. Onboarding

- **First use** — Guide user to enroll biometric; clear copy.
- **Rationale** — Explain why biometric is used; stress security and convenience.
- **Fallback** — Do not force biometric; always offer another method.

### 2. Dialog

- **Consistency** — Match app branding.
- **Wording** — User-friendly; avoid jargon.
- **Icon** — Show icon for the type of biometric (fingerprint/face/etc.) when relevant.

### 3. Feedback

- **Success** — Clear visual/haptic feedback.
- **Failure** — Useful message; do not leak security details.
- **Progress** — Indicate that auth is in progress.

---

## Compatibility

### 1. API

- **API &lt; 23** — No biometric; use traditional auth.
- **API 23–27** — FingerprintManager if needed; prefer AndroidX Biometric where possible.
- **API ≥ 28** — BiometricPrompt for best experience.

### 2. Device

- **No hardware** — Fall back to device credential or other auth.
- **Not enrolled** — Guide user to system settings to enroll.
- **Multiple** — Support user preference when device has more than one type.

---

## Quality checklist (self-check or code review)

- [ ] Is AndroidX Biometric used for backward compatibility?
- [ ] Are all canAuthenticate() results handled?
- [ ] Is a fallback auth method provided?
- [ ] Is sensitive data encrypted with biometric-protected keys?
- [ ] Is sensitive data cleared when app goes to background?
- [ ] Is dialog copy user-friendly?
- [ ] Are repeated failures handled?
- [ ] Are API level differences considered?
