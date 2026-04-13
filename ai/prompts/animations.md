# Animations & Transitions

This prompt defines how to implement animations and transitions on Android with the View system. It applies to projects using View + DataBinding + MVVM. Scope, rules, and required operations are stated only in this file.

---

## Scope

- View animation (Alpha, Scale, Translate, Rotate), property animation (ValueAnimator, ObjectAnimator), Transition Framework, MotionLayout, and custom animations. Includes screen transition, list item animation, loading, and other common cases.

---

## Types and when to use

### 1. Property animation

- **ObjectAnimator** — Animate View properties (translationX/Y, alpha, scaleX/Y, rotation); ObjectAnimator.ofFloat(view, "translationX", 0f, 100f).
- **ValueAnimator** — Animate any value; ValueAnimator.ofFloat(0f, 1f) + addUpdateListener to update state.
- **AnimatorSet** — Combine with playTogether() or playSequentially().
- **Interpolator** — LinearInterpolator, AccelerateDecelerateInterpolator, OvershootInterpolator; custom TimeInterpolator for custom easing.

### 2. Transition Framework

- **Scene/Transition** — Complex in-screen changes; define Scene, TransitionManager.go() to switch.
- **Shared element** — Between screens with @Element(sharedElement) or ActivityOptions.makeSceneTransitionAnimation().
- **Custom** — Extend Transition and implement createAnimator().

### 3. MotionLayout

- **ConstraintSet** — Start/end state; motionScene or code.
- **KeyFrame** — KeyPosition, KeyAttribute, KeyCycle for control.
- **OnSwipe/OnClick** — TouchResponse for gesture/click.

### 4. Custom

- **View** — Override draw() + invalidate() for canvas animation.
- **SurfaceView/GLSurfaceView** — For high-performance or game/video.

---

## Performance

### 1. Hardware

- **RenderThread** — translationX/Y, scaleX/Y, alpha, rotation run on RenderThread; prefer these.
- **Overdraw** — Reduce overlap; use clipChildren=false, clipToPadding=false where it helps.
- **LayerType** — For complex animation set view.setLayerType(LAYER_TYPE_HARDWARE, null) before and restore after.

### 2. Memory

- **Cleanup** — animator.cancel(), animator.removeAllListeners() when done to avoid leaks.
- **Lifecycle** — Cancel when View/Fragment is destroyed; avoid holding View with strong ref (e.g. WeakReference).

### 3. Smoothness

- **60fps** — Avoid heavy work or I/O during animation.
- **Choreographer** — For custom animation use Choreographer.getInstance().postFrameCallback() to sync with VSync.

---

## Best practices

### 1. Design

- **Purpose** — Animation should communicate state or focus; avoid decoration-only animation.
- **Consistency** — Same duration and easing across the app; document animation spec.
- **Accessibility** — Respect “reduce motion”; check ValueAnimator.areAnimatorsEnabled() and skip or shorten when false.
- **Duration** — Match design (often 200–300ms); use @dimen or theme for easy tuning.

### 2. Code

- **Encapsulate** — Put complex animation in a class or extension (e.g. View.fadeIn(duration)).
- **Config** — Extract duration, interpolator to constants or config.
- **Reuse** — Shared logic in helpers (e.g. AnimationUtils.createBounceAnimator()).

### 3. State

- **DataBinding** — Bind animation state to layout via LiveData (e.g. app:animateVisibility="@{viewModel.loading}").
- **ViewModel** — Trigger animation from ViewModel via StateFlow if needed.

---

## Quality checklist (self-check or code review)

- [ ] Do animated properties use RenderThread (translation/scale/alpha/rotation)?
- [ ] Are animations cancelled when lifecycle ends?
- [ ] Is “reduce motion” respected?
- [ ] Is duration in line with platform (e.g. 200–300ms)?
- [ ] Is heavy work avoided during animation?
- [ ] Is hardware acceleration used for complex animation?
- [ ] Is animation logic reusable and encapsulated?
