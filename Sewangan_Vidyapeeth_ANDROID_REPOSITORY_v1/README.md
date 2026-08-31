# Sewangan Vidyapeeth Admin Android

Android wrapper project for the Sewangan Vidyapeeth Admin application.

## Build
Open this repository in Android Studio, allow Gradle sync, then use **Build > Build APK(s)**.

## Included
- Current v9 Admin frontend bundled in `app/src/main/assets/www/`
- Camera permission for face registration and attendance
- Android 13+ notification permission
- Native notification channel and JavaScript bridge
- Responsive WebView for phone/tablet/desktop-style Android devices
- Padded launcher icon with Admin badge to avoid icon cropping

## Native notification bridge
Web code can call:
`SVAndroidNotify('Title', 'Message')`

This displays a native Android status-bar notification while the application process is available. Reliable push delivery while the application is fully closed requires a push service/backend integration in a later version.
