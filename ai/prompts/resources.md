# Resources & Themes

This prompt defines how to organize and use strings, colors, dimens, themes, styles, and drawables. It applies to XML layouts and design system. Scope, rules, and required operations are stated only in this file.

---

## Scope

- res/values and qualified values (strings, colors, dimens, themes, styles); drawable and mipmap naming and layout; multi-language, dark mode, and design tokens.

---

## Resource dirs and qualifiers

- **Default** — values/, drawable/, layout/ are default. Qualified dirs (e.g. values-zh, values-night, drawable-xxhdpi) override when matched.
- **Order** — Follow Android qualifier order (MCC/MNC → language → layout direction → width → density → night, etc.); only add combinations that have real differences.
- **No duplication** — Put only overridden keys in qualified files; missing keys fall back to default values.

---

## Strings

- **Location** — res/values/strings.xml (default); locales in values-zh-rCN, values-en, etc. Do not hardcode user-visible text in layout or code.
- **Naming** — Lowercase + underscore; group by module/screen in comments; e.g. app_name, home_title, error_network_retry. Use &lt;xliff:g&gt; or %s/%d for placeholders; in code getString(R.string.xxx, arg).
- **translatable** — Default translatable="true"; set translatable="false" for debug-only or region-only strings to avoid wrong translation.
- **Long text** — Very long or rich text can live in res/raw/ or from server; keep only key or placeholder in strings.

---

## Colors

- **Location** — res/values/colors.xml or split (e.g. colors_primary.xml, colors_semantic.xml). Define theme colors in colors and reference in theme with ?attr/.
- **Naming** — Prefer semantics: color_primary, color_on_primary, color_error, color_surface; avoid raw value names (e.g. blue_500) unless they are design tokens; optional prefix (e.g. md_theme_ vs app).
- **Dark** — Override in values-night/colors.xml (or themes.xml); same keys, different values.
- **In code** — ContextCompat.getColor(context, R.color.xxx) or Resources.getColor(R.color.xxx, theme); in XML @color/xxx or ?attr/colorPrimary.

---

## Dimensions

- **Location** — res/values/dimens.xml (or dimens_sp.xml, dimens_dp.xml). For adaptation use values-sw360dp/, values-sw600dp/, etc.
- **Naming** — Include unit, e.g. sp_14_body, dp_16_margin, dp_1_divider. If design is 360dp width, use dp_*/sp_* plus comment.
- **Usage** — In layout @dimen/xxx; in code resources.getDimensionPixelSize(R.dimen.xxx). Avoid magic numbers or scattered dpToPx(16); use dimen for consistency and overrides.
- **Conversion** — Use Resources.getDimensionPixelSize/getDimensionPixelOffset or project util for dp/sp→px; do not duplicate conversion logic.

---

## Themes and styles

- **Theme** — Define in themes.xml or styles.xml; extend Theme.MaterialComponents.* or project base; apply via android:theme or AppTheme in Application/Activity.
- **Overrides** — In theme override colorPrimary, colorOnPrimary, colorSurface, textAppearance*, shapeAppearance* for consistency; use ?attr/xxx for theming and dark mode.
- **Style** — Reusable View attribute groups (e.g. Widget.App.Button.Primary); style can extend parent; name per Material or design system.
- **TextAppearance** — Size, weight, line spacing in textAppearance; define in theme (textAppearanceBody1, textAppearanceHeadline6, etc.); in layout android:textAppearance="?attr/textAppearanceBody1".
- **Dark theme** — In values-night/themes.xml extend default and override colors, or use Theme.MaterialComponents.DayNight.NoActionBar and provide night color resources.

---

## Drawable and mipmap

- **Drawable** — Prefer vector in drawable/; bitmaps by density in drawable-mdpi etc. or in drawable/ for system scale. Name lowercase+underscore: ic_arrow_right, bg_card_rounded.
- **Mipmap** — Launcher icons only; use mipmap-*. App icons use drawable.
- **Selector / shape** — Selector for pressed/disabled; shape or ripple for corners/border; use ?attr/colorControlHighlight etc. for theme.
- **Avoid** — Do not hardcode #RRGGBB or android:background="#..." in layout; use @drawable/xxx or ?attr/.

---

## References and design tokens

- **In layout** — @dimen/ for size, @color/ or ?attr/ for color, @string/ for text, @drawable/ for icons; use ?attr/ for theme-dependent so it follows theme.
- **Tokens** — If design has tokens (primary, spacing_md), define in colors/dimens and map in theme to ?attr/; reference attr in layout and code for theming and multi-brand.

---

## Multi-language and optional resources

- **Language** — values-zh, values-en, etc.; translate key strings; placeholder count and order must match default; missing key falls back to default (avoid crash).
- **Optional** — Large or locale-specific assets in drawable-zh etc.; only split layout by language if necessary; prefer strings for text.
- **RTL** — Use start/end, layoutDirection; optional RTL strings; values-ldrtl only when there is real difference.
