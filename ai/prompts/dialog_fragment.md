# Dialog & DialogFragment

This prompt defines how to implement dialogs with DialogFragment, AlertDialog, and custom dialogs: lifecycle and result callbacks. It applies to Fragment and MVVM. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Confirmation, single/multi-select, input, custom-layout dialogs; dialogs that must survive configuration change or return a result. Does not cover Toast, Snackbar (drive those from ViewModel events).

---

## DialogFragment basics

- **When** — Use **DialogFragment** when the dialog has state, must be restorable, or must deliver a result. One-off AlertDialogs can be built in Activity/Fragment but are lost on rotation; prefer DialogFragment for important dialogs.
- **Subclass** — Extend **DialogFragment**; override `onCreateDialog()` to return a `Dialog` (e.g. `AlertDialog.Builder(...).create()`) or a custom Dialog; or override `onCreateView()` for a custom layout (then typically use Dialog + setContentView or inflated View, not AlertDialog).
- **Theme/style** — Call `setStyle(STYLE_NORMAL | STYLE_NO_TITLE | STYLE_NO_FRAME | STYLE_NO_INPUT, R.style.Xxx)` in `onCreate`; define style in themes (background, corner, width).
- **Show** — `show(supportFragmentManager, tag)` or `show(parentFragmentManager, tag)`. Use a tag so you can `findFragmentByTag` or dismiss by tag; avoid showing without a tag.

---

## Lifecycle and configuration change

- **Lifecycle** — DialogFragment follows Fragment lifecycle; `onCreateDialog` runs after `onCreate`. On dismiss, `onDismiss`, `onDestroyView`, etc. run; in `onDestroyView` unbind listeners and clear View references to avoid leaks.
- **Configuration change** — DialogFragment is recreated on rotation; if `onCreateDialog` depends on Activity/Fragment, they may not be attached yet. Restore state via `savedInstanceState` or a ViewModel if the dialog has one.
- **dismiss** — Call `dismiss()` or `dismissAllowingStateLoss()`; prefer sending the result before dismiss so the result is not lost.

---

## Result callbacks

- **Fragment Result API (preferred)** — Use `setFragmentResult(requestKey, bundle)` to send; target uses `setFragmentResultListener(requestKey, viewLifecycleOwner) { key, bundle -> ... }`. Tied to lifecycle; no deprecated APIs.
- **Legacy (deprecated)** — `setTargetFragment` is deprecated since API 28; use only when maintaining existing code that already relies on it. Do not use in new code.
- **ViewModel / shared state** — If the dialog shares a ViewModel or scope with the host, use LiveData/StateFlow to pass “user selection”; dialog updates state, host observes. Good for in-page dialogs.
- **One-shot** — Consume the result once; Result API listener fires once. With LiveData avoid duplicate triggers; use Channel or SingleLiveEvent pattern if needed.

---

## AlertDialog and Builder

- **AlertDialog** — `AlertDialog.Builder(context).setTitle(...).setMessage(...).setPositiveButton(...).setNegativeButton(...).create()`; return this from DialogFragment’s `onCreateDialog`.
- **Buttons** — Listeners for setPositiveButton/setNegativeButton/setNeutralButton run on click; in the listener build result, callback or setFragmentResult, then dismiss.
- **Lists** — setSingleChoiceItems/setMultiChoiceItems; in callback get selection and pass via Result API or interface.
- **Custom view** — setView(inflated View); watch IME, focus, padding; for complex forms consider custom Dialog or full-screen DialogFragment with full layout.

---

## Boundary with ViewModel

- **In dialog** — Simple choices/confirmations can stay in DialogFragment. If the dialog does network or heavy validation, give it a ViewModel (activityViewModels or viewModels) or pass result to host and let host ViewModel handle it.
- **Host** — Host Fragment/Activity does not hold the Dialog reference; after receiving result via Result API or interface, host ViewModel updates state or runs business. Keep “dialog only collects input; business in ViewModel”.
