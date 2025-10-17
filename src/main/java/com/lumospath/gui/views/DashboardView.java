package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Main dashboard view with feature navigation
 */
public class DashboardView {
    private final MainController controller;
    
    public DashboardView(MainController controller) {
        this.controller = controller;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(30));
        
        // Welcome header
        VBox header = createWelcomeHeader();
        
        // Feature cards grid
        GridPane featuresGrid = createFeaturesGrid();

        VBox mainContent = new VBox(10);
        mainContent.getChildren().addAll(header, featuresGrid);
        container.getChildren().add(mainContent);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private VBox createWelcomeHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        String username = controller.getCurrentUser() != null ? 
                         controller.getCurrentUser().getUsername() : "User";
        
        Label welcomeLabel = new Label("Welcome back, " + username + "! 🌟");
        welcomeLabel.getStyleClass().add("header-title");
        
        Label subtitleLabel = new Label("How can LumosPath support you today?");
        subtitleLabel.getStyleClass().add("header-subtitle");
        
        header.getChildren().addAll(welcomeLabel, subtitleLabel);
        
        return header;
    }
    
    private GridPane createFeaturesGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
        
        // Create feature cards with enhanced styling
        VBox moodCard = createFeatureCard(
            "📈", 
            "Mood Tracking",
            "Record and track your emotional journey",
            () -> controller.showMoodTrackingView(),
            "mood-card"
        );
        
        VBox chatCard = createFeatureCard(
            "🤖", 
            "LumosBot Chat",
            "Talk to our AI companion for emotional support",
            () -> controller.showChatView(),
            "chat-card"
        );
        
        VBox helplineCard = createFeatureCard(
            "📞", 
            "Emergency Support",
            "Access mental health helplines and crisis support",
            () -> controller.showHelplineView(),
            "help-card"
        );
        
        VBox meditationCard = createFeatureCard(
            "🧘‍♀️", 
            "Meditation",
            "Guided sessions for calm, focus, and emotional balance",
            () -> controller.showMeditationView(),
            "meditation-card"
        );
        
        VBox progressCard = createFeatureCard(
            "📈", 
            "Your Progress",
            "View your mood trends and journey insights",
            () -> controller.showInfoAlert("Coming Soon", "Progress tracking features are being developed!"),
            "progress-card"
        );
        
        VBox resourcesCard = createFeatureCard(
            "📚", 
            "Resources",
            "Articles and guides for mental wellness",
            () -> controller.showInfoAlert("Coming Soon", "Resource library is being curated for you!"),
            "resources-card"
        );
        
        // Add cards to grid
        grid.add(moodCard, 0, 0);
        grid.add(chatCard, 1, 0);
        grid.add(helplineCard, 2, 0);
        grid.add(meditationCard, 0, 1);
        grid.add(progressCard, 1, 1);
        grid.add(resourcesCard, 2, 1);
        
        return grid;
    }
    
    private VBox createFeatureCard(String icon, String title, String description, Runnable action, String customStyleClass) {
        VBox card = new VBox(16);
        card.getStyleClass().addAll("nav-card", customStyleClass);
        card.setAlignment(Pos.CENTER);
        card.setOnMouseClicked(e -> action.run());
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("nav-card-icon");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("nav-card-title");
        
        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("nav-card-description");
        descLabel.setMaxWidth(250);
        
        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        
        return card;
    }
    
}