# Media & Camera

This prompt defines how to implement Android media and camera features. It applies to projects using modern camera API (CameraX) and media frameworks. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Camera preview, capture, video recording, image processing, video recording, gallery selection, media file handling, and camera permission handling. Includes CameraX, Camera2 API, MediaStore, ExoPlayer, and image-processing libraries.

---

## Camera implementation

### 1. CameraX

- **LifecycleOwner** — Bind CameraX to Fragment/Activity lifecycle with `ProcessCameraProvider.bindToLifecycle()`.
- **Use cases** — Preview, ImageCapture, and VideoCapture can be bound together; choose as needed.
- **Resolution** — Use `AspectRatio.RATIO_4_3` or `RATIO_16_9`; pick best resolution for target devices.
- **Flash** — Control via `ImageCapture.setFlashMode()`; design UX for on/auto/off.

### 2. Permissions and safety

- **Permissions** — CAMERA for capture/preview; CAMERA + RECORD_AUDIO for video. For saving to shared storage, request storage/media permissions per API (e.g. API 33+ READ_MEDIA_IMAGES/VIDEO or READ_EXTERNAL_STORAGE) and follow scoped storage and MediaStore.
- **Privacy** — Show camera indicator during preview; add privacy copy for sensitive cases (e.g. face recognition).
- **Secure camera** — For enterprise/banking, consider CameraEffect or secure camera API to prevent screenshots.

### 3. Capture and processing

- **Capture** — ImageCapture `takePicture()`; handle result in callback; respect file path and permissions.
- **ImageAnalysis** — For real-time processing; set aspect ratio and analysis interval appropriately.
- **Formats** — JPEG for photos; YUV/NV21 for real-time; choose by use case.

---

## Media implementation

### 1. Images

- **Loading** — Use Glide/Coil; set placeholder, error, transforms (rounded, blur).
- **Caching** — Memory + disk; tune by size and usage.
- **Large images** — Use SubsamplingScaleImageView or BitmapFactory.Options.inSampleSize.

### 2. Video

- **Player** — ExoPlayer (recommended), MediaPlayer, IjkPlayer; choose by needs.
- **Formats** — H.264, H.265, VP8, VP9; consider device support and performance.
- **Streaming** — RTMP, HLS, DASH; choose by latency needs.

### 3. Media files

- **MediaStore** — On API ≥ 29 use MediaStore to insert/query; avoid raw file paths.
- **Scoped storage** — On API ≥ 29 follow scoped storage; use getExternalFilesDir() for app-specific dirs.
- **File access** — Use SAF for shared storage; handle permission persistence.

---

## Performance

### 1. Camera

- **Warm-up** — Pre-initialize CameraProvider on cold start to reduce first-open delay.
- **Resolution** — Match preview view to avoid unnecessary scaling.
- **Memory** — Release ImageProxy, Bitmap, etc. promptly; avoid leaks.

### 2. Media

- **Async** — Do image compress, filters, etc. on background (e.g. Dispatchers.IO).
- **Cache** — LRU and disk size limits; tune by device memory.
- **Battery** — For long video play/record, monitor battery and show low-battery notice.

---

## Best practices

### 1. UX

- **Smooth preview** — Keep ~30fps; avoid jank.
- **Feedback** — Haptic/sound on capture; clear state for recording.
- **Errors** — Fallback when camera is unavailable; when permission denied, guide user to settings.

### 2. Code

- **Encapsulate** — Use CameraXHelper or CameraViewModel; expose a simple API.
- **State** — Centralize camera state (previewing, capturing, recording); avoid conflicting state.
- **Config** — Make resolution, flash, HDR configurable for different scenarios.

### 3. Security and compliance

- **Privacy** — Describe camera/mic use in privacy policy.
- **Storage** — Encrypt sensitive images/video locally; use HTTPS for transfer.
- **Content** — For UGC, use content moderation; filter inappropriate content.

---

## Common scenarios

### 1. Barcode/QR

- **ZXing + CameraX** — ZXing for decode; CameraX for preview.
- **Focus** — Continuous focus; optional tap-to-focus.
- **Light** — Detect ambient light; auto flash on/off.

### 2. Capture and upload

- **Preview** — Show thumbnail after capture; allow retake.
- **Compress** — Compress per upload requirements; balance quality and size.
- **Progress** — Show upload progress; support pause/retry.

### 3. Video recording

- **Segments** — For long recording, save in segments to avoid huge files.
- **Filters** — GPUImage or OpenCV for real-time filters; watch performance.
- **Watermark** — Add timestamp/brand watermark after recording.

---

## Quality checklist (self-check or code review)

- [ ] Are camera permissions requested and handled correctly?
- [ ] Is CameraX bound to lifecycle correctly?
- [ ] Does image capture handle all error cases?
- [ ] Does media access follow scoped storage (API ≥ 29)?
- [ ] Are heavy image operations on a background thread?
- [ ] Are camera resources released at the right time?
- [ ] Is error handling and user feedback in place?
- [ ] Are performance and battery considered?
