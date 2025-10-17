package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.service.MeditationService;
import com.lumospath.model.MeditationSession;
import com.lumospath.model.MeditationType;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.List;

/**
 * Beautiful meditation interface with guided sessions, breathing exercises, and timers
 */
public class MeditationView {
    private final MainController controller;
    private final MeditationService meditationService;
    
    // UI Components
    private VBox mainContainer;
    private ScrollPane sessionListPane;
    private VBox sessionListContainer;
    private VBox meditationPlayerContainer;
    
    // Meditation Session Components
    private Label sessionTitleLabel;
    private Label instructionLabel;
    private Label timerLabel;
    private Button playPauseButton;
    private Button stopButton;
    private ProgressBar progressBar;
    private Circle breathingCircle;
    
    // Session State
    private MeditationSession currentSession;
    private Timeline meditationTimer;
    private Timeline breathingAnimation;
    private int currentStepIndex = 0;
    private int totalElapsedSeconds = 0;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    
    public MeditationView(MainController controller) {
        this.controller = controller;
        this.meditationService = new MeditationService();
    }
    
    public ScrollPane createView() {
        mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setPadding(new Insets(20));
        
        // Header
        createHeader();
        
        // Quick Actions
        createQuickActions();
        
        // Session List
        createSessionList();
        
        // Meditation Player (initially hidden)
        createMeditationPlayer();
        
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private void createHeader() {
        Label title = new Label("🧘‍♀️ Meditation & Mindfulness");
        title.getStyleClass().add("header-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        
        Label subtitle = new Label("Find peace, reduce stress, and cultivate inner calm through guided meditation");
        subtitle.getStyleClass().add("subtitle");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web("#666666"));
        subtitle.setWrapText(true);
        
        VBox headerContainer = new VBox(5);
        headerContainer.getChildren().addAll(title, subtitle);
        
        mainContainer.getChildren().add(headerContainer);
    }
    
    private void createQuickActions() {
        Label quickTitle = new Label("✨ Quick Start");
        quickTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        
        HBox quickActions = new HBox(15);
        quickActions.setAlignment(Pos.CENTER);
        
        Button breathingBtn = new Button("🌬️ Quick Breathing\n(5 minutes)");
        breathingBtn.getStyleClass().add("meditation-card-button");
        breathingBtn.setPrefSize(150, 80);
        breathingBtn.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2; -fx-background-radius: 15; -fx-font-weight: bold;");
        breathingBtn.setTextAlignment(TextAlignment.CENTER);
        breathingBtn.setOnAction(e -> startBreathingExercise(5));
        
        Button mindfulnessBtn = new Button("🧘‍♀️ Mindfulness\n(10 minutes)");
        mindfulnessBtn.getStyleClass().add("meditation-card-button");
        mindfulnessBtn.setPrefSize(150, 80);
        mindfulnessBtn.setStyle("-fx-background-color: #f3e5f5; -fx-text-fill: #7b1fa2; -fx-background-radius: 15; -fx-font-weight: bold;");
        mindfulnessBtn.setTextAlignment(TextAlignment.CENTER);
        mindfulnessBtn.setOnAction(e -> startQuickMindfulness());
        
        Button anxietyBtn = new Button("🕊️ Anxiety Relief\n(7 minutes)");
        anxietyBtn.getStyleClass().add("meditation-card-button");
        anxietyBtn.setPrefSize(150, 80);
        anxietyBtn.setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d2e; -fx-background-radius: 15; -fx-font-weight: bold;");
        anxietyBtn.setTextAlignment(TextAlignment.CENTER);
        anxietyBtn.setOnAction(e -> startAnxietyRelief());
        
        Button customBtn = new Button("⚙️ Custom Session\n(Choose length)");
        customBtn.getStyleClass().add("meditation-card-button");
        customBtn.setPrefSize(150, 80);
        customBtn.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00; -fx-background-radius: 15; -fx-font-weight: bold;");
        customBtn.setTextAlignment(TextAlignment.CENTER);
        customBtn.setOnAction(e -> createCustomSession());
        
        quickActions.getChildren().addAll(breathingBtn, mindfulnessBtn, anxietyBtn, customBtn);
        
        VBox quickContainer = new VBox(10);
        quickContainer.getChildren().addAll(quickTitle, quickActions);
        
        mainContainer.getChildren().add(quickContainer);
    }
    
    private void createSessionList() {
        Label sessionsTitle = new Label("🎯 Guided Meditation Sessions");
        sessionsTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        
        sessionListContainer = new VBox(10);
        sessionListContainer.setPadding(new Insets(10));
        
        // Load all sessions
        List<MeditationSession> sessions = meditationService.getAllSessions();
        for (MeditationSession session : sessions) {
            createSessionCard(session);
        }
        
        sessionListPane = new ScrollPane(sessionListContainer);
        sessionListPane.setFitToWidth(true);
        sessionListPane.setPrefHeight(300);
        sessionListPane.getStyleClass().add("session-list-pane");
        
        VBox sessionsContainer = new VBox(10);
        sessionsContainer.getChildren().addAll(sessionsTitle, sessionListPane);
        
        mainContainer.getChildren().add(sessionsContainer);
    }
    
    private void createSessionCard(MeditationSession session) {
        HBox sessionCard = new HBox(15);
        sessionCard.setPadding(new Insets(15));
        sessionCard.getStyleClass().add("session-card");
        sessionCard.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-radius: 12; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Session Icon and Type
        Label typeLabel = new Label(session.getType().getEmoji());
        typeLabel.setFont(Font.font(24));
        
        // Session Details
        VBox detailsBox = new VBox(5);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(session.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Label descLabel = new Label(session.getDescription());
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        
        Label durationLabel = new Label("⏱️ " + session.getFormattedDuration() + " • " + session.getType().getDisplayName());
        durationLabel.setFont(Font.font("System", FontWeight.NORMAL, 11));
        durationLabel.setTextFill(Color.web("#888888"));
        
        detailsBox.getChildren().addAll(titleLabel, descLabel, durationLabel);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Start Button
        Button startButton = new Button("Start Session");
        startButton.getStyleClass().add("primary-button");
        startButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;");
        startButton.setOnAction(e -> startMeditationSession(session));
        
        sessionCard.getChildren().addAll(typeLabel, detailsBox, spacer, startButton);
        sessionListContainer.getChildren().add(sessionCard);
    }
    
    private void createMeditationPlayer() {
        meditationPlayerContainer = new VBox(20);
        meditationPlayerContainer.setPadding(new Insets(20));
        meditationPlayerContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #e8eaf6, #f3e5f5); " +
                                          "-fx-background-radius: 15; -fx-border-radius: 15;");
        meditationPlayerContainer.setVisible(false);
        meditationPlayerContainer.setManaged(false);
        
        // Session Title
        sessionTitleLabel = new Label();
        sessionTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        sessionTitleLabel.setAlignment(Pos.CENTER);
        sessionTitleLabel.setTextAlignment(TextAlignment.CENTER);
        
        // Breathing Circle (for breathing exercises)
        breathingCircle = new Circle(80);
        breathingCircle.setFill(Color.web("#81c784", 0.3));
        breathingCircle.setStroke(Color.web("#4caf50"));
        breathingCircle.setStrokeWidth(3);
        breathingCircle.setVisible(false);
        
        // Instruction Text
        instructionLabel = new Label();
        instructionLabel.setFont(Font.font("System", FontWeight.NORMAL, 18));
        instructionLabel.setTextAlignment(TextAlignment.CENTER);
        instructionLabel.setAlignment(Pos.CENTER);
        instructionLabel.setWrapText(true);
        instructionLabel.setMaxWidth(600);
        instructionLabel.setStyle("-fx-text-fill: #333333; -fx-padding: 20;");
        
        // Timer
        timerLabel = new Label("00:00");
        timerLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        timerLabel.setTextFill(Color.web("#4caf50"));
        timerLabel.setAlignment(Pos.CENTER);
        
        // Progress Bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setStyle("-fx-accent: #4caf50;");
        
        // Control Buttons
        HBox controlsBox = new HBox(15);
        controlsBox.setAlignment(Pos.CENTER);
        
        playPauseButton = new Button("▶️ Start");
        playPauseButton.getStyleClass().add("primary-button");
        playPauseButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-background-radius: 25; " +
                                "-fx-font-weight: bold; -fx-font-size: 16; -fx-padding: 12 24;");
        playPauseButton.setOnAction(e -> togglePlayPause());
        
        stopButton = new Button("⏹️ Stop");
        stopButton.getStyleClass().add("secondary-button");
        stopButton.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-background-radius: 25; " +
                           "-fx-font-weight: bold; -fx-font-size: 16; -fx-padding: 12 24;");
        stopButton.setOnAction(e -> stopMeditation());
        
        Button backButton = new Button("← Back to Sessions");
        backButton.getStyleClass().add("accent-button");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-border-color: #cccccc; " +
                           "-fx-border-radius: 25; -fx-background-radius: 25; -fx-padding: 8 16;");
        backButton.setOnAction(e -> showSessionList());
        
        controlsBox.getChildren().addAll(playPauseButton, stopButton, backButton);
        
        // Assemble player
        VBox playerContent = new VBox(15);
        playerContent.setAlignment(Pos.CENTER);
        playerContent.getChildren().addAll(
            sessionTitleLabel,
            breathingCircle,
            instructionLabel,
            timerLabel,
            progressBar,
            controlsBox
        );
        
        meditationPlayerContainer.getChildren().add(playerContent);
        mainContainer.getChildren().add(meditationPlayerContainer);
    }
    
    private void startMeditationSession(MeditationSession session) {
        this.currentSession = session;
        this.currentStepIndex = 0;
        this.totalElapsedSeconds = 0;
        this.isPlaying = false;
        this.isPaused = false;
        
        // Show player, hide session list
        sessionListPane.setVisible(false);
        sessionListPane.setManaged(false);
        meditationPlayerContainer.setVisible(true);
        meditationPlayerContainer.setManaged(true);
        
        // Setup player
        sessionTitleLabel.setText(session.getTitle());
        instructionLabel.setText(session.getPreparationText());
        timerLabel.setText("00:00");
        progressBar.setProgress(0);
        playPauseButton.setText("▶️ Start");
        
        // Show breathing circle for breathing exercises
        breathingCircle.setVisible(session.getType() == MeditationType.BREATHING || 
                                  session.getType() == MeditationType.ANXIETY_RELIEF);
    }
    
    private void startBreathingExercise(int minutes) {
        MeditationSession session = meditationService.createBreathingExercise(minutes);
        startMeditationSession(session);
    }
    
    private void startQuickMindfulness() {
        List<MeditationSession> mindfulnessSessions = meditationService.getSessionsByType(MeditationType.MINDFULNESS);
        if (!mindfulnessSessions.isEmpty()) {
            startMeditationSession(mindfulnessSessions.get(0));
        }
    }
    
    private void startAnxietyRelief() {
        List<MeditationSession> anxietySessions = meditationService.getSessionsByType(MeditationType.ANXIETY_RELIEF);
        if (!anxietySessions.isEmpty()) {
            startMeditationSession(anxietySessions.get(0));
        }
    }
    
    private void createCustomSession() {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Custom Meditation");
        dialog.setHeaderText("Create Custom Meditation Session");
        dialog.setContentText("Duration in minutes:");
        
        dialog.showAndWait().ifPresent(durationStr -> {
            try {
                int duration = Integer.parseInt(durationStr);
                if (duration > 0 && duration <= 120) {
                    startBreathingExercise(duration);
                } else {
                    showAlert("Please enter a duration between 1 and 120 minutes.");
                }
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid number for duration.");
            }
        });
    }
    
    private void togglePlayPause() {
        if (!isPlaying && !isPaused) {
            // Start meditation
            startMeditationTimer();
            playPauseButton.setText("⏸️ Pause");
            isPlaying = true;
        } else if (isPlaying && !isPaused) {
            // Pause meditation
            pauseMeditationTimer();
            playPauseButton.setText("▶️ Resume");
            isPaused = true;
            isPlaying = false;
        } else if (!isPlaying && isPaused) {
            // Resume meditation
            resumeMeditationTimer();
            playPauseButton.setText("⏸️ Pause");
            isPlaying = true;
            isPaused = false;
        }
    }
    
    private void startMeditationTimer() {
        if (currentSession == null || currentSession.getSteps() == null || currentSession.getSteps().isEmpty()) return;
        
        meditationTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            totalElapsedSeconds++;
            updateTimerDisplay();
            updateProgress();
        }));
        meditationTimer.setCycleCount(Animation.INDEFINITE);
        meditationTimer.play();
        
        // Start breathing animation if needed
        if (shouldShowBreathingAnimation()) {
            startBreathingAnimation();
        }
    }
    
    private void pauseMeditationTimer() {
        if (meditationTimer != null) {
            meditationTimer.pause();
        }
        if (breathingAnimation != null) {
            breathingAnimation.pause();
        }
    }
    
    private void resumeMeditationTimer() {
        if (meditationTimer != null) {
            meditationTimer.play();
        }
        if (breathingAnimation != null) {
            breathingAnimation.play();
        }
    }
    
    private void stopMeditation() {
        if (meditationTimer != null) {
            meditationTimer.stop();
        }
        if (breathingAnimation != null) {
            breathingAnimation.stop();
        }
        
        // Show completion message
        if (totalElapsedSeconds > 30) {
            String completionMsg = currentSession != null ? 
                currentSession.getClosingText() : 
                "Thank you for taking time to meditate. Carry this sense of peace with you.";
                
            showCompletionDialog(completionMsg);
            
            if (currentSession != null) {
                meditationService.completeSession(currentSession);
            }
        }
        
        showSessionList();
    }
    
    private void showSessionList() {
        meditationPlayerContainer.setVisible(false);
        meditationPlayerContainer.setManaged(false);
        sessionListPane.setVisible(true);
        sessionListPane.setManaged(true);
        
        resetMeditationState();
    }
    
    private void resetMeditationState() {
        currentSession = null;
        currentStepIndex = 0;
        totalElapsedSeconds = 0;
        isPlaying = false;
        isPaused = false;
        
        if (meditationTimer != null) {
            meditationTimer.stop();
        }
        if (breathingAnimation != null) {
            breathingAnimation.stop();
        }
    }
    
    private void updateTimerDisplay() {
        int minutes = totalElapsedSeconds / 60;
        int seconds = totalElapsedSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
    
    private void updateProgress() {
        if (currentSession == null) return;
        
        int totalDurationSeconds = currentSession.getDurationMinutes() * 60;
        double progress = Math.min((double) totalElapsedSeconds / totalDurationSeconds, 1.0);
        progressBar.setProgress(progress);
    }
    
    private boolean shouldShowBreathingAnimation() {
        return currentSession != null && 
               (currentSession.getType() == MeditationType.BREATHING || 
                currentSession.getType() == MeditationType.ANXIETY_RELIEF);
    }
    
    private void startBreathingAnimation() {
        if (breathingAnimation != null) {
            breathingAnimation.stop();
        }
        
        // Simple breathing animation - expand and contract circle
        breathingAnimation = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> breathingCircle.setRadius(60)),
            new KeyFrame(Duration.seconds(4), e -> breathingCircle.setRadius(100)), // Inhale
            new KeyFrame(Duration.seconds(8), e -> breathingCircle.setRadius(80)),  // Hold
            new KeyFrame(Duration.seconds(14), e -> breathingCircle.setRadius(60))  // Exhale
        );
        breathingAnimation.setCycleCount(Animation.INDEFINITE);
        breathingAnimation.play();
    }
    
    private void showCompletionDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Meditation Complete");
        alert.setHeaderText("🧘‍♀️ Session Finished");
        alert.setContentText(message);
        
        ButtonType newSessionBtn = new ButtonType("Start Another Session");
        ButtonType doneBtn = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        alert.getButtonTypes().setAll(newSessionBtn, doneBtn);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == newSessionBtn) {
                // User wants another session - stay on meditation view
            }
        });
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}