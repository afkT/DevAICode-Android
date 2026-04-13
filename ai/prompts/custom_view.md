# Custom View (Custom Drawing and Measurement)

This prompt defines how to create custom Views with proper measurement, drawing, custom attributes, DataBinding integration, and accessibility. It applies to projects using the View system (not Compose). Scope, rules, and required operations are stated only in this file.

---

## Scope

- Creating or modifying custom Views and ViewGroups: onMeasure, onDraw, custom XML attributes (attrs), saved state, DataBinding support, and accessibility. Does not cover existing framework widgets or third-party libraries.

---

## Class structure

- **Extend** — Extend `View`, `ViewGroup`, or the closest framework class (e.g. `FrameLayout`, `LinearLayout`, `AppCompatImageView`). Prefer extending a concrete class when you only need to add behavior; extend `View` only for fully custom drawing.
- **Constructors** — Provide all three standard constructors (Context; Context + AttributeSet; Context + AttributeSet + defStyleAttr) or use `@JvmOverloads constructor(context, attrs?, defStyleAttr = 0)`. Call the init method from the primary constructor path.
- **Init** — Parse custom attributes in `init {}` using `context.obtainStyledAttributes(attrs, R.styleable.XxxView)`, then `recycle()`. Set defaults for all attributes so the view works without XML attrs.

---

## Custom attributes (attrs.xml)

- **Declare** — In `res/values/attrs.xml`: `<declare-styleable name="XxxView">` with typed `<attr>` entries (string, color, dimension, integer, enum, reference, boolean, float).
- **Naming** — Prefix attributes with the view name or abbreviation to avoid collision (e.g. `cv_borderColor`, `cv_borderWidth`).
- **Use in XML** — `app:cv_borderColor="@color/xxx"`; namespace is `xmlns:app="http://schemas.android.com/apk/res-auto"`.

---

## Measurement (onMeasure)

- **Contract** — Always call `setMeasuredDimension(width, height)` at the end of `onMeasure`.
- **MeasureSpec** — Respect `MeasureSpec.EXACTLY`, `AT_MOST`, `UNSPECIFIED`. For `AT_MOST`, compute desired size and clamp to spec size. For `EXACTLY`, use spec size. For `UNSPECIFIED`, use desired size.
- **Padding** — Account for `paddingStart/End/Top/Bottom` in both measurement and drawing.
- **Wrap content** — If the view supports `wrap_content`, compute intrinsic size from content (text, drawable, children); do not default to `match_parent` behavior.
- **ViewGroup** — Measure children with `measureChild()` or `measureChildWithMargins()`; sum or max child sizes based on layout direction; account for child margins.

---

## Drawing (onDraw / dispatchDraw)

- **Canvas** — Override `onDraw(canvas: Canvas)` for View; for ViewGroup custom backgrounds/overlays, draw in `onDraw` (set `setWillNotDraw(false)`) or `dispatchDraw`.
- **Paint** — Create `Paint` objects in `init` or as class fields; never allocate in `onDraw` (called every frame). Use `Paint.ANTI_ALIAS_FLAG` for smooth edges.
- **Coordinates** — Draw relative to (0, 0) of the view; account for padding. Use `width` and `height` (post-measurement), not hardcoded sizes.
- **Invalidation** — Call `invalidate()` when visual state changes (color, progress); call `requestLayout()` when size may change (text, content). Do not call both on every property change unless necessary.

---

## Saved state (configuration change)

- **SavedState** — Override `onSaveInstanceState()` and `onRestoreInstanceState(state)` to persist view-specific state (selection, progress, expanded). Use `BaseSavedState` pattern or `Parcelable`.
- **ID required** — Saved state only works if the view has an `android:id`; document this requirement.
- **ViewModel** — For complex state tied to business logic, prefer keeping state in ViewModel and passing it via DataBinding; the view should be stateless where possible.

---

## DataBinding integration

- **BindingAdapter** — For custom attributes that need logic (e.g. loading a URL into the custom view), create a `@BindingAdapter` in the shared binding package.
- **InverseBindingAdapter** — For two-way binding (e.g. a custom slider value), provide `@InverseBindingAdapter` and `@InverseBindingListener` so `@={}` works.
- **Setter** — Simple attributes (color, text) can use standard Kotlin property setters; DataBinding resolves `app:xxx` to `setXxx()` automatically.

---

## Accessibility

- **Content description** — Set meaningful `contentDescription` for non-text views; update when state changes.
- **Role** — Use `ViewCompat.setAccessibilityDelegate` or override `onInitializeAccessibilityNodeInfo` to set class name, role description, and state (checked, selected, disabled).
- **Events** — Fire `AccessibilityEvent` on meaningful state changes; support `performClick()` so TalkBack click works.
- **Touch target** — Ensure touch target is at least 48dp; expand with `TouchDelegate` if visual size is smaller.

---

## Performance

- **Avoid** — Object allocation in `onDraw`, deep view hierarchies, unnecessary `invalidate` calls.
- **Hardware acceleration** — Most Canvas ops are hardware-accelerated; if you use unsupported ops (e.g. `canvas.clipPath` on old APIs), set `setLayerType(LAYER_TYPE_SOFTWARE, null)` only when needed.
- **Bitmap** — For complex static graphics, consider drawing to an offscreen `Bitmap` once and blitting in `onDraw`; recycle when the view is detached.

---

## Testing

- **Unit** — Test measurement logic by constructing the view in a test context and asserting measured dimensions.
- **Instrumented** — Use Espresso or screenshot tests to verify drawing output and accessibility.
- **Accessibility** — Run Accessibility Scanner or TalkBack on screens with the custom view.

---

## Example request (for AI)

```
Create a custom CircleProgressView:
- Custom attrs: cv_progress (int 0–100), cv_progressColor (color), cv_trackColor (color), cv_strokeWidth (dimension).
- Draws a circular track and arc for progress.
- Supports wrap_content (default 48dp) and exact size.
- BindingAdapter for app:cv_progress so it works with @{viewModel.progress}.
- Accessibility: announces "XX percent" on progress change.
```
