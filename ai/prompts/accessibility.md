# Accessibility

This prompt defines accessibility requirements for content, interaction, and navigation in the XML + View system. Scope, rules, and required operations are stated only in this file.

---

## Scope

- All user-facing UI and interaction. Goal: the app remains usable with TalkBack, large text, high contrast, etc.

---

## Content and semantics

- **Content description** — Set **contentDescription** (android:contentDescription or contentDescription) on icon buttons, meaningful images (not purely decorative). Describe the action (e.g. “Submit order”), not “button”.
- **Text** — Do not rely on images alone for meaning; if you use an image, give the same information via contentDescription.
- **Grouping and headings** — Use **accessibilityHeading** or clear ViewGroup structure for lists, forms, cards so TalkBack users can navigate by block; keep heading hierarchy (title, subtitle, body).

---

## Touch and operation

- **Touch target** — Minimum about **48dp×48dp** for tappable areas; if the visible control is smaller, expand with touchDelegate or transparent padding.
- **Focus order** — Use focusable, focusOrder or tab order so focus order matches visual order; custom views must handle accessibilityFocus and click correctly.
- **Gestures** — If you use custom gestures, provide an alternative (button or menu); do not require “swipe only” for critical actions.

---

## Visual and contrast

- **Contrast** — Text vs background should meet WCAG (e.g. at least 4.5:1 for body). Do not convey important information by color alone; add icon or text.
- **Scaling** — Support system font scaling; layout should not break or clip badly at 1.2x, 1.5x; key text should not be cut off.
- **Dynamic type** — Where supported, use sp for key text and test at large sizes.

---

## State and feedback

- **State** — Selected, loading, disabled, error should be conveyed beyond color: stateListDrawable, contentDescription, or announceForAccessibility.
- **Dynamic content** — For list updates, Toast, Snackbar use **announceForAccessibility** or **LiveRegion** so TalkBack users are notified; avoid silent changes.

---

## Verification

- **TalkBack** — Enable TalkBack on device/emulator and walk key paths (enter screen, list, submit, back); check focus order and avoid redundant announcements.
- **Large text** — Increase system font size and check that layout does not break and text is not truncated.
