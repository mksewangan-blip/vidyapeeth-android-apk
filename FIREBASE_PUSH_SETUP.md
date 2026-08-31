# Push notification setup

This Android project includes Firebase Cloud Messaging (FCM) support for status-bar notifications, including delivery when the app is backgrounded/closed (subject to Android/FCM delivery rules).

## Before the GitHub build
1. Create/open a Firebase project.
2. Add Android app package: `in.sewangan.vidyapeeth`.
3. Download `google-services.json`.
4. In the APK GitHub repository open Settings > Secrets and variables > Actions.
5. Create repository secret `GOOGLE_SERVICES_JSON` containing the Base64-encoded contents of `google-services.json`.
   - Windows PowerShell: `[Convert]::ToBase64String([IO.File]::ReadAllBytes("google-services.json"))`
   - Linux/macOS: `base64 -w 0 google-services.json` (macOS: `base64 < google-services.json`)
6. Run Actions > Build Android APK > Run workflow.
7. Download artifact `Sewangan-Vidyapeeth-Admin-APK`.

## Backend requirement
The APK can receive FCM messages, but your Apps Script/backend still needs to store each app's FCM token and send the appropriate notification to that token. The Android JavaScript bridge exposes the current token through `VidyapeethAndroid.getPushToken()`.
