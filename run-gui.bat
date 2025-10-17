@echo off
:: LumosPath GUI Launcher Script for Windows
echo 🌟 Starting LumosPath - Mental Health Support Application...
echo ✨ GUI Version with JavaFX Interface
echo.

:: Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo Please install Java 17 or higher to run LumosPath
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

:: Check Java version
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -version 2^>^&1 ^| find "version"') do set "JAVA_VERSION=%%j"
if %JAVA_VERSION% lss 17 (
    echo ⚠️  Java 17 or higher is required. Current version: %JAVA_VERSION%
    echo Please upgrade your Java installation
    pause
    exit /b 1
)

echo ☑️  Java version check passed

:: Check if Maven is available
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ Maven is not installed or not in PATH
    echo Please install Apache Maven to run LumosPath
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo ☑️  Maven check passed

:: Compile if needed
if not exist "target\classes" (
    echo 🔧 Compiling application...
    call mvn compile
    if %ERRORLEVEL% neq 0 (
        echo ❌ Compilation failed
        pause
        exit /b 1
    )
)

echo 🚀 Launching LumosPath GUI...
echo.

:: Try to run with JavaFX plugin first, then fallback to exec plugin
call mvn javafx:run 2>nul
if %ERRORLEVEL% neq 0 (
    echo Trying alternative launch method...
    call mvn exec:java -Dexec.mainClass="com.lumospath.gui.LumosPathGUI"
)

echo.
echo Thank you for using LumosPath! 💙
echo Remember: You are stronger than you think!
pause