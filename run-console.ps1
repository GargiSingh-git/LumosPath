# LumosPath Console Launcher for Windows PowerShell
# Enhanced PowerShell script with better error handling and modern Windows features

param(
    [switch]$SkipChecks,
    [switch]$Quiet
)

# Set console to UTF-8 for better emoji support
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

if (-not $Quiet) {
    Write-Host "🌟 Starting LumosPath - Mental Health Support Application..." -ForegroundColor Cyan
    Write-Host "💻 Console Version (PowerShell)" -ForegroundColor Yellow
    Write-Host ""
}

# Function to check if a command exists
function Test-Command {
    param($Command)
    try {
        if (Get-Command $Command -ErrorAction SilentlyContinue) {
            return $true
        }
    }
    catch {
        return $false
    }
    return $false
}

# Check prerequisites unless skipped
if (-not $SkipChecks) {
    # Check Java
    if (-not (Test-Command "java")) {
        Write-Host "❌ Java is not installed or not in PATH" -ForegroundColor Red
        Write-Host "Please install Java 17 or higher to run LumosPath" -ForegroundColor Yellow
        Write-Host "Recommended: Download from https://adoptium.net/" -ForegroundColor Green
        Read-Host "Press Enter to exit"
        exit 1
    }

    # Check Java version
    try {
        $javaVersion = java -version 2>&1 | Select-String -Pattern 'version' | ForEach-Object { $_.ToString() }
        $versionNumber = [regex]::Match($javaVersion, '"(\d+)').Groups[1].Value
        
        if ([int]$versionNumber -lt 17) {
            Write-Host "⚠️  Java 17 or higher is required. Current version: $versionNumber" -ForegroundColor Yellow
            Write-Host "Please upgrade your Java installation" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        if (-not $Quiet) {
            Write-Host "☑️  Java version check passed (Java $versionNumber)" -ForegroundColor Green
        }
    }
    catch {
        Write-Host "⚠️  Could not determine Java version, continuing..." -ForegroundColor Yellow
    }

    # Check Maven
    if (-not (Test-Command "mvn")) {
        Write-Host "❌ Maven is not installed or not in PATH" -ForegroundColor Red
        Write-Host "Please install Apache Maven to run LumosPath" -ForegroundColor Yellow
        Write-Host "Download from: https://maven.apache.org/download.cgi" -ForegroundColor Green
        Read-Host "Press Enter to exit"
        exit 1
    }

    if (-not $Quiet) {
        Write-Host "☑️  Maven check passed" -ForegroundColor Green
    }
}

# Check if project needs compilation
if (-not (Test-Path "target\classes")) {
    Write-Host "🔧 Compiling application..." -ForegroundColor Yellow
    try {
        $compileResult = mvn compile -q
        if ($LASTEXITCODE -ne 0) {
            Write-Host "❌ Compilation failed" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        Write-Host "✅ Compilation successful" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ Compilation error: $_" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

if (-not $Quiet) {
    Write-Host "🚀 Launching LumosPath Console..." -ForegroundColor Cyan
    Write-Host ""
}

# Set Windows-specific Java options
$env:JAVA_OPTS = "-Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8"

# Run the console application
try {
    mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication" -q
}
catch {
    Write-Host "❌ Error running application: $_" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

if (-not $Quiet) {
    Write-Host ""
    Write-Host "Thank you for using LumosPath! 💙" -ForegroundColor Cyan
    Write-Host "Remember: You are stronger than you think!" -ForegroundColor Green
}

# Keep window open if run by double-clicking
if (-not $env:TERM -and -not $env:WT_SESSION) {
    Read-Host "Press Enter to exit"
}