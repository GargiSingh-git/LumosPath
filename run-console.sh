#!/bin/bash

# LumosPath Console Launcher Script for Mac/Linux
echo "🌟 Starting LumosPath - Mental Health Support Application..."
echo "💻 Console Version"
echo ""

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install Java 17 or higher to run LumosPath"
    echo "Mac: brew install openjdk@17"
    echo "Linux: sudo apt install openjdk-17-jdk"
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
    echo "Linux: sudo apt install maven"
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

echo "🚀 Launching LumosPath Console..."
echo ""

# Run the console application
mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication"

echo ""
echo "Thank you for using LumosPath! 💙"
echo "Remember: You are stronger than you think!"