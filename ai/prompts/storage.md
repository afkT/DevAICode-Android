# Storage Management

This prompt defines how to implement Android storage: app data, files, and media. It applies to projects using modern storage (DataStore, MediaStore, scoped storage). Scope, rules, and required operations are stated only in this file.

---

## Scope

- App data (SharedPreferences, DataStore, MMKV, SQLite), file storage (internal/external), media (images/video/audio), storage permissions, and file sharing. Includes legacy APIs and scoped storage (API ≥ 29).

---

## Data storage

### 1. Config and preferences

- **DataStore** (preferred) — Proto DataStore for type-safe config or Preferences DataStore for key-value. Use androidx.datastore. Migrate from SharedPreferences with PreferenceDataStoreMigration, then remove old file.
- **MMKV** — Fast key-value; good for frequent read/write; supports multi-process.
- **SharedPreferences** — Simple config only; note apply() vs commit(); avoid reading on UI thread.

### 2. Structured data

- **Room** — Complex structured data; relations, migrations, async; use with Flow.
- **SQLite** — Direct use for simple cases; prefer Room for complex; guard against SQL injection.
- **Files** — JSON/XML with kotlinx.serialization or Gson.

### 3. Caching

- **Memory** — LruCache, ArrayMap for hot data; set capacity.
- **Disk** — DiskLruCache, Glide disk cache; size limit and eviction.
- **Network** — OkHttp Cache, Retrofit response cache; TTL and policy.

---

## File storage

### 1. App-specific dirs

- **Internal** — getFilesDir(), getCacheDir(); no permission; cleared on uninstall.
- **External** — getExternalFilesDir(), getExternalCacheDir(); cleared on uninstall; no permission on API ≥ 29.
- **Use** — Sensitive data in internal; large files in external.

### 2. Shared storage

- **MediaStore** (API ≥ 29) — Access media via ContentResolver insert/query.
- **Scoped storage** — App cannot access other apps’ files; use SAF or MediaStore.
- **Legacy** — API &lt; 29 can use requestLegacyExternalStorage; new apps should not.

### 3. File usage

- **Async** — Do file I/O on Dispatchers.IO; do not block main thread.
- **Errors** — Handle space, permission, IO; provide fallback.
- **Paths** — Validate paths; avoid path traversal.

---

## Permissions and security

### 1. Storage permissions

- **API &lt; 29** — READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE.
- **API ≥ 29** — Own files: no permission; media: MediaStore; other: SAF.
- **MANAGE_EXTERNAL_STORAGE** — Only for special cases (e.g. file manager).

### 2. Data security

- **Encryption** — EncryptedSharedPreferences, EncryptedFile for sensitive data; keys from Android Keystore.
- **Modes** — Internal files default MODE_PRIVATE; avoid MODE_WORLD_READABLE/WRITEABLE.
- **Transfer** — HTTPS for transfer; no plaintext sensitive data.

---

## Performance

### 1. Storage

- **Batching** — Room @Transaction; SQLite transactions.
- **Indexes** — Index query/sort columns; avoid full scan.
- **Compression** — GZIP for large text/data; balance size vs CPU.

### 2. Memory

- **Streaming** — Use InputStream/OutputStream for large files; avoid loading full file.
- **Cache** — Cap size; evict when needed.
- **Handles** — Close files, Cursor; use try-with-resources.

---

## Best practices

### 1. Choice

- **Small config** — DataStore Preferences or MMKV.
- **Structured** — Room.
- **Large files** — External app dirs.
- **Media** — MediaStore (API ≥ 29).

### 2. Code

- **Repository** — Encapsulate storage behind Repository; single API.
- **Config** — Centralize paths, cache size.
- **Migration** — Plan migration from old to new storage; support incremental.

### 3. UX

- **Space** — Check free space; prompt to free space when low.
- **Offline** — Cache critical data; support offline browse.
- **Sync** — Cloud sync and conflict handling.

---

## Quality checklist (self-check or code review)

- [ ] Is the right storage used for each data type?
- [ ] Is scoped storage followed (API ≥ 29)?
- [ ] Is file I/O on a background thread?
- [ ] Are storage permissions correct (including API differences)?
- [ ] Is sensitive data encrypted?
- [ ] Are file handles and Cursor closed?
- [ ] Is free space checked before write?
- [ ] Are errors and fallbacks handled?
