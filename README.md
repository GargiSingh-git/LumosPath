# LumosPath - Mental Health Support Application

**✨ Your companion for mental health support and emotional guidance**

LumosPath is a Java-based mental health support application designed to help users manage depressive phases through motivation, support, and guidance. The application combines ancient scriptural wisdom with modern technology to provide accessible, personalized mental health support.

## 🎯 Purpose

Help users manage depressive phases through:
- **Motivation** from scriptural texts and inspirational content
- **Support** through AI chatbot and emergency helplines  
- **Guidance** from ancient wisdom and modern mental health practices

## 👥 Target Users

- Students facing academic stress
- Young adults dealing with life transitions
- Anyone experiencing stress (academic, personal, financial, emotional)
- Individuals seeking accessible mental health support

## ✨ Key Features

### 📊 Mood Tracking
- Interactive mood entry system
- Emotional state tracking with 1-10 scale
- Mood history and trend analysis
- Trigger identification and pattern recognition

### 💫 Motivational Content
- Daily scriptural wisdom from Bhagavad Gita and Srimad Bhagavatam
- Categorized motivational quotes for different moods
- Inspirational content from various sources
- Mood-specific quote recommendations

### 🤖 LumosBot - AI Chatbot
- 24/7 emotional support through conversational AI
- Scriptural guidance integration
- Empathetic responses for various emotional states
- Crisis detection and supportive responses

### 📞 Emergency Support
- Comprehensive Indian mental health helplines directory
- Location-based helpline recommendations
- 24x7 crisis support contacts
- Specialized helplines for different needs (suicide prevention, anxiety, etc.)

### 🔒 Privacy & Security
- Anonymous usage option
- Local data storage
- No personal data sharing
- User privacy protection

## 🏗️ Technical Architecture

### Backend (Java - 80% of codebase)
- **Core Language**: Java 17+
- **Framework**: Built for future Spring Boot integration
- **Database**: SQLite (JDBC connectivity) - easily switchable to MySQL/PostgreSQL
- **Architecture**: MVC Pattern for organized, scalable code
- **Build Tool**: Maven

### Project Structure
```
LumosPath/
├── src/main/java/com/lumospath/
│   ├── model/              # Data models (User, MoodEntry, etc.)
│   ├── service/            # Business logic services
│   ├── chatbot/            # AI chatbot implementation
│   ├── util/               # Database and utility classes
│   └── LumosPathApplication.java  # Main application class
├── src/main/resources/     # Configuration files
├── src/test/java/          # Unit tests
├── pom.xml                 # Maven configuration
└── README.md               # This file
```

## 🚀 Getting Started

### 🌍 Platform Support
LumosPath works on **Windows**, **macOS**, and **Linux**!

#### ✅ Windows Compatibility
- **Windows 10** and **Windows 11** fully supported
- Easy-to-use `.bat` batch files for quick launching
- PowerShell script available for advanced users
- Comprehensive Windows setup guide: [`WINDOWS_SETUP.md`](WINDOWS_SETUP.md)
- Windows-specific testing script: `windows-test.bat`

**Quick Start on Windows:**
```cmd
# Test compatibility first
windows-test.bat

# Run the console application
run-console.bat

# Or try the GUI version
run-gui.bat
```

### Prerequisites
- **Java 17 or higher** (Required)
- **Apache Maven 3.6+** (Required)
- **Display environment** for GUI (GUI version)

📋 **Detailed setup instructions**: See [`SETUP.md`](SETUP.md) for platform-specific installation guides.

### Installation & Setup

1. **Clone or navigate to the project directory:**
   ```bash
   cd LumosPath
   ```

2. **Compile the project:**
   ```bash
   mvn compile
   ```

3. **Run the Application:**

   **🖥️ GUI Version (Recommended):**
   ```bash
   # Windows:
   run-gui.bat
   
   # macOS/Linux:
   ./run-gui.sh
   
   # Manual (all platforms):
   mvn javafx:run
   ```

   **💻 Console Version (Alternative):**
   ```bash
   # Windows:
   run-console.bat
   
   # macOS/Linux:
   ./run-console.sh
   
   # Manual (all platforms):
   mvn exec:java -Dexec.mainClass="com.lumospath.LumosPathApplication"
   ```

5. **Build JAR file (optional):**
   ```bash
   mvn clean package
   java -jar target/lumos-path-1.0.0.jar
   ```

### First Time Setup
1. When you first run the application, it will initialize the SQLite database
2. **GUI Version**: Use the beautiful graphical interface with:
   - Welcome screen for user setup
   - Interactive dashboard with feature cards
   - Visual mood selectors with emojis
   - Attractive quote displays
   - Chat interface for LumosBot
   - Emergency helpline directory
3. **Console Version**: Follow the text-based interactive prompts
4. Choose whether to create a user profile or continue anonymously

## 📱 Usage Guide

### GUI Interface Features:
- **🏠 Welcome Screen**: Beautiful introduction with user setup options
- **🎯 Dashboard**: Central hub with feature cards for easy navigation
- **📊 Interactive Mood Tracking**: Visual mood selectors with emojis and sliders
- **💫 Quote Gallery**: Elegant display of inspirational quotes and wisdom
- **💬 Chat Interface**: Clean, modern chat window for LumosBot conversations
- **📞 Helpline Directory**: Organized emergency contact information
- **🎨 Beautiful Styling**: Calming color palette designed for mental wellness

### Main Features:
1. **📊 Mood Tracking** - Record how you're feeling today with visual selectors
2. **📈 Mood Journey** - View your emotional patterns over time
3. **💫 Motivational Content** - Access quotes and scriptural wisdom
4. **🤖 LumosBot Chat** - Interact with the AI support chatbot
5. **📞 Emergency Helplines** - Access mental health support contacts
6. **🚨 Crisis Support** - Immediate crisis intervention resources

### Mood Tracking
- Select from 10 different mood types with emoji indicators
- Rate intensity on a 1-10 scale
- Add optional descriptions and trigger identification
- View mood trends and patterns over time

### Chatbot Interaction
- Type naturally to interact with LumosBot
- Use keywords like 'quote', 'wisdom', 'help' for specific responses
- Type 'quit' or 'exit' to end the conversation
- Receive scriptural guidance and emotional support

### Emergency Support
- Access 24x7 crisis helplines immediately
- Browse helplines by location or type of support needed
- Get Indian mental health helpline directory

## 🛡️ Privacy & Data

- **Local Storage**: All data is stored locally in SQLite database
- **Anonymous Option**: Use the app without creating a profile
- **No Data Sharing**: Personal information never leaves your device
- **Secure**: No internet connection required for core functionality

## ⚠️ Important Disclaimer

LumosPath is a **support tool and companion**, not a replacement for professional mental health care. If you are experiencing:
- Suicidal thoughts or ideation
- Severe depression or anxiety
- Mental health crisis

**Please contact:**
- Emergency services (112 in India)
- Mental health professionals
- Crisis helplines provided in the app

## 🔧 Development

### Key Classes:
- `LumosPathApplication.java` - Main application entry point
- `MoodTrackingService.java` - Handles mood entry and tracking
- `MotivationalQuoteService.java` - Manages inspirational content
- `LumosBot.java` - AI chatbot implementation
- `EmergencyHelplineService.java` - Emergency contact management

### Database Schema:
- Users table for user profiles
- Mood entries for tracking emotional states
- Motivational quotes repository
- Emergency contacts directory
- Chat logs for bot interactions

### Future Enhancements:
- Web interface using JSP/Servlets
- Spring Boot REST API integration
- MySQL/PostgreSQL database support
- JavaFX desktop GUI
- Advanced analytics and reporting

## 🙏 Acknowledgments

This project incorporates wisdom from:
- **Bhagavad Gita** - Ancient Sanskrit scripture
- **Srimad Bhagavatam** - Vedic literature
- Various motivational speakers and mental health resources
- Indian mental health helplines and crisis support services

## 📄 License

This project is developed for educational and mental health support purposes. Please use responsibly and seek professional help when needed.

---

**🌟 Remember: You are stronger than you think, and you are not alone. 💙**

For support or questions about this application, please refer to the emergency helplines provided within the app or consult with mental health professionals.#   L u m o s P a t h  
 