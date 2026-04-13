# Screen Adaptation & Responsive Layout

This prompt defines layout and resource strategy for different screen sizes, densities, and form factors. It applies to XML, DataBinding, and ConstraintLayout. Scope, rules, and required operations are stated only in this file.

---

## Scope

- All XML layouts and size/density-related resources. Goal: usable and consistent on phones, tablets, foldables, and multi-window.

---

## Layout and units

- **Root** — Prefer **ConstraintLayout** to reduce nesting and support ratio/constraint adaptation.
- **Units** — **dp** for size, **sp** for text; avoid hardcoded px or magic numbers in code; use **dimension resources** (e.g. @dimen/sp_14, @dimen/dp_16) so values can be overridden by configuration.
- **Proportion** — Use **Guideline** (percent or fixed dp), **Barrier**, **Chain** for relative position and spacing so layout is not tied to a fixed width.

---

## Resource qualifiers

- **Width** — values-sw&lt;N&gt;dp (smallest width), values-w&lt;N&gt;dp (available width) for different width buckets; e.g. sw360dp, sw600dp (tablet).
- **Orientation** — layout-land, values-land for landscape when needed.
- **Density** — Usually dp/sp is enough; use drawable-xxhdpi etc. or vectors; avoid many duplicate layouts per density.
- **Order** — Match Android’s qualifier order so the right folder is selected (e.g. values-sw600dp vs values-land combination).

---

## Notch, corners, and window insets

- **WindowInsets** — Use **WindowInsetsCompat** or View.setOnApplyWindowInsetsListener for system bars, notch, navigation bar; apply padding or margin to content so it is not covered.
- **Edge-to-edge** — When full screen or edge-to-edge, apply insets to main content; coordinate with status/navigation bar color.

---

## Multi-window and foldables

- **Multi-window** — In split or freeform, layout should shrink; avoid fixed min size that makes the app unusable; consider single pane / two pane by width.
- **Foldables** — If supported, listen to configuration or Jetpack WindowManager fold state and switch layout or data (e.g. list ↔ master-detail) on expand/fold.

---

## Typography

- **sp** — Use sp for text so it scales with system; avoid very small body (e.g. ≥ 12sp for body).
- **Line** — Use lineSpacingMultiplier, lineSpacingExtra or dimens for line height and readability; larger screens can use slightly larger text.

---

## Animation and transition

- **MotionLayout** — If used, define key transitions and constraints as scenes; reuse or provide different scenes by width so animation does not misalign.
- **Config change** — On rotation, fold, multi-window avoid losing state; keep necessary data in ViewModel or SavedStateHandle.

---

## Testing and acceptance

- **Devices** — Test on several resolutions and densities (e.g. 5", 6", 7", 10"); if possible on foldable, test expand/fold.
- **Orientation** — Portrait and landscape; check that key screens are not covered or misaligned.
- **Design** — If design is for one width (e.g. 360dp), map with ratio or dimen and spot-check at other widths.
