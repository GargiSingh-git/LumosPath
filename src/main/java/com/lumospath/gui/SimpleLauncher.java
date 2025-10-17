package com.lumospath.gui;

import com.lumospath.model.*;
import com.lumospath.service.*;
import com.lumospath.util.DatabaseUtil;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Simplified GUI launcher for better macOS compatibility
 */
public class SimpleLauncher extends Application {
    
    private MotivationalQuoteService quoteService;
    private MoodTrackingService moodService;
    
    public static void main(String[] args) {
        // Set macOS-specific properties
        System.setProperty("javafx.macosx.embedded", "false");
        System.setProperty("apple.awt.application.name", "LumosPath");
        
        try {
            DatabaseUtil.initializeDatabase();
            System.out.println("✅ Database initialized successfully!");
        } catch (Exception e) {
            System.out.println("⚠️ Database warning: " + e.getMessage());
        }
        
        System.out.println("🌟 Starting LumosPath GUI...");
        
        try {
            launch(args);
        } catch (Exception e) {
            System.out.println("❌ GUI failed to start: " + e.getMessage());
            System.out.println("🔄 Starting console version instead...");
            
            // Fallback to console
            com.lumospath.LumosPathApplication.main(args);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize services
            quoteService = new MotivationalQuoteService();
            moodService = new MoodTrackingService();
            
            // Create main layout
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6f3ff);");
            
            // Header
            VBox header = createHeader();
            root.setTop(header);
            
            // Main content
            VBox content = createMainContent();
            root.setCenter(content);
            
            // Create scene
            Scene scene = new Scene(root, 900, 700);
            
            // Load CSS if available
            try {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Note: Custom styling not loaded, using default appearance");
            }
            
            // Setup stage
            primaryStage.setTitle("LumosPath - Mental Health Support");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            
            // Show the application
            primaryStage.show();
            
            System.out.println("🎉 LumosPath GUI started successfully!");
            
        } catch (Exception e) {
            System.err.println("Error in GUI start: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label title = new Label("✨ Welcome to LumosPath ✨");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitle = new Label("Your companion for mental health support and emotional guidance");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
        
        header.getChildren().addAll(title, subtitle);
        
        return header;
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        
        // Daily quote
        VBox quoteBox = createQuoteSection();
        
        // Feature buttons
        GridPane featuresGrid = createFeaturesGrid();
        
        content.getChildren().addAll(quoteBox, featuresGrid);
        
        return content;
    }
    
    private VBox createQuoteSection() {
        VBox quoteBox = new VBox(15);
        quoteBox.setAlignment(Pos.CENTER);
        quoteBox.setPadding(new Insets(25));
        quoteBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        quoteBox.setMaxWidth(700);
        
        Label sectionTitle = new Label("📿 Daily Wisdom");
        sectionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Get a quote
        MotivationalQuote quote = quoteService.getRandomQuote();
        
        Label quoteText = new Label("\"" + quote.getQuote() + "\"");
        quoteText.setStyle("-fx-font-size: 18px; -fx-font-style: italic; -fx-text-fill: #2c3e50; -fx-text-alignment: center; -fx-wrap-text: true;");
        quoteText.setWrapText(true);
        quoteText.setMaxWidth(650);
        
        Label quoteAuthor = new Label("- " + quote.getAuthor() + " (" + quote.getSource().getDisplayName() + ")");
        quoteAuthor.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4A90E2;");
        
        Button newQuoteBtn = new Button("✨ New Quote");
        newQuoteBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #4A90E2, #357ABD); -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 20; -fx-cursor: hand;");
        newQuoteBtn.setOnAction(e -> refreshQuote(quoteText, quoteAuthor));
        
        quoteBox.getChildren().addAll(sectionTitle, quoteText, quoteAuthor, newQuoteBtn);
        
        return quoteBox;
    }
    
    private void refreshQuote(Label quoteText, Label quoteAuthor) {
        MotivationalQuote quote = quoteService.getRandomQuote();
        quoteText.setText("\"" + quote.getQuote() + "\"");
        quoteAuthor.setText("- " + quote.getAuthor() + " (" + quote.getSource().getDisplayName() + ")");
    }
    
    private GridPane createFeaturesGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        
        // Feature cards
        Button moodBtn = createFeatureButton("📊", "Track Mood", "Record how you're feeling today");
        Button quotesBtn = createFeatureButton("💫", "Quotes & Wisdom", "Find inspiration and guidance");
        Button chatBtn = createFeatureButton("🤖", "LumosBot Chat", "Talk to our AI companion");
        Button helpBtn = createFeatureButton("📞", "Emergency Help", "Access crisis support");
        
        // Set actions
        moodBtn.setOnAction(e -> showMoodDialog());
        quotesBtn.setOnAction(e -> showQuotesDialog());
        chatBtn.setOnAction(e -> showChatDialog());
        helpBtn.setOnAction(e -> showHelpDialog());
        
        // Add to grid
        grid.add(moodBtn, 0, 0);
        grid.add(quotesBtn, 1, 0);
        grid.add(chatBtn, 0, 1);
        grid.add(helpBtn, 1, 1);
        
        return grid;
    }
    
    private Button createFeatureButton(String icon, String title, String description) {
        Button button = new Button();
        
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 36px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-text-alignment: center; -fx-wrap-text: true;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(150);
        
        content.getChildren().addAll(iconLabel, titleLabel, descLabel);
        
        button.setGraphic(content);
        button.setPrefSize(200, 150);
        button.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        
        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 15; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(74,144,226,0.3), 12, 0, 0, 5);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: transparent; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);"));
        
        return button;
    }
    
    private void showMoodDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mood Tracking");
        alert.setHeaderText("📊 How are you feeling today?");
        
        StringBuilder moodOptions = new StringBuilder("Available moods:\n\n");
        for (MoodType mood : MoodType.values()) {
            moodOptions.append(mood.toString()).append(" - ").append(mood.getDescription()).append("\n");
        }
        
        alert.setContentText(moodOptions.toString() + "\n💙 Your feelings are valid and important!");
        alert.showAndWait();
    }
    
    private void showQuotesDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Motivational Quotes");
        alert.setHeaderText("💫 Inspirational Wisdom");
        
        MotivationalQuote scripturalQuote = quoteService.getScripturalQuote();
        alert.setContentText("\"" + scripturalQuote.getQuote() + "\"\n\n- " + 
                           scripturalQuote.getAuthor() + " (" + scripturalQuote.getSource().getDisplayName() + ")");
        alert.showAndWait();
    }
    
    private void showChatDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("LumosBot Chat");
        alert.setHeaderText("🤖 AI Companion");
        alert.setContentText("Hello! I'm LumosBot, your companion for emotional support.\n\n" +
                           "I'm here to listen and provide guidance based on ancient wisdom and modern understanding.\n\n" +
                           "How are you feeling today? Remember:\n" +
                           "• Your feelings are valid\n" +
                           "• You're stronger than you think\n" +
                           "• You're not alone in this journey\n\n" +
                           "💙 Take care of yourself!");
        alert.showAndWait();
    }
    
    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Emergency Support");
        alert.setHeaderText("📞 Mental Health Helplines");
        alert.setContentText("🚨 24x7 Crisis Support:\n\n" +
                           "• Vandrevala Foundation: 1860-266-2345\n" +
                           "• Kiran Mental Health: 1800-599-0019\n" +
                           "• Sneha Foundation: 044-24640050\n" +
                           "• AASRA: 022-27546669\n\n" +
                           "💙 Remember: You are not alone!\n" +
                           "These professionals are here to help you.\n\n" +
                           "If you're in crisis, please reach out immediately.");
        alert.showAndWait();
    }
}