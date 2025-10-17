#!/bin/bash

# Simple GUI Launcher for LumosPath
echo "🌟 Starting LumosPath Simple GUI..."

# Change to the project directory
PROJECT_DIR="/Users/mkmohan/Desktop/LumosPath"
cd "$PROJECT_DIR" || exit 1

# Check if JavaFX is available
if ! java -cp target/classes -Djava.awt.headless=false com.lumospath.gui.SimpleLauncher --version > /dev/null 2>&1; then
    echo "⚠️ Checking JavaFX installation..."
    
    # Try to detect Java version
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | cut -d'.' -f1)
    echo "📍 Using Java version: $JAVA_VERSION"
    
    # For Java 11+, we might need to add JavaFX modules
    if [ "$JAVA_VERSION" -ge "11" ]; then
        echo "📦 Java 11+ detected - JavaFX might need to be added separately"
    fi
fi

# Set macOS-specific properties for better GUI support
export JAVA_OPTS="-Dapple.awt.application.appearance=system -Dapple.awt.application.name=LumosPath -Djavafx.macosx.embedded=false"

echo "🚀 Launching GUI application..."

# Run the simplified launcher
java $JAVA_OPTS -cp target/classes -Djava.awt.headless=false com.lumospath.gui.SimpleLauncher

echo "✨ GUI session completed!"