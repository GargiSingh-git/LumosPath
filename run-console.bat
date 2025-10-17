@echo off
:: LumosPath Console Launcher Script for Windows
echo 🌟 Starting LumosPath - Mental Health Support Application...
echo 💻 Console Version
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

echo 🚀 Launching LumosPath Console...
echo.

:: Run the console application
call mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication"

echo.
echo Thank you for using LumosPath! 💙
echo Remember: You are stronger than you think!
pause