package com.lumospath.gui;

import com.lumospath.chatbot.LumosBot;
import com.lumospath.gui.controllers.MainController;
import com.lumospath.service.EmergencyHelplineService;
import com.lumospath.service.MoodTrackingService;
import com.lumospath.service.MotivationalQuoteService;
import com.lumospath.util.DatabaseUtil;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class for LumosPath GUI
 */
public class LumosPathGUI extends Application {
    
    public static void main(String[] args) {
        // Initialize database
        try {
            DatabaseUtil.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
        
        // macOS-specific system properties
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "LumosPath");
        
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("JavaFX launch failed: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to console version
            System.out.println("\n🔄 GUI failed to start. Launching console version...");
            com.lumospath.LumosPathApplication.main(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {
            try {
                // Set application title and icon
                primaryStage.setTitle("LumosPath - Mental Health Support");
            
            // Try to set an icon (will use a simple emoji for now)
            try {
                // You can replace this with an actual icon file later
                primaryStage.getIcons().add(new Image("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMTYiIGN5PSIxNiIgcj0iMTYiIGZpbGw9IiM0Qzc2QUYiLz4KPHN2ZyB4PSI4IiB5PSI4IiB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSI+CjxjaXJjbGUgY3g9IjgiIGN5PSI4IiByPSI4IiBmaWxsPSIjRkZGIi8+Cjwvc3ZnPgo8L3N2Zz4K"));
            } catch (Exception e) {
                // Icon loading failed, continue without icon
            }

            // Initialize services
            MoodTrackingService moodService = new MoodTrackingService();
            MotivationalQuoteService quoteService = new MotivationalQuoteService();
            EmergencyHelplineService helplineService = new EmergencyHelplineService();
            LumosBot chatbot = new LumosBot();

            // Create main controller
            MainController mainController = new MainController(moodService, quoteService, helplineService, chatbot);
            
            // Create and set scene
            Scene scene = mainController.createMainScene();
            // Load the main app stylesheet so custom classes (buttons, containers) are applied
            try {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Could not load styles.css: " + e.getMessage());
            }
            // Try to load the high-visibility overrides to ensure text is readable
            try {
                scene.getStylesheets().add(getClass().getResource("/high-visibility.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Could not load high-visibility.css: " + e.getMessage());
            }
            // Also load the generated button color CSS so each button class has a distinct color
            try {
                scene.getStylesheets().add(getClass().getResource("/generated-button-colors.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Could not load generated-button-colors.css: " + e.getMessage());
            }
            primaryStage.setScene(scene);
            
            // Set minimum size
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(700);
            
            // Center on screen
            primaryStage.centerOnScreen();
            
            // Show the stage
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting JavaFX application: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        // Clean up resources when application is closing
        System.out.println("LumosPath application is closing...");
    }
}