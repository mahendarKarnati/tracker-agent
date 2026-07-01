@echo off
setlocal

title Tracker Agent Build

echo ============================================
echo Building Tracker Agent...
echo ============================================

REM --------------------------------------------------
REM Check JAVA_HOME
REM --------------------------------------------------
if "%JAVA_HOME%"=="" (
    echo ERROR: JAVA_HOME is not set.
    pause
    exit /b 1
)

REM --------------------------------------------------
REM Clean
REM --------------------------------------------------
if exist dist rmdir /S /Q dist
if exist runtime rmdir /S /Q runtime
if exist installer rmdir /S /Q installer

mkdir dist

echo.
echo [1/5] Maven Build...
call mvn clean package -DskipTests
if errorlevel 1 goto error

echo.
echo [2/5] Copying Jar...
copy target\tracker-backend-0.0.1-SNAPSHOT.jar dist\
if errorlevel 1 goto error

echo.
echo [3/5] Creating Runtime...

"%JAVA_HOME%\bin\jlink.exe" ^
--add-modules ^
java.base,^
java.desktop,^
java.datatransfer,^
java.logging,^
java.management,^
java.naming,^
java.net.http,^
java.prefs,^
java.sql,^
java.transaction.xa,^
java.xml,^
jdk.crypto.ec,^
jdk.unsupported ^
--output runtime

if errorlevel 1 goto error

if errorlevel 1 goto error

echo.
echo [4/5] Creating Installer...

jpackage ^
--type exe ^
--name TrackerAgent ^
--vendor "HANSETECH SOFT SOLUTION PRIVATE LIMITED" ^
--app-version 1.0.0 ^
--input dist ^
--main-jar tracker-backend-0.0.1-SNAPSHOT.jar ^
--runtime-image runtime ^
--icon TrackerAgent.ico ^
--win-shortcut ^
--win-menu ^
--win-dir-chooser ^
--install-dir TrackerAgent ^
--dest installer ^
--win-upgrade-uuid D1A49A3E-3E6A-4D5F-9A2A-6B7C8D9E1F10

if errorlevel 1 goto error

echo.
echo ============================================
echo BUILD SUCCESS
echo ============================================
echo.
echo Installer:
echo %CD%\installer

pause
exit /b 0

:error
echo.
echo ============================================
echo BUILD FAILED
echo ============================================
pause
exit /b 1