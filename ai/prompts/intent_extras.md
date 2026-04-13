# Intent & Extras (Screen-to-Screen Data)

This prompt defines how to pass parameters between Activity/Fragment via Intent and Bundle, which types to use, and boundaries. It applies to projects that follow a single source of truth. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Passing launch or one-off data between screens with startActivity, Intent, Bundle, arguments. Does not replace Navigation Safe Args when Safe Args is used; use when not using Navigation or when compatible with legacy or external callers.

---

## How to pass and types

- **Intent extras** — intent.putExtra(key, value), intent.getSerializableExtra(key), etc. Supports primitives, String, Parcelable, Serializable, Bundle, String[], ArrayList&lt;String&gt;.
- **Fragment arguments** — fragment.arguments = bundle or Fragment().apply { arguments = Bundle().apply { putXxx(...) } }; in Fragment use arguments?.getXxx(key) or requireArguments(). Same Binder size limit (~1MB) as Intent.
- **Parcelable** — Preferred on Android; faster than Serializable. Implement Parcelable or @Parcelize. Pass only what the target screen needs; avoid large or deeply nested objects.
- **Serializable** — Use when Parcelable is inconvenient or for compatibility; higher cost; still avoid large objects.
- **Pass ID only** — For detail/edit screens that only need “which record”, pass id (or type + id); target loads full data from Repository by id so there is a single source of truth and Intent does not go out of sync.

---

## Keys and type safety

- **Key constants** — Define keys in constants or companion (e.g. const val EXTRA_USER_ID = "user_id"); avoid typos and keep sender/receiver in sync.
- **Null and default** — getXxxExtra(key) can be null; use getLongExtra(key, -1L) etc. or validate null/invalid in target and return early or show message.
- **Type match** — Write and read types must match; wrong type causes runtime error or crash. In Kotlin use getSerializableExtra(key) as? Type and null-check.

---

## Size and boundaries

- **Binder limit** — Intent/Bundle go through Binder; large payloads (e.g. big Bitmap, long list) can cause TransactionTooLargeException. Pass only keys or ids; large content via Repository, cache, or in-process singleton.
- **Lifecycle** — Intent and arguments are available at creation; do not pass “expiring” references (Activity, Fragment); pass data only.
- **Security** — Do not put sensitive plaintext (e.g. password) in Intent; if passing sensitive id, be careful with FLAG and exported so other apps cannot read.

---

## With ViewModel

- **When to read** — Read in ViewModel init or Factory from SavedStateHandle (extras and arguments are merged there), or in Activity/Fragment onCreate/onCreateView and pass to ViewModel; avoid getExtra scattered in the View layer.
- **One-shot** — Launch args are for first creation only; after process death they are restored from SavedStateHandle. Do not cache the Intent in ViewModel; cache only the parsed id or needed fields.

---

## Result (Activity Result)

- **API** — registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> ... }. Target calls setResult(RESULT_OK, Intent().putExtra(...)); in callback use result.data?.getXxxExtra(key).
- **Result size** — Same rules: small data, Parcelable/primitives; for large result use shared ViewModel, singleton, or persistence and pass only key or flag in the result Intent.
