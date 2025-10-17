package com.lumospath;

import com.lumospath.chatbot.LumosBot;
import com.lumospath.model.User;
import com.lumospath.service.MoodTrackingService;
import com.lumospath.service.MotivationalQuoteService;
import com.lumospath.service.EmergencyHelplineService;
import com.lumospath.util.DatabaseUtil;

import java.util.Scanner;

/**
 * Main application class for LumosPath - Mental Health Support System
 * 
 * LumosPath helps users manage depressive phases through motivation, support, and guidance.
 * Features include mood tracking, motivational quotes, chatbot support, and emergency helplines.
 */
public class LumosPathApplication {
    private Scanner scanner;
    private User currentUser;
    private MoodTrackingService moodService;
    private MotivationalQuoteService quoteService;
    private EmergencyHelplineService helplineService;
    private LumosBot chatbot;
    
    public LumosPathApplication() {
        this.scanner = new Scanner(System.in);
        this.moodService = new MoodTrackingService();
        this.quoteService = new MotivationalQuoteService();
        this.helplineService = new EmergencyHelplineService();
        this.chatbot = new LumosBot();
    }

    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        LumosPathApplication app = new LumosPathApplication();
        app.start();
    }

    /**
     * Start the application
     */
    public void start() {
        // Initialize database
        DatabaseUtil.initializeDatabase();
        
        // Display welcome message
        displayWelcomeMessage();
        
        // Create or get user
        setupUser();
        
        // Display daily wisdom
        quoteService.displayDailyWisdom();
        
        // Main application loop
        runMainLoop();
        
        System.out.println("\n🌟 Thank you for using LumosPath. Take care of yourself!");
        System.out.println("💙 Remember: You are stronger than you think, and you are not alone.");
    }

    /**
     * Display welcome message and app introduction
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✨                           WELCOME TO LUMOSPATH                           ✨");
        System.out.println("=".repeat(80));
        System.out.println("🌟 Your companion for mental health support and emotional guidance");
        System.out.println("💙 Helping you navigate through life's challenges with wisdom and care");
        System.out.println("📿 Featuring scriptural wisdom, mood tracking, and 24/7 support");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("🎯 Purpose: To help users manage depressive phases through motivation,");
        System.out.println("           support, and guidance from ancient wisdom and modern care.");
        System.out.println();
        System.out.println("👥 Designed for: Students, young adults, and anyone experiencing stress");
        System.out.println("                (academic, personal, financial, or emotional)");
        System.out.println();
        System.out.println("🔒 Privacy: Your conversations and mood data are kept private and secure.");
        System.out.println("=".repeat(80));
    }

    /**
     * Setup user - either create new or continue as anonymous
     */
    private void setupUser() {
        System.out.println("\n=== User Setup ===");
        System.out.println("1. 👤 Create a profile (optional - helps personalize your experience)");
        System.out.println("2. 🕶️  Continue anonymously");
        
        System.out.print("\nChoose an option (1-2): ");
        String choice = scanner.nextLine().trim();
        
        if ("1".equals(choice)) {
            createUserProfile();
        } else {
            createAnonymousUser();
        }
        
        System.out.println("\n✅ Welcome" + (currentUser.isAnonymous() ? "" : ", " + currentUser.getUsername()) + "!");
    }

    /**
     * Create user profile
     */
    private void createUserProfile() {
        System.out.println("\n📝 Creating your profile (all fields are optional):");
        
        System.out.print("Enter your preferred name: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            username = "User" + System.currentTimeMillis() % 1000;
        }
        
        System.out.print("Enter your age (or skip): ");
        String ageStr = scanner.nextLine().trim();
        int age = 0;
        try {
            if (!ageStr.isEmpty()) {
                age = Integer.parseInt(ageStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format, skipping...");
        }
        
        System.out.print("Enter your location/city (or skip): ");
        String location = scanner.nextLine().trim();
        if (location.isEmpty()) {
            location = "Not specified";
        }
        
        currentUser = new User(username, "", age, location);
        currentUser.setUserId(1); // Simple ID assignment for demo
    }

    /**
     * Create anonymous user
     */
    private void createAnonymousUser() {
        currentUser = new User();
        currentUser.setUserId(0); // Anonymous user ID
        currentUser.setUsername("Anonymous User");
        currentUser.setAnonymous(true);
        System.out.println("\n🕶️  Continuing as anonymous user. Your privacy is protected.");
    }

    /**
     * Main application loop
     */
    private void runMainLoop() {
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    // Record how user feels
                    moodService.recordMood(currentUser.getUserId());
                    break;
                    
                case "2":
                    // View mood history
                    moodService.showMoodHistory(currentUser.getUserId());
                    break;
                    
                case "3":
                    // Motivational quotes and wisdom
                    handleQuoteMenu();
                    break;
                    
                case "4":
                    // Chat with LumosBot
                    System.out.println("\n🤖 Starting conversation with LumosBot...");
                    chatbot.startConversation();
                    break;
                    
                case "5":
                    // Emergency helplines
                    handleHelplineMenu();
                    break;
                    
                case "6":
                    // Quick crisis support
                    helplineService.displayCrisisHelplines();
                    break;
                    
                case "7":
                    // About & Help
                    displayAboutInfo();
                    break;
                    
                case "8":
                case "q":
                case "quit":
                case "exit":
                    running = false;
                    break;
                    
                default:
                    System.out.println("❌ Invalid option. Please choose 1-8.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌟 LUMOSPATH - Main Menu");
        System.out.println("=".repeat(60));
        System.out.println("1. 📊 How are you feeling today? (Record mood)");
        System.out.println("2. 📈 View your mood journey");
        System.out.println("3. 💫 Motivational quotes & wisdom");
        System.out.println("4. 🤖 Chat with LumosBot (AI support)");
        System.out.println("5. 📞 Emergency helplines & support");
        System.out.println("6. 🚨 CRISIS SUPPORT (immediate help)");
        System.out.println("7. ℹ️  About & Help");
        System.out.println("8. 👋 Exit");
        System.out.println("=".repeat(60));
        System.out.print("Select an option (1-8): ");
    }

    /**
     * Handle quote menu interactions
     */
    private void handleQuoteMenu() {
        boolean inQuoteMenu = true;
        
        while (inQuoteMenu) {
            quoteService.showQuoteMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    quoteService.displayRandomQuote();
                    break;
                case "2":
                    quoteService.displayDailyWisdom();
                    break;
                case "3":
                    System.out.print("What mood would you like help with? (sad/anxious/stressed/etc.): ");
                    String mood = scanner.nextLine().trim();
                    quoteService.displayQuoteForMood(mood);
                    break;
                case "4":
                    System.out.println("\n📚 Available categories: " + 
                                     String.join(", ", quoteService.getAvailableCategories()));
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine().trim();
                    quoteService.displayQuoteForMood(category);
                    break;
                case "5":
                    inQuoteMenu = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose 1-5.");
            }
            
            if (inQuoteMenu) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Handle helpline menu interactions
     */
    private void handleHelplineMenu() {
        boolean inHelplineMenu = true;
        
        while (inHelplineMenu) {
            helplineService.showHelplineMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    helplineService.displayCrisisHelplines();
                    break;
                case "2":
                    helplineService.displayAllHelplines();
                    break;
                case "3":
                    System.out.println("\n🌍 Available locations: " + 
                                     String.join(", ", helplineService.getAvailableLocations()));
                    System.out.print("Enter your location: ");
                    String location = scanner.nextLine().trim();
                    helplineService.displayHelplinesByLocation(location);
                    break;
                case "4":
                    System.out.println("\n🎯 Available types:");
                    helplineService.getAvailableContactTypes().forEach(type -> 
                        System.out.println("  • " + type.getDisplayName()));
                    System.out.print("Enter type name: ");
                    String typeName = scanner.nextLine().trim();
                    // Find matching type (simplified)
                    helplineService.getAvailableContactTypes().stream()
                        .filter(type -> type.getDisplayName().toLowerCase().contains(typeName.toLowerCase()))
                        .findFirst()
                        .ifPresentOrElse(
                            type -> helplineService.displayHelplinesByType(type),
                            () -> System.out.println("❌ Type not found. Showing all helplines:")
                        );
                    if (helplineService.getAvailableContactTypes().stream()
                        .noneMatch(type -> type.getDisplayName().toLowerCase().contains(typeName.toLowerCase()))) {
                        helplineService.displayAllHelplines();
                    }
                    break;
                case "5":
                    inHelplineMenu = false;
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose 1-5.");
            }
            
            if (inHelplineMenu) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Display about information
     */
    private void displayAboutInfo() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ℹ️  ABOUT LUMOSPATH");
        System.out.println("=".repeat(70));
        System.out.println("🎯 Mission: To provide accessible, personalized mental health support");
        System.out.println("           through technology, ancient wisdom, and modern care.");
        System.out.println();
        System.out.println("✨ Features:");
        System.out.println("   • 📊 Mood tracking to understand your emotional patterns");
        System.out.println("   • 💫 Scriptural wisdom from Bhagavad Gita & Srimad Bhagavatam");
        System.out.println("   • 🤖 AI chatbot for 24/7 emotional support");
        System.out.println("   • 📞 Indian mental health helplines directory");
        System.out.println("   • 🚨 Crisis intervention support");
        System.out.println();
        System.out.println("🛡️  Privacy: Your data stays on your device. We prioritize your privacy.");
        System.out.println();
        System.out.println("⚠️  Important: LumosPath is a support tool, not a replacement for");
        System.out.println("   professional mental health care. If you're in crisis, please");
        System.out.println("   contact emergency services or mental health professionals.");
        System.out.println();
        System.out.println("🙏 Developed with care for students, young adults, and anyone");
        System.out.println("   facing mental health challenges.");
        System.out.println("=".repeat(70));
    }
}