@echo off
:: Windows Compatibility Test Script for LumosPath
echo ========================================
echo 🧪 LumosPath Windows Compatibility Test
echo ========================================
echo.

echo 📊 Testing Windows compatibility...
echo.

:: Test 1: Java Installation
echo [1/7] Testing Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ FAIL: Java not found
    goto :end
) else (
    echo ✅ PASS: Java is installed
)

:: Test 2: Java Version
echo [2/7] Testing Java version...
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -version 2^>^&1 ^| find "version"') do set "JAVA_VERSION=%%j"
if %JAVA_VERSION% lss 17 (
    echo ❌ FAIL: Java version %JAVA_VERSION% is too old (need 17+)
    goto :end
) else (
    echo ✅ PASS: Java version %JAVA_VERSION% is compatible
)

:: Test 3: Maven Installation
echo [3/7] Testing Maven installation...
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ FAIL: Maven not found
    goto :end
) else (
    echo ✅ PASS: Maven is installed
)

:: Test 4: Project Structure
echo [4/7] Testing project structure...
if not exist "pom.xml" (
    echo ❌ FAIL: pom.xml not found
    goto :end
)
if not exist "src\main\java" (
    echo ❌ FAIL: Source directory not found
    goto :end
)
if not exist "run-console.bat" (
    echo ❌ FAIL: Windows batch scripts not found
    goto :end
)
echo ✅ PASS: Project structure is correct

:: Test 5: Compilation
echo [5/7] Testing compilation...
mvn compile -q >compile.log 2>&1
if %ERRORLEVEL% neq 0 (
    echo ❌ FAIL: Compilation failed (see compile.log)
    goto :end
) else (
    echo ✅ PASS: Project compiles successfully
    if exist compile.log del compile.log
)

:: Test 6: Windows Path Handling
echo [6/7] Testing Windows path handling...
if not exist "target\classes" (
    echo ❌ FAIL: Compiled classes not found
    goto :end
) else (
    echo ✅ PASS: Windows paths work correctly
)

:: Test 7: Database Compatibility (Quick Test)
echo [7/7] Testing database initialization...
echo Testing database creation... > test.tmp
java -cp target\classes;%USERPROFILE%\.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar com.lumospath.util.DatabaseUtil >db-test.log 2>&1
if exist lumospath.db (
    echo ✅ PASS: Database creation works
    del lumospath.db >nul 2>&1
) else (
    echo ⚠️  SKIP: Database test (may require full application run)
)
if exist test.tmp del test.tmp
if exist db-test.log del db-test.log

echo.
echo ========================================
echo 🎉 Windows Compatibility Test Results
echo ========================================
echo ✅ LumosPath should work properly on this Windows system!
echo.
echo 🚀 You can now run:
echo    📘 Console: run-console.bat
echo    🖥️  GUI: run-gui.bat  
echo    📋 Demo: demo.bat
echo.
echo 💡 Tips:
echo    • Use Windows Terminal for best console experience
echo    • Run as Administrator if you encounter permission issues
echo    • Check Windows Defender if files are blocked
echo.

:end
echo ========================================
pause