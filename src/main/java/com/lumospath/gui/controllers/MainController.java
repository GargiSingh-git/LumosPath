package com.lumospath.gui.controllers;

import com.lumospath.service.MoodTrackingService;
import com.lumospath.service.MotivationalQuoteService;
import com.lumospath.service.EmergencyHelplineService;
import com.lumospath.service.AuthenticationService;
import com.lumospath.service.ChatHistoryService;
import com.lumospath.chatbot.LumosBot;
import com.lumospath.model.User;
import com.lumospath.gui.views.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

/**
 * Main controller class for managing the JavaFX GUI
 */
public class MainController {
    private final MoodTrackingService moodService;
    private final MotivationalQuoteService quoteService;
    private final EmergencyHelplineService helplineService;
    private final AuthenticationService authService;
    private final ChatHistoryService chatHistoryService;
    private final LumosBot chatbot;
    
    private User currentUser;
    private BorderPane mainLayout;
    private Scene scene;
    
    // Views
    private WelcomeView welcomeView;
    private LoginView loginView;
    private SignUpView signUpView;
    private DashboardView dashboardView;
    private MoodTrackingView moodView;
    private QuotesView quotesView;
    private ChatView chatView;
    private HelplineView helplineView;
    private MeditationView meditationView;
    private UserProgressView progressView;
    
    public MainController(MoodTrackingService moodService, MotivationalQuoteService quoteService,
                         EmergencyHelplineService helplineService, LumosBot chatbot) {
        this.moodService = moodService;
        this.quoteService = quoteService;
        this.helplineService = helplineService;
        this.authService = new AuthenticationService();
        this.chatHistoryService = new ChatHistoryService();
        this.chatbot = chatbot;
        
        // Initialize views
        this.welcomeView = new WelcomeView(this);
        this.loginView = new LoginView(this, authService);
        this.signUpView = new SignUpView(this, authService);
        this.dashboardView = new DashboardView(this);
        this.moodView = new MoodTrackingView(this, moodService);
        this.quotesView = new QuotesView(this, quoteService);
        this.chatView = new ChatView(this, chatbot);
        this.helplineView = new HelplineView(this, helplineService);
        this.meditationView = new MeditationView(this);
        this.progressView = new UserProgressView(this, moodService, chatHistoryService);
    }
    
    public Scene createMainScene() {
        mainLayout = new BorderPane();
        scene = new Scene(mainLayout, 1200, 800);
        
        // Load CSS styling
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
        
        // Start with welcome view
        showWelcomeView();
        
        return scene;
    }
    
    public void showWelcomeView() {
        mainLayout.setCenter(welcomeView.createView());
        mainLayout.setTop(null);
        mainLayout.setLeft(null);
    }
    
    public void showLoginView() {
        mainLayout.setCenter(loginView.createView());
        mainLayout.setTop(null);
        mainLayout.setLeft(null);
    }
    
    public void showSignUpView() {
        mainLayout.setCenter(signUpView.createView());
        mainLayout.setTop(null);
        mainLayout.setLeft(null);
    }
    
    public void showDashboardView() {
        createNavigation();
        mainLayout.setCenter(dashboardView.createView());
    }
    
    public void showMoodTrackingView() {
        createNavigation();
        mainLayout.setCenter(moodView.createView());
    }
    
    public void showQuotesView() {
        createNavigation();
        mainLayout.setCenter(quotesView.createView());
    }
    
    public void showChatView() {
        createNavigation();
        mainLayout.setCenter(chatView.createView());
    }
    
    public void showHelplineView() {
        createNavigation();
        mainLayout.setCenter(helplineView.createView());
    }
    
    public void showMeditationView() {
        createNavigation();
        mainLayout.setCenter(meditationView.createView());
    }
    
    public void showProgressView() {
        createNavigation();
        mainLayout.setCenter(progressView.createView());
    }
    
    private void createNavigation() {
        // Create top navigation bar
        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.getStyleClass().add("header");
        
        Label title = new Label("LumosPath");
        title.getStyleClass().add("header-title");
        
        // User info section
        HBox userInfo = new HBox(10);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        
        userInfo.getStyleClass().add("user-info");
        
        if (currentUser != null) {
            Label welcomeLabel = new Label("Welcome, " + currentUser.getFullName() + "!");
            welcomeLabel.getStyleClass().add("user-welcome-text");
            
            Button logoutBtn = new Button("Logout");
            logoutBtn.getStyleClass().add("secondary-button");
            logoutBtn.setStyle("-fx-font-size: 11px; -fx-padding: 6px 12px;");
            logoutBtn.setOnAction(e -> {
                authService.logout();
                currentUser = null;
                showWelcomeView();
            });
            
            userInfo.getChildren().addAll(welcomeLabel, logoutBtn);
        } else {
            Label guestLabel = new Label("Guest Mode");
            guestLabel.getStyleClass().add("user-welcome-text");
            guestLabel.setStyle("-fx-text-fill: #718096;");
            
            Button loginBtn = new Button("Sign In");
            loginBtn.getStyleClass().add("primary-button");
            loginBtn.setStyle("-fx-font-size: 11px; -fx-padding: 6px 12px;");
            loginBtn.setOnAction(e -> showLoginView());
            
            userInfo.getChildren().addAll(guestLabel, loginBtn);
        }
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Navigation buttons
        Button dashboardBtn = new Button("🏠 Dashboard");
        Button moodBtn = new Button("📈 Mood");
        Button chatBtn = new Button("🤖 Chat");
        Button progressBtn = new Button("📊 Progress");
        Button meditationBtn = new Button("🧘‍♀️ Meditate");
        Button helplineBtn = new Button("📞 Help");
        
        // Style navigation buttons
        dashboardBtn.getStyleClass().add("nav-button");
        moodBtn.getStyleClass().add("nav-button");
        chatBtn.getStyleClass().add("nav-button");
        progressBtn.getStyleClass().add("nav-button");
        meditationBtn.getStyleClass().add("nav-button");
        helplineBtn.getStyleClass().add("nav-button");
        
        // Set button actions
        dashboardBtn.setOnAction(e -> showDashboardView());
        moodBtn.setOnAction(e -> showMoodTrackingView());
        chatBtn.setOnAction(e -> showChatView());
        progressBtn.setOnAction(e -> showProgressView());
        meditationBtn.setOnAction(e -> showMeditationView());
        helplineBtn.setOnAction(e -> showHelplineView());
        
        HBox navButtons = new HBox(10);
        navButtons.getChildren().addAll(dashboardBtn, moodBtn, chatBtn, progressBtn, meditationBtn, helplineBtn);
        
        navbar.getChildren().addAll(title, spacer, navButtons, userInfo);
        mainLayout.setTop(navbar);
    }
    
    // User management
    public void setUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public MoodTrackingService getMoodService() {
        return moodService;
    }
    
    public ChatHistoryService getChatHistoryService() {
        return chatHistoryService;
    }
    
    // Show alert dialogs
    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS loading failed, continue without styling
        }
        
        alert.showAndWait();
    }
    
    public void showInfoAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.INFORMATION);
    }
    
    public void showWarningAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.WARNING);
    }
    
    public void showErrorAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }
    
    // Confirmation dialog
    public boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS loading failed, continue without styling
        }
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    // Exit application
    public void exitApplication() {
        if (showConfirmation("Exit LumosPath", "Are you sure you want to exit?\n\nRemember: You are stronger than you think! 💙")) {
            System.exit(0);
        }
    }
}