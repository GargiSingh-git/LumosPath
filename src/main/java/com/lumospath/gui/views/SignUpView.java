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
 * Sign up view for user registration
 */
public class SignUpView {
    private final MainController controller;
    private final AuthenticationService authService;
    
    public SignUpView(MainController controller, AuthenticationService authService) {
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
        
        // Registration form
        VBox signUpForm = createSignUpForm();
        
        // Navigation links
        HBox navigation = createNavigationLinks();
        
        // Center the container
        HBox centerWrapper = new HBox();
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.getChildren().add(container);
        
        container.getChildren().addAll(header, signUpForm, navigation);
        
        ScrollPane scrollPane = new ScrollPane(centerWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Join LumosPath! ✨");
        titleLabel.getStyleClass().add("welcome-title");
        titleLabel.setStyle("-fx-font-size: 36px;");
        
        Label subtitleLabel = new Label("Start your journey to mental wellness and emotional support");
        subtitleLabel.getStyleClass().add("welcome-subtitle");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #4a5568;");
        
        header.getChildren().addAll(titleLabel, subtitleLabel);
        
        return header;
    }
    
    private VBox createSignUpForm() {
        VBox form = new VBox(24);
        form.getStyleClass().add("form-container");
        
        // Name fields (side by side)
        HBox nameRow = new HBox(15);
        
        VBox firstNameGroup = new VBox(8);
        Label firstNameLabel = new Label("First Name *");
        firstNameLabel.getStyleClass().add("form-label");
        
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");
        firstNameField.getStyleClass().add("form-field");
        
        firstNameGroup.getChildren().addAll(firstNameLabel, firstNameField);
        
        VBox lastNameGroup = new VBox(8);
        Label lastNameLabel = new Label("Last Name *");
        lastNameLabel.getStyleClass().add("form-label");
        
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");
        lastNameField.getStyleClass().add("form-field");
        
        lastNameGroup.getChildren().addAll(lastNameLabel, lastNameField);
        
        nameRow.getChildren().addAll(firstNameGroup, lastNameGroup);
        HBox.setHgrow(firstNameGroup, Priority.ALWAYS);
        HBox.setHgrow(lastNameGroup, Priority.ALWAYS);
        
        // Username field
        VBox usernameGroup = new VBox(8);
        Label usernameLabel = new Label("Username *");
        usernameLabel.getStyleClass().add("form-label");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a unique username");
        usernameField.setPrefHeight(40);
        usernameField.getStyleClass().add("form-field");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; " +
                              "-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
        
        Label usernameHint = new Label("3-50 characters, letters, numbers, hyphens and underscores only");
        usernameHint.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");
        
        usernameGroup.getChildren().addAll(usernameLabel, usernameField, usernameHint);
        
        // Email field
        VBox emailGroup = new VBox(8);
        Label emailLabel = new Label("Email Address *");
        emailLabel.getStyleClass().add("form-label");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefHeight(40);
        emailField.getStyleClass().add("form-field");
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; " +
                           "-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
        
        emailGroup.getChildren().addAll(emailLabel, emailField);
        
        // Password field
        VBox passwordGroup = new VBox(8);
        Label passwordLabel = new Label("Password *");
        passwordLabel.getStyleClass().add("form-label");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a strong password");
        passwordField.setPrefHeight(40);
        passwordField.getStyleClass().add("form-field");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; " +
                              "-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
        
        Label passwordHint = new Label("At least 8 characters with uppercase, lowercase, number, and special character");
        passwordHint.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");
        passwordHint.setWrapText(true);
        
        passwordGroup.getChildren().addAll(passwordLabel, passwordField, passwordHint);
        
        // Confirm password field
        VBox confirmPasswordGroup = new VBox(8);
        Label confirmPasswordLabel = new Label("Confirm Password *");
        confirmPasswordLabel.getStyleClass().add("form-label");
        confirmPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setPrefHeight(40);
        confirmPasswordField.getStyleClass().add("form-field");
        confirmPasswordField.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-border-radius: 5; " +
                                     "-fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
        
        confirmPasswordGroup.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);
        
        // Terms checkbox
        CheckBox termsCheckBox = new CheckBox();
        Label termsLabel = new Label("I agree to the Terms of Service and Privacy Policy");
        termsLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");
        
        HBox termsRow = new HBox(8);
        termsRow.setAlignment(Pos.CENTER_LEFT);
        termsRow.getChildren().addAll(termsCheckBox, termsLabel);
        
        // Sign up button
        Button signUpButton = new Button("Create Account");
        signUpButton.getStyleClass().add("success-button");
        signUpButton.setPrefHeight(48);
        signUpButton.setMaxWidth(Double.MAX_VALUE);
        
        // Error/Success message label
        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        messageLabel.setWrapText(true);
        
        // Sign up button action
        signUpButton.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            // Validation
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || 
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError(messageLabel, "Please fill in all required fields");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showError(messageLabel, "Passwords do not match");
                return;
            }
            
            if (!termsCheckBox.isSelected()) {
                showError(messageLabel, "Please agree to the Terms of Service and Privacy Policy");
                return;
            }
            
            // Disable button during registration
            signUpButton.setDisable(true);
            signUpButton.setText("Creating Account...");
            
            // Perform registration
            AuthenticationResult result = authService.register(username, email, password, firstName, lastName);
            
            if (result.isSuccess()) {
                // Registration successful
                showSuccess(messageLabel, "Account created successfully! You can now sign in.");
                
                // Clear form
                firstNameField.clear();
                lastNameField.clear();
                usernameField.clear();
                emailField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
                termsCheckBox.setSelected(false);
                
                // Navigate to login after a short delay
                javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(2000);
                        return null;
                    }
                };
                
                task.setOnSucceeded(evt -> controller.showLoginView());
                new Thread(task).start();
            } else {
                // Registration failed - show error
                showError(messageLabel, result.getMessage());
            }
            
            signUpButton.setDisable(false);
            signUpButton.setText("Create Account");
        });
        
        form.getChildren().addAll(
            nameRow,
            usernameGroup,
            emailGroup,
            passwordGroup,
            confirmPasswordGroup,
            termsRow,
            messageLabel,
            signUpButton
        );
        
        return form;
    }
    
    private HBox createNavigationLinks() {
        HBox navigation = new HBox(15);
        navigation.setAlignment(Pos.CENTER);
        
        Label existingUserLabel = new Label("Already have an account?");
        existingUserLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        Hyperlink signInLink = new Hyperlink("Sign In");
        signInLink.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        signInLink.setOnAction(e -> controller.showLoginView());
        
        Hyperlink guestLink = new Hyperlink("Continue as Guest");
        guestLink.setStyle("-fx-text-fill: #95a5a6;");
        guestLink.setOnAction(e -> {
            // Create anonymous user and continue
            controller.setUser(null); // Set to null for guest mode
            controller.showDashboardView();
        });
        
        navigation.getChildren().addAll(existingUserLabel, signInLink, new Label("|"), guestLink);
        
        return navigation;
    }
    
    private void showError(Label messageLabel, String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("success-message");
        messageLabel.getStyleClass().add("error-message");
        messageLabel.setVisible(true);
        
        // Hide error after 5 seconds
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(5000);
                return null;
            }
        };
        
        task.setOnSucceeded(e -> messageLabel.setVisible(false));
        new Thread(task).start();
    }
    
    private void showSuccess(Label messageLabel, String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("error-message");
        messageLabel.getStyleClass().add("success-message");
        messageLabel.setVisible(true);
    }
}