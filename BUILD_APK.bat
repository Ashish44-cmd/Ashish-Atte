@echo off
setlocal
cd /d "%~dp0"
if not exist gradlew.bat (
  echo Gradle wrapper is not included in this package.
  echo Use Android Studio with Gradle 8.10.2 and JDK 17, or run the GitHub Actions workflow.
  exit /b 1
)
call gradlew.bat :app:assembleDebug
if errorlevel 1 exit /b %errorlevel%
echo.
echo APK: app\build\outputs\apk\debug\app-debug.apk
