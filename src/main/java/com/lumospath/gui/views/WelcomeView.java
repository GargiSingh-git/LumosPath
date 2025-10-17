package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Welcome screen for user setup and introduction
 */
public class WelcomeView {
    private final MainController controller;
    
    public WelcomeView(MainController controller) {
        this.controller = controller;
    }
    
    public VBox createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("welcome-container");
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        
        // Welcome header
        VBox header = createHeader();
        
        // Welcome card with options
        VBox card = createWelcomeCard();
        
        container.getChildren().addAll(header, card);
        
        return container;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(20);
        header.setAlignment(Pos.CENTER);
        
        Label title = new Label("✨ Welcome to LumosPath ✨");
        title.getStyleClass().add("welcome-title");
        
        Label subtitle = new Label("Your companion for mental health support and emotional guidance");
        subtitle.getStyleClass().add("welcome-subtitle");
        subtitle.setMaxWidth(600);
        
    Label description = new Label("Helping you navigate through life's challenges with wisdom, care,\nand scriptural guidance from ancient texts");
    description.getStyleClass().addAll("welcome-subtitle", "card-description");
        
        header.getChildren().addAll(title, subtitle, description);
        
        return header;
    }
    
    private VBox createWelcomeCard() {
        VBox card = new VBox(25);
        card.getStyleClass().add("welcome-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(500);
        
    Label cardTitle = new Label("🎯 Get Started");
    cardTitle.getStyleClass().add("card-title");
        
    Label cardDescription = new Label("Choose how you'd like to begin your journey with LumosPath:");
    cardDescription.getStyleClass().add("card-description");
    cardDescription.setMaxWidth(400);
        
        // Authentication options
        VBox optionsContainer = new VBox(15);
        optionsContainer.setAlignment(Pos.CENTER);
        
        // Sign in button
        Button signInBtn = new Button("🔐 Sign In");
        signInBtn.getStyleClass().add("sign-in-button");
        signInBtn.setPrefWidth(240);
        signInBtn.setPrefHeight(48);
        signInBtn.setOnAction(e -> controller.showLoginView());
        
    Label signInDesc = new Label("Sign in to your existing account");
    signInDesc.getStyleClass().addAll("form-label", "muted-text");
        
        // Sign up button
        Button signUpBtn = new Button("✨ Create Account");
        signUpBtn.getStyleClass().add("create-account-button");
        signUpBtn.setPrefWidth(240);
        signUpBtn.setPrefHeight(48);
        signUpBtn.setOnAction(e -> controller.showSignUpView());
        
    Label signUpDesc = new Label("Join LumosPath with a new account");
    signUpDesc.getStyleClass().addAll("form-label", "muted-text");
        
        // Guest button
        Button guestBtn = new Button("🕶️ Continue as Guest");
        guestBtn.getStyleClass().add("guest-button");
        guestBtn.setPrefWidth(240);
        guestBtn.setPrefHeight(42);
        guestBtn.setOnAction(e -> continueAnonymously());
        
    Label guestDesc = new Label("Use LumosPath without creating an account");
    guestDesc.getStyleClass().add("muted-small");
        
        VBox signInOption = new VBox(8);
        signInOption.setAlignment(Pos.CENTER);
        signInOption.getChildren().addAll(signInBtn, signInDesc);
        
        VBox signUpOption = new VBox(8);
        signUpOption.setAlignment(Pos.CENTER);
        signUpOption.getChildren().addAll(signUpBtn, signUpDesc);
        
        VBox guestOption = new VBox(8);
        guestOption.setAlignment(Pos.CENTER);
        guestOption.getChildren().addAll(guestBtn, guestDesc);
        
        // Add separator line
        Separator separator = new Separator();
    separator.setMaxWidth(200);
    separator.getStyleClass().add("welcome-separator");
        
        optionsContainer.getChildren().addAll(signInOption, signUpOption, separator, guestOption);
        
        // Privacy note
    Label privacyNote = new Label("🔒 Your privacy is protected. All data stays local on your device.");
    privacyNote.getStyleClass().add("privacy-note");
    privacyNote.setMaxWidth(400);
        
        card.getChildren().addAll(cardTitle, cardDescription, optionsContainer, privacyNote);
        
        return card;
    }
    
    private void showUserSetupDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Create Your Profile");
        dialog.setHeaderText("Welcome! Let's set up your LumosPath profile");
        
        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your preferred name");
        nameField.getStyleClass().add("form-field");
        
        TextField ageField = new TextField();
        ageField.setPromptText("Age (optional)");
        ageField.getStyleClass().add("form-field");
        
        TextField locationField = new TextField();
        locationField.setPromptText("City/Location (optional)");
        locationField.getStyleClass().add("form-field");
        
        Label nameLabel = new Label("Name:");
        Label ageLabel = new Label("Age:");
        Label locationLabel = new Label("Location:");
        
        nameLabel.getStyleClass().add("form-label");
        ageLabel.getStyleClass().add("form-label");
        locationLabel.getStyleClass().add("form-label");
        
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(ageLabel, 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(locationLabel, 0, 2);
        grid.add(locationField, 1, 2);
        
        Label note = new Label("All fields are optional. You can always update your profile later.");
        note.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");
        grid.add(note, 0, 3, 2, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        // Add buttons
        ButtonType createButton = new ButtonType("Create Profile", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, cancelButton);
        
        // Style dialog
        try {
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS loading failed, continue without styling
        }
        
        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButton) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    name = "User" + System.currentTimeMillis() % 1000;
                }
                
                int age = 0;
                try {
                    if (!ageField.getText().trim().isEmpty()) {
                        age = Integer.parseInt(ageField.getText().trim());
                    }
                } catch (NumberFormatException e) {
                    // Invalid age, use 0
                }
                
                String location = locationField.getText().trim();
                if (location.isEmpty()) {
                    location = "Not specified";
                }
                
                User user = new User(name, "", age, location);
                user.setUserId(1);
                return user;
            }
            return null;
        });
        
        // Show dialog and handle result
        dialog.showAndWait().ifPresent(user -> {
            controller.setUser(user);
            controller.showInfoAlert("Profile Created", 
                "Welcome, " + user.getUsername() + "! Your profile has been created successfully.");
            controller.showDashboardView();
        });
    }
    
    private void continueAnonymously() {
        User anonymousUser = new User();
        anonymousUser.setUserId(0);
        anonymousUser.setUsername("Anonymous User");
        anonymousUser.setAnonymous(true);
        
        controller.setUser(anonymousUser);
        controller.showInfoAlert("Anonymous Mode", 
            "Welcome! You're using LumosPath in anonymous mode.\nYour privacy is fully protected.");
        controller.showDashboardView();
    }
}