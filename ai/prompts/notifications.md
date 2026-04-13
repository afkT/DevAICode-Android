# Notifications

This prompt defines notification channels, foreground service notifications, and how to build notifications. It applies to projects targeting current SDK and using NotificationCompat. Scope, rules, and required operations are stated only in this file.

---

## Scope

- In-app notifications (messages, reminders), persistent notification for foreground services, notification channels, and permissions (e.g. POST_NOTIFICATIONS on Android 13+). Does not cover push SDK integration details.

---

## Notification channels

- **Required** — On Android 8.0+ every notification must belong to a **NotificationChannel**. Create the channel before first use (e.g. in Application or at first notification entry); keep `channelId` stable for reuse.
- **Properties** — Set channel `name` (user-visible), `importance` (IMPORTANCE_HIGH/DEFAULT/LOW, etc.), `enableVibrate`, `enableLights`, `sound`. Users manage by channel in system settings.
- **Grouping** — Use `setGroup` by feature so notifications can be grouped in the shade; use high importance or heads-up for important notifications.

---

## Building notifications

- **Compatibility** — Use **NotificationCompat** and **NotificationCompat.Builder** with context and `channelId`; avoid deprecated APIs.
- **Required** — setContentTitle, setContentText (or setStyle(BigTextStyle())), setSmallIcon (alpha-only icon); setContentIntent for tap.
- **Rich** — Use setStyle(NotificationCompat.BigPictureStyle()) for large images; BigTextStyle() for long text. Avoid very large images (memory and bandwidth).
- **Actions** — Optional addAction; handle delete/archive via PendingIntent or BroadcastReceiver and align with app logic.

---

## Foreground service notification

- **Foreground service** — On Android 9+ a foreground service must show a notification; build the Notification and ensure the channel exists before `startForeground(notificationId, notification)`.
- **Persistent** — Foreground notification is usually not dismissible (or dismissing stops the service, per product). Title/body should describe what the service does (e.g. “Syncing data”).
- **Permission** — On Android 13+ posting notifications requires **POST_NOTIFICATIONS**; check and request before posting; if denied, do not post or handle silently per product.

---

## Send and cancel

- **NotificationManager** — Get via `Context.getSystemService(NotificationManager::class.java)`; send with `notify(notificationId, notification)`. Same `notificationId` updates an existing notification.
- **Cancel** — `cancel(notificationId)` or `cancelAll()`; cancel when the task finishes or user dismisses; avoid stale notifications.
- **IDs** — Assign notificationId by feature or request (e.g. by type) so different use cases do not overwrite each other.
