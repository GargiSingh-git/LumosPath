package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.service.AuthenticationService;
import com.lumospath.service.AuthenticationService.AuthenticationResult;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

/**
 * Login view for user authentication
 */
public class LoginView {
    private final MainController controller;
    private final AuthenticationService authService;
    
    public LoginView(MainController controller, AuthenticationService authService) {
        this.controller = controller;
        this.authService = authService;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(30);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(40));
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        
        // Welcome header
        VBox header = createHeader();
        
        // Login form
        VBox loginForm = createLoginForm();
        
        // Navigation links
        HBox navigation = createNavigationLinks();
        
        // Center the container
        HBox centerWrapper = new HBox();
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.getChildren().add(container);
        
        container.getChildren().addAll(header, loginForm, navigation);
        
        ScrollPane scrollPane = new ScrollPane(centerWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Welcome Back! 🌟");
        titleLabel.getStyleClass().add("welcome-title");
        titleLabel.setStyle("-fx-font-size: 36px;");
        
        Label subtitleLabel = new Label("Sign in to continue your mental wellness journey");
        subtitleLabel.getStyleClass().add("welcome-subtitle");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #4a5568;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        return header;
    }
    
    private VBox createLoginForm() {
        VBox form = new VBox(24);
        form.getStyleClass().add("form-container");
        
        // Username/Email field
        VBox usernameGroup = new VBox(8);
        Label usernameLabel = new Label("Username or Email");
        usernameLabel.getStyleClass().add("form-label");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username or email");
        usernameField.getStyleClass().add("form-field");
        
        usernameGroup.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        VBox passwordGroup = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("form-field");
        
        passwordGroup.getChildren().addAll(passwordLabel, passwordField);
        
        // Remember me checkbox
        CheckBox rememberMe = new CheckBox("Remember me");
        rememberMe.setStyle("-fx-text-fill: #2c3e50;");
        
        // Login button
        Button loginButton = new Button("Sign In");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setPrefHeight(48);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        
        // Error message label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-message");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        
        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                showError(errorLabel, "Please fill in all fields");
                return;
            }
            
            // Disable button during login
            loginButton.setDisable(true);
            loginButton.setText("Signing In...");
            
            // Perform authentication
            AuthenticationResult result = authService.login(username, password);
            
            if (result.isSuccess()) {
                // Login successful - update controller and navigate to dashboard
                controller.setUser(result.getUser());
                controller.showDashboardView();
            } else {
                // Login failed - show error
                showError(errorLabel, result.getMessage());
                loginButton.setDisable(false);
                loginButton.setText("Sign In");
            }
        });
        
        // Enter key support
        passwordField.setOnAction(e -> loginButton.fire());
        usernameField.setOnAction(e -> passwordField.requestFocus());
        
        form.getChildren().addAll(
            usernameGroup, 
            passwordGroup, 
            rememberMe, 
            errorLabel,
            loginButton
        );
        
        return form;
    }
    
    private HBox createNavigationLinks() {
        HBox navigation = new HBox(15);
        navigation.setAlignment(Pos.CENTER);
        
        Label newUserLabel = new Label("New to LumosPath?");
        newUserLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        Hyperlink signUpLink = new Hyperlink("Create Account");
        signUpLink.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        signUpLink.setOnAction(e -> controller.showSignUpView());
        
        Hyperlink guestLink = new Hyperlink("Continue as Guest");
        guestLink.setStyle("-fx-text-fill: #95a5a6;");
        guestLink.setOnAction(e -> {
            // Create anonymous user and continue
            controller.setUser(null); // Set to null for guest mode
            controller.showDashboardView();
        });
        
        navigation.getChildren().addAll(newUserLabel, signUpLink, new Label("|"), guestLink);
        
        return navigation;
    }
    
    private void showError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        
        // Hide error after 5 seconds
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(5000);
                return null;
            }
        };
        
        task.setOnSucceeded(e -> errorLabel.setVisible(false));
        new Thread(task).start();
    }
}