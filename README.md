# Ashish Atte 2.0.1

Android GPS attendance app using Firebase Authentication and Cloud Firestore.

## Build

### GitHub Actions
Push this project to the `main` branch, then open **Actions → Build Ashish Atte APK**.
The workflow uses Gradle 8.10.2 and JDK 17 and publishes the debug APK as an artifact.

### Android Studio
Open this folder as an Android project. If Android Studio asks to use a local Gradle installation, use Gradle 8.10.2 and JDK 17.

## Firebase setup

1. Enable Email/Password Authentication.
2. Create Firestore.
3. Deploy `firestore.rules`.
4. Create employee documents at `employees/{firebaseAuthUid}`.
5. Configure the office in `settings/office`.
6. Grant the administrator account the Firebase custom claim `{ "admin": true }` from a trusted backend.

Do not place a Firebase service-account private key in the APK.

## Attendance behavior

- Employee signs in with Firebase Email/Password.
- Precise location permission is requested.
- Current GPS position is checked against `settings/office`.
- Check-in/check-out is written with a server timestamp.
- Admin UI can update the office geofence.


## Finalized changes
- GPS attendance now requests a fresh high-accuracy location instead of relying only on the cached `lastLocation`.
- Firebase package/application ID remains `com.example.ashishatte` and matches `google-services.json`.
- Debug APK workflow remains configured for JDK 17, Gradle 8.10.2, Android 35.
