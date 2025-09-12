@echo off

setlocal enableextensions enabledelayedexpansion

set /p VERSION=Enter version (e.g. 1.2.3): 

REM Use release.kjs at root for signing
set RELEASE_KEYSTORE=release.kjs
set /p RELEASE_STORE_PASSWORD=Enter keystore password: 
set /p RELEASE_KEY_ALIAS=Enter key alias: 
set /p RELEASE_KEY_PASSWORD=Enter key password: 

REM Optionally update versionName in app/build.gradle.kts (uncomment if needed)
REM powershell -Command "(Get-Content app\build.gradle.kts) -replace 'versionName = ".*"', 'versionName = \"%VERSION%\"' | Set-Content app\build.gradle.kts"

echo Pulling latest changes from origin/main...
git pull origin main

echo Building debug APK...
call gradlew.bat :app:assembleDebug

echo Building release APK...
call gradlew.bat :app:assembleRelease -PRELEASE_STORE_FILE=%RELEASE_KEYSTORE% -PRELEASE_STORE_PASSWORD=%RELEASE_STORE_PASSWORD% -PRELEASE_KEY_ALIAS=%RELEASE_KEY_ALIAS% -PRELEASE_KEY_PASSWORD=%RELEASE_KEY_PASSWORD%


REM Copy all built APKs from app\release to a versioned directory for upload
set RELEASEDIR=app\release
set VERSIONDIR=%RELEASEDIR%\%VERSION%
if not exist %VERSIONDIR% mkdir %VERSIONDIR%

for %%f in (%RELEASEDIR%\*.apk) do copy "%%f" "%VERSIONDIR%\%%~nxf" >nul

echo.
echo Builds complete!
echo Release APKs (for upload):
dir /b %VERSIONDIR%\*.apk
echo.
echo You can now upload all APKs in %VERSIONDIR% to your GitHub release or distribution platform.

echo Checking for existing tag v%VERSION% ...
git tag -l v%VERSION% >nul 2>&1
if not errorlevel 1 (
	echo Tag v%VERSION% exists locally. Deleting...
	git tag -d v%VERSION%
)
git ls-remote --tags origin v%VERSION% | findstr /C:"refs/tags/v%VERSION%" >nul 2>&1
if not errorlevel 1 (
	echo Tag v%VERSION% exists on remote. Deleting...
	git push --delete origin v%VERSION%
)

echo Tagging release as v%VERSION% ...
git tag v%VERSION%

git push origin v%VERSION%

REM Upload APKs to GitHub Release using gh CLI

echo Uploading APKs to GitHub Release v%VERSION% ...
set APKS=
for %%f in (%VERSIONDIR%\*.apk) do set APKS=!APKS! "%%f"
gh release view v%VERSION% --repo prirai/qalculate-android >nul 2>&1
if %errorlevel%==0 (
	echo Release v%VERSION% exists. Uploading APKs ...
	call gh release upload v%VERSION% %APKS% --clobber --repo prirai/qalculate-android
) else (
	echo Creating new release v%VERSION% and uploading APKs ...
	call gh release create v%VERSION% %APKS% --title "Release v%VERSION%" --notes "Automated release for v%VERSION%" --latest --verify-tag --repo prirai/qalculate-android
)
echo APK upload complete.

pause
