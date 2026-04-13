# ProGuard / R8 (Obfuscation and Release Build)

This prompt defines code shrinking, obfuscation, resource shrinking, and keep rules for release builds. It applies to Android Gradle and common libraries (Room, Retrofit, DataBinding, Hilt, reflection, serialization). Scope, rules, and required operations are stated only in this file.

---

## Scope

- Enabling minifyEnabled (R8/ProGuard) and shrinkResources; writing and maintaining `proguard-rules.pro`; debugging release crashes and ClassNotFoundException/NoSuchMethodError; generating and using the mapping file.

---

## Enabling and basic config

- **build.gradle** — In `buildTypes.release` set `minifyEnabled true`, `shrinkResources true` (optional, requires minifyEnabled); `proguardFiles` to `getDefaultProguardFile('proguard-android-optimize.txt')` and the project `proguard-rules.pro`.
- **R8 vs ProGuard** — AGP uses **R8** by default (ProGuard-compatible rules). Rule syntax is the same; R8 does shrinking, obfuscation, optimization and writes `build/outputs/mapping/release/mapping.txt`.
- **Debugging** — Use the mapping file to de-obfuscate release stack traces; upload mapping to your crash service for symbolication; keep `mapping.txt` or auto-upload in the build script.

---

## Keep rule syntax

- **-keep** — Keep class and members; no obfuscation or removal. E.g. `-keep class com.example.Foo { *; }`.
- **-keepclassmembers** — Keep only members; class name can be obfuscated. Use when “method/field names must stay” (e.g. reflection).
- **-keepnames / -keepclassmembernames** — Keep names but allow removal of unused code.
- **-dontwarn** — Ignore missing or reference warnings for given classes; use only when the missing reference is acceptable (e.g. optional dependency).
- **@Keep** — In code, annotate class or member with `@Keep` (androidx.annotation); equivalent to -keep for that element.

---

## Common keep scenarios

### Data models and serialization

- **Parcelable / Serializable** — Entities passed via Intent, Bundle, or process must keep no-arg constructor and field names (or keep generated classes if using Parceler). E.g. `-keep class * implements android.os.Parcelable { *; }`.
- **Gson / Moshi** — DTO/Entity field names are read by reflection; keep them. E.g. `-keep class com.example.dto.** { *; }` or by package; keep annotations: `-keepattributes Signature, *Annotation*`.
- **Kotlin data class** — If using reflection for serialization, keep the data class and members; note Kotlin generates `copy`, `componentN`, etc.; keep as needed or use -keepclassmembers.

### Room, Retrofit, OkHttp

- **Room** — Entity and DAO implementation are generated; keep Entity and DAO public methods. Typically `-keep class * extends androidx.room.RoomDatabase`, and keep `**/entity/**` and DAO interfaces.
- **Retrofit** — Keep interface and DTO if used by reflection; `-keepattributes RuntimeVisibleAnnotations` etc.; if using Gson converter, add Gson keep rules.
- **OkHttp** — Usually AAR ships its own rules; keep custom Interceptor or Cookie implementations if they are used via reflection.

### DataBinding and ViewBinding

- **DataBinding** — Generated binding classes are under the app namespace (e.g. `afkt.code.databinding`). Use `-keep class <your.package>.databinding.** { *; }` so binding classes are not obfuscated and missing at runtime.
- **ViewBinding** — Same for generated binding classes; adjust path to `**/databinding/**` or `**/viewbinding/**` per your setup.

### DI (Hilt / Koin)

- **Hilt** — Hilt generates and uses reflection; typically `-keep class dagger.hilt.** { *; }`, `-keep class *_HiltModules**`, keep @Inject constructors and @AndroidEntryPoint classes; follow Hilt’s official ProGuard docs.
- **Koin** — If using reflection registration, keep scanned modules/ViewModels; follow Koin’s official docs.

### Reflection and Native

- **Reflection** — Any class/method/field accessed by name (`Class.forName`, `getMethod`, `getField`) must be kept with -keep or -keepclassmembers or names will change and break at runtime.
- **JNI / Native** — Java methods called by name from native code must be kept; e.g. `-keepclasseswithmembernames class * { native <methods>; }`.

---

## General advice

- **By package** — Prefer package + wildcard (e.g. `com.example.api.dto.**`) instead of many single-class -keep rules.
- **Minimal** — Keep only what is needed; excessive keep increases size and reduces obfuscation.
- **Test** — After changing keep rules, build release and run key flows (login, list, detail, submit) to catch missing keep.
- **Third-party AAR** — Many libraries ship `proguard.txt` in the AAR and it is merged; if you still get errors, add rules in project `proguard-rules.pro`; use `-dontwarn` for known harmless missing references so the build does not fail.

---

## Resource shrinking (shrinkResources)

- **Enable** — `shrinkResources true` removes unreferenced resources and shrinks the APK; must be used with minifyEnabled.
- **Keep** — Use `keep.xml` (e.g. `res/raw/keep.xml`) to list resources to keep (e.g. drawables or raw files resolved by string at runtime).
