# LumosPath - Cross-Platform Setup Guide

## 🌍 Platform Support
LumosPath works seamlessly on:
- ✅ **Windows** (Windows 10/11)
- ✅ **macOS** (macOS 10.15+)  
- ✅ **Linux** (Ubuntu, CentOS, etc.)

---

## 📋 Prerequisites

### Required Software

#### Java 17 or Higher
**Windows:**
```batch
# Download from Eclipse Adoptium
# https://adoptium.net/temurin/releases/?version=17
# Or use Chocolatey:
choco install temurin17
```

**macOS:**
```bash
# Using Homebrew (recommended):
brew install openjdk@17

# Or download from: https://adoptium.net/temurin/releases/?version=17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Linux (CentOS/RHEL):**
```bash
sudo yum install java-17-openjdk-devel
```

#### Apache Maven
**Windows:**
```batch
# Download from https://maven.apache.org/download.cgi
# Or use Chocolatey:
choco install maven
```

**macOS:**
```bash
# Using Homebrew:
brew install maven

# Or download from: https://maven.apache.org/download.cgi
```

**Linux:**
```bash
# Ubuntu/Debian:
sudo apt install maven

# CentOS/RHEL:
sudo yum install maven
```

---

## 🚀 Quick Start

### 1. Download/Clone the Project
```bash
# If using Git:
git clone <repository-url>
cd LumosPath

# Or extract the downloaded ZIP file and navigate to the folder
```

### 2. Run LumosPath

#### 🖥️ GUI Version (Recommended)

**Windows:**
```batch
# Double-click run-gui.bat or run in Command Prompt:
run-gui.bat
```

**macOS/Linux:**
```bash
# Make executable and run:
chmod +x run-gui.sh
./run-gui.sh
```

#### 💻 Console Version (Alternative)

**Windows:**
```batch
# Double-click run-console.bat or run in Command Prompt:
run-console.bat
```

**macOS/Linux:**
```bash
# Make executable and run:
chmod +x run-console.sh
./run-console.sh
```

---

## 🔧 Manual Installation

If the automated scripts don't work, you can run manually:

### 1. Compile the Project
```bash
mvn clean compile
```

### 2. Run GUI Version
```bash
mvn javafx:run
# or
mvn exec:java -Dexec.mainClass="com.lumospath.gui.LumosPathGUI"
```

### 3. Run Console Version
```bash
mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication"
```

---

## 🐛 Troubleshooting

### Common Issues

#### "Java not found" Error
**Solution:** Make sure Java 17+ is installed and in your PATH
```bash
# Check Java version:
java -version

# Should show version 17 or higher
```

#### "Maven not found" Error  
**Solution:** Install Apache Maven and add to PATH
```bash
# Check Maven installation:
mvn -version
```

#### JavaFX Runtime Components Missing
**Solution:** The project includes JavaFX dependencies, but if you still get this error:

**Windows:**
```batch
# Add JavaFX module path:
java --module-path "path\to\javafx\lib" --add-modules javafx.controls,javafx.fxml -cp target\classes com.lumospath.gui.LumosPathGUI
```

**macOS/Linux:**
```bash
# Add JavaFX module path:
java --module-path "/path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml -cp target/classes com.lumospath.gui.LumosPathGUI
```

#### GUI Won't Start on Linux
**Solution:** Install JavaFX for your Linux distribution:
```bash
# Ubuntu/Debian:
sudo apt install openjfx

# CentOS/RHEL:
sudo yum install java-17-openjdk-jmods
```

---

## 🎯 Platform-Specific Notes

### Windows
- Use `.bat` scripts for easy launching
- Scripts include automatic dependency checking
- Works with both Command Prompt and PowerShell
- GUI requires Windows 10/11 with display

### macOS  
- Use `.sh` scripts for launching
- Homebrew installation recommended for dependencies
- Works on macOS 10.15+ (Catalina and newer)
- May require allowing unsigned apps in Security settings

### Linux
- Use `.sh` scripts for launching
- Different package managers supported (apt, yum, etc.)
- GUI requires X11 or Wayland display server
- Console version works on headless servers

---

## 📱 Features Across Platforms

All platforms support:
- ✅ **GUI Interface** - Beautiful JavaFX interface
- ✅ **Console Interface** - Text-based fallback
- ✅ **Mood Tracking** - Interactive emotional journaling  
- ✅ **Scriptural Wisdom** - Quotes from Bhagavad Gita & Srimad Bhagavatam
- ✅ **AI Chatbot** - LumosBot for emotional support
- ✅ **Emergency Helplines** - Indian mental health support directory
- ✅ **Privacy Protection** - Local data storage, anonymous usage
- ✅ **Database Support** - SQLite (cross-platform)

---

## 🔐 Security & Privacy

- 🔒 **All data stays local** - No data sent to external servers
- 🕶️ **Anonymous usage** - No personal information required
- 💾 **Local database** - SQLite database stored on your device
- 🛡️ **No internet required** - Works completely offline

---

## 📞 Support

If you encounter any platform-specific issues:

1. **Check Prerequisites** - Ensure Java 17+ and Maven are installed
2. **Try Console Version** - If GUI fails, console version usually works
3. **Check Permissions** - Ensure scripts have execute permissions (Mac/Linux)
4. **Verify PATH** - Make sure Java and Maven are in your system PATH

---

## 🌟 Quick Test

To verify everything is working, run the demo:

**All Platforms:**
```bash
javac -cp target/classes demo.java
java -cp "target/classes:." demo
```

This will show LumosPath features without requiring GUI or full interaction.

---

**💙 Remember: You are stronger than you think, and you are not alone!**

*LumosPath - Your companion for mental health support across all platforms* ✨