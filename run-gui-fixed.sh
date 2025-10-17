#!/bin/bash

# LumosPath Fixed GUI Launcher for macOS
# This version includes JVM arguments to prevent macOS-specific crashes

echo "🌟 Starting LumosPath - Mental Health Support Application..."
echo "🖥️  GUI Version (macOS Optimized)"
echo ""

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 17 or higher to run LumosPath"
    echo "Mac: brew install openjdk@17"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "⚠️  Java 17 or higher is required. Current version: $JAVA_VERSION"
    echo "Please upgrade your Java installation"
    exit 1
fi

echo "☑️  Java version check passed"

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed or not in PATH"
    echo "Please install Apache Maven to run LumosPath"
    echo "Mac: brew install maven"
    exit 1
fi

echo "☑️  Maven check passed"

# Compile if needed
if [ ! -d "target/classes" ]; then
    echo "🔧 Compiling application..."
    mvn compile
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed"
        exit 1
    fi
fi

echo "🚀 Launching LumosPath GUI (macOS Optimized)..."
echo ""

# macOS-specific JVM arguments to prevent crashes
export MAVEN_OPTS="-Xmx2g \
    -Dprism.order=sw \
    -Dprism.text=t2k \
    -Djavafx.animation.fullspeed=true \
    -Djava.awt.headless=false \
    --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED \
    --add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED \
    --add-opens java.desktop/sun.awt=ALL-UNNAMED \
    --add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED \
    --add-exports javafx.graphics/com.sun.glass.ui.mac=ALL-UNNAMED \
    --enable-native-access=ALL-UNNAMED"

# Run the GUI application
mvn javafx:run

echo ""
echo "Thank you for using LumosPath! 💙"
echo "Remember: You are stronger than you think!"