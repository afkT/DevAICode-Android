# Permissions (Runtime Permissions)

This prompt defines how to request runtime permissions, show rationale, and handle results in Activity/Fragment with MVVM. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Request flow, rationale UI, and result handling when dangerous permissions are needed (camera, storage, location, microphone, contacts, etc.). Follow least privilege and request-only-when-needed.

---

## When to request and least privilege

- **On demand** — Request when the user triggers a feature that needs the permission (e.g. request camera when user taps “Take photo”), not all at startup.
- **Least privilege** — Request only what the feature needs (e.g. ACCESS_COARSE_LOCATION when approximate location is enough). On targetSdk 33+ pay attention to split media permissions (READ_MEDIA_IMAGES, etc.).
- **Check first** — Before requesting use `ContextCompat.checkSelfPermission == PERMISSION_GRANTED`; if already granted, run the flow; otherwise request.
- **Manifest** — Declare needed dangerous permissions in AndroidManifest `<uses-permission android:name="..." />`; for targetSdk 33+ storage/media and 34+ background permissions, add as per official docs.

---

## Request flow

- **API** — Use `ActivityResultContracts.RequestPermission()` or `RequestMultiplePermissions()` and register an ActivityResultLauncher; handle result in the `registerForActivityResult` callback.
- **Trigger** — Call `launcher.launch(permission)` or `launch(arrayOf(...))` from the entry that needs the permission (button, init). Avoid auto-launching from Fragment/Activity onCreate unless it is a dedicated permission screen.
- **Result** — In the launcher callback use `isGranted` to continue or show “This feature needs … permission”. When all are denied, optionally guide the user to app settings (Intent to app details).

---

## Rationale (why we need the permission)

- **When** — Before requesting, if `shouldShowRequestPermissionRationale(permission)` is true, show a Dialog or Snackbar explaining why (e.g. “We need gallery access to upload your profile photo”), then launch after user confirms.
- **“Don’t ask again”** — If the user previously chose “Don’t ask again” and denied, `shouldShowRequestPermissionRationale` may be false; then only option is to guide to settings and explain “Please enable … in Settings”.

---

## ViewModel and lifecycle

- **Launcher** — ActivityResultLauncher must be registered in Activity/Fragment (registerForActivityResult must be called before or during onCreate); do not hold the launcher in ViewModel.
- **Callback** — In the launcher callback call ViewModel (e.g. `viewModel.onPermissionResult(granted)`); ViewModel updates state or triggers next step. Keep business logic in ViewModel, not in the callback.
- **Configuration change** — Launcher survives rotation; avoid holding references to Views that may be destroyed in the callback; use ViewModel or check lifecycle before updating UI.
