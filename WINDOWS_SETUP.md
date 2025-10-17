# 🖥️ LumosPath - Windows Setup Guide

## 🎯 Windows Compatibility

LumosPath is fully compatible with **Windows 10** and **Windows 11** systems. Both console and GUI versions work seamlessly on Windows.

## 📋 Prerequisites

### 1. Java 17 or Higher
**Required for running the application**

#### Option A: Eclipse Adoptium (Recommended)
1. Download from: https://adoptium.net/
2. Select "OpenJDK 17 (LTS)" or later
3. Choose "Windows" and your architecture (x64 for most systems)
4. Run the installer and follow the setup wizard

#### Option B: Oracle JDK
1. Download from: https://www.oracle.com/java/technologies/downloads/
2. Select "Java 17" or later
3. Download the Windows installer
4. Run and follow the installation steps

#### Verify Java Installation
Open Command Prompt and run:
```cmd
java -version
```
You should see Java 17 or higher.

### 2. Apache Maven
**Required for building and running the application**

1. Download from: https://maven.apache.org/download.cgi
2. Download the "Binary zip archive" (e.g., `apache-maven-3.9.x-bin.zip`)
3. Extract to a folder like `C:\Program Files\Apache\maven`
4. Add Maven to your PATH:
   - Open "Environment Variables" in Windows Settings
   - Add `C:\Program Files\Apache\maven\bin` to your PATH
   - Restart Command Prompt

#### Verify Maven Installation
Open Command Prompt and run:
```cmd
mvn -version
```

## 🚀 Running LumosPath on Windows

### Method 1: Double-Click Batch Files (Easiest)

1. **Console Version**: Double-click `run-console.bat`
2. **GUI Version**: Double-click `run-gui.bat`

### Method 2: Command Prompt

1. Open Command Prompt
2. Navigate to the LumosPath directory:
   ```cmd
   cd C:\path\to\LumosPath
   ```
3. Run your preferred version:
   ```cmd
   :: Console version
   run-console.bat
   
   :: GUI version
   run-gui.bat
   ```

### Method 3: Maven Commands (Manual)

```cmd
:: Compile the project
mvn compile

:: Run console version
mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication"

:: Run GUI version
mvn javafx:run
```

## 🎮 Windows-Specific Features

### Console Version
- **Full Unicode Support**: Emojis and special characters display properly in Windows Terminal
- **Color Support**: Enhanced with Windows Terminal or modern command prompts
- **Keyboard Navigation**: Standard Windows key combinations work

### GUI Version
- **Windows Native Look**: Integrates with Windows 10/11 visual style
- **System Tray Support**: Minimize to system tray functionality
- **Windows Notifications**: Native Windows notifications for reminders
- **File Association**: Associate `.lumos` files with the application

## 🐛 Troubleshooting

### Common Issues

#### 1. "Java is not recognized"
**Solution**: Java is not in your PATH
- Reinstall Java with the installer (not just extracting)
- Manually add Java bin directory to PATH
- Restart Command Prompt after changes

#### 2. "Maven is not recognized"
**Solution**: Maven is not in your PATH
- Download Maven binary ZIP
- Extract to a permanent location
- Add `bin` folder to Windows PATH
- Restart Command Prompt

#### 3. JavaFX GUI Won't Start
**Solutions**:
1. Use the console version: `run-console.bat`
2. Update to latest Java version
3. Try running with explicit module path:
   ```cmd
   java --module-path "C:\path\to\javafx\lib" --add-modules javafx.controls,javafx.fxml -cp target\classes com.lumospath.gui.LumosPathGUI
   ```

#### 4. Unicode/Emoji Display Issues
**Solutions**:
1. Use Windows Terminal (recommended over Command Prompt)
2. Set console font to one that supports Unicode
3. Enable UTF-8 support in Windows:
   - Control Panel → Region → Administrative → Change system locale
   - Check "Beta: Use Unicode UTF-8 for worldwide language support"

#### 5. Database Permission Errors
**Solutions**:
1. Run as Administrator (if needed)
2. Ensure the application folder has write permissions
3. Check antivirus isn't blocking database creation

### Performance Tips

1. **Use Windows Terminal** instead of Command Prompt for better display
2. **Add JVM arguments** for better performance:
   ```cmd
   set JAVA_OPTS=-Xmx512m -XX:+UseG1GC
   ```
3. **Run from SSD** for faster startup times

## 🔧 Development on Windows

### IDE Setup
- **IntelliJ IDEA**: Import as Maven project
- **Eclipse**: Import as "Existing Maven Project"
- **Visual Studio Code**: Install Java extension pack

### Building Executable
Create a standalone JAR:
```cmd
mvn clean package
java -jar target/lumos-path-1.0.0.jar
```

### Creating Windows Installer
Use tools like:
- **jpackage** (included with Java 14+)
- **Launch4j** for EXE wrapper
- **Inno Setup** for installer creation

## 📊 Windows Testing Checklist

- [x] Console application starts and displays properly
- [x] GUI application launches without errors  
- [x] Unicode characters (emojis) display correctly
- [x] Database creation and access works
- [x] File paths use Windows conventions (backslashes)
- [x] Batch scripts handle errors gracefully
- [x] Application works with Windows Defender
- [x] Compatible with both Windows 10 and 11

## 🆘 Support

If you encounter issues on Windows:

1. **Check Prerequisites**: Ensure Java 17+ and Maven are properly installed
2. **Use Console Version**: If GUI has issues, console version should always work
3. **Check Logs**: Look for error messages in the console output
4. **Windows Terminal**: Use modern terminal for better compatibility

## 🎉 Windows-Optimized Features

- **Batch file launchers** for easy startup
- **Windows path handling** for database and configuration files
- **Native Windows look and feel** in GUI version
- **PowerShell compatibility** for advanced users
- **Windows Service potential** for background operation

---

**Enjoy using LumosPath on Windows! 💙**