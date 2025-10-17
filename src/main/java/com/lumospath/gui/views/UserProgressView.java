package com.lumospath.gui.views;

import com.lumospath.gui.controllers.MainController;
import com.lumospath.model.ChatLog;
import com.lumospath.model.MoodEntry;
import com.lumospath.service.ChatHistoryService;
import com.lumospath.service.MoodTrackingService;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * View for displaying user progress including mood history and chat history
 */
public class UserProgressView {
    private final MainController controller;
    private final MoodTrackingService moodService;
    private final ChatHistoryService chatService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    public UserProgressView(MainController controller, MoodTrackingService moodService, ChatHistoryService chatService) {
        this.controller = controller;
        this.moodService = moodService;
        this.chatService = chatService;
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(25);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(30));
        
        // Header
        VBox header = createHeader();
        
        // Progress summary cards
        HBox summaryCards = createSummaryCards();
        
        // Progress content with tabs
        TabPane tabPane = createProgressTabs();
        
        // History management controls
        VBox controls = createHistoryControls();
        
        container.getChildren().addAll(header, summaryCards, tabPane, controls);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        
        Label title = new Label("📊 Your Progress Journey");
        title.getStyleClass().add("header-title");
        
        Label subtitle = new Label("Track your emotional wellness and chat insights");
        subtitle.getStyleClass().add("header-subtitle");
        
        header.getChildren().addAll(title, subtitle);
        
        return header;
    }
    
    private HBox createSummaryCards() {
        HBox cardsContainer = new HBox(20);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setPadding(new Insets(0, 0, 20, 0));
        
        // Get current user
        int userId = controller.getCurrentUser() != null ? controller.getCurrentUser().getUserId() : 0;
        
        if (userId <= 0) {
            // Anonymous user message
            VBox anonymousCard = createCard("🔒 Anonymous Mode", 
                "Sign up to track your progress and view your history!");
            anonymousCard.getStyleClass().add("warning-card");
            cardsContainer.getChildren().add(anonymousCard);
            return cardsContainer;
        }
        
        // Mood summary card
        VBox moodCard = createMoodSummaryCard(userId);
        
        // Chat summary card
        VBox chatCard = createChatSummaryCard(userId);
        
        // Overall progress card
        VBox overallCard = createOverallProgressCard(userId);
        
        cardsContainer.getChildren().addAll(moodCard, chatCard, overallCard);
        
        return cardsContainer;
    }
    
    private VBox createMoodSummaryCard(int userId) {
        long totalMoodEntries = moodService.getMoodEntryCount(userId);
        double avgMood = moodService.getAverageMoodScore(userId);
        
        VBox card = new VBox(10);
        card.getStyleClass().add("progress-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        
        Label icon = new Label("😊");
        icon.setStyle("-fx-font-size: 36px;");
        
        Label title = new Label("Mood Tracking");
        title.getStyleClass().add("card-title");
        
        Label entriesLabel = new Label(totalMoodEntries + " entries recorded");
        entriesLabel.getStyleClass().add("card-text");
        
        Label avgLabel = new Label(String.format("Avg mood: %.1f/10", avgMood));
        avgLabel.getStyleClass().add("card-subtitle");
        
        String moodTrend = avgMood >= 7 ? "Great progress! 🌟" : 
                          avgMood >= 5 ? "Keep it up! 💪" : "Stay strong! 💜";
        Label trendLabel = new Label(moodTrend);
        trendLabel.getStyleClass().add("card-text");
        trendLabel.setStyle("-fx-text-fill: #4A90E2; -fx-font-style: italic;");
        
        card.getChildren().addAll(icon, title, entriesLabel, avgLabel, trendLabel);
        
        return card;
    }
    
    private VBox createChatSummaryCard(int userId) {
        ChatHistoryService.ChatSummary chatSummary = chatService.getChatSummary(userId, 30);
        
        VBox card = new VBox(10);
        card.getStyleClass().add("progress-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        
        Label icon = new Label("💬");
        icon.setStyle("-fx-font-size: 36px;");
        
        Label title = new Label("Chat History");
        title.getStyleClass().add("card-title");
        
        Label chatsLabel = new Label(chatSummary.getTotalChats() + " conversations");
        chatsLabel.getStyleClass().add("card-text");
        
        Label sentimentLabel = new Label("Sentiment: " + chatSummary.getSentimentDescription());
        sentimentLabel.getStyleClass().add("card-subtitle");
        
        Label emotionLabel = new Label("Dominant: " + chatSummary.getDominantEmotion());
        emotionLabel.getStyleClass().add("card-text");
        emotionLabel.setStyle("-fx-text-fill: #6B46C1; -fx-font-style: italic;");
        
        card.getChildren().addAll(icon, title, chatsLabel, sentimentLabel, emotionLabel);
        
        return card;
    }
    
    private VBox createOverallProgressCard(int userId) {
        long totalMoods = moodService.getMoodEntryCount(userId);
        long totalChats = chatService.getChatCount(userId);
        long totalInteractions = totalMoods + totalChats;
        
        VBox card = new VBox(10);
        card.getStyleClass().add("progress-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        
        Label icon = new Label("📈");
        icon.setStyle("-fx-font-size: 36px;");
        
        Label title = new Label("Overall Progress");
        title.getStyleClass().add("card-title");
        
        Label interactionsLabel = new Label(totalInteractions + " total interactions");
        interactionsLabel.getStyleClass().add("card-text");
        
        // Determine progress level
        String progressLevel = totalInteractions >= 50 ? "Advanced Explorer 🏆" :
                              totalInteractions >= 20 ? "Regular User 🌟" :
                              totalInteractions >= 5 ? "Getting Started 🌱" : "Beginner 👋";
        
        Label levelLabel = new Label(progressLevel);
        levelLabel.getStyleClass().add("card-subtitle");
        
        Label encouragement = new Label("Keep growing! 💫");
        encouragement.getStyleClass().add("card-text");
        encouragement.setStyle("-fx-text-fill: #10B981; -fx-font-style: italic;");
        
        card.getChildren().addAll(icon, title, interactionsLabel, levelLabel, encouragement);
        
        return card;
    }
    
    private VBox createCard(String title, String content) {
        VBox card = new VBox(10);
        card.getStyleClass().add("progress-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(300);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        
        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().add("card-text");
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(280);
        
        card.getChildren().addAll(titleLabel, contentLabel);
        
        return card;
    }
    
    private TabPane createProgressTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setPrefHeight(500);
        
        int userId = controller.getCurrentUser() != null ? controller.getCurrentUser().getUserId() : 0;
        
        if (userId <= 0) {
            // Anonymous user - show sign up prompt
            Tab signUpTab = new Tab("📝 Sign Up to View History");
            signUpTab.setClosable(false);
            
            VBox signUpContent = new VBox(30);
            signUpContent.setAlignment(Pos.CENTER);
            signUpContent.setPadding(new Insets(50));
            
            Label icon = new Label("🔐");
            icon.setStyle("-fx-font-size: 48px;");
            
            Label title = new Label("Your Journey Awaits!");
            title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            Label description = new Label("Create an account to:\n• Track your mood over time\n• View your chat history\n• See personalized insights\n• Monitor your progress");
            description.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
            description.setWrapText(true);
            
            Button signUpBtn = new Button("🚀 Sign Up Now");
            signUpBtn.getStyleClass().add("primary-button");
            signUpBtn.setStyle("-fx-font-size: 16px; -fx-padding: 15px 30px;");
            signUpBtn.setOnAction(e -> controller.showSignUpView());
            
            signUpContent.getChildren().addAll(icon, title, description, signUpBtn);
            signUpTab.setContent(signUpContent);
            
            tabPane.getTabs().add(signUpTab);
            return tabPane;
        }
        
        // Mood History Tab
        Tab moodTab = new Tab("😊 Mood History");
        moodTab.setClosable(false);
        moodTab.setContent(createMoodHistoryContent(userId));
        
        // Chat History Tab
        Tab chatTab = new Tab("💬 Chat History");
        chatTab.setClosable(false);
        chatTab.setContent(createChatHistoryContent(userId));
        
        // Statistics Tab
        Tab statsTab = new Tab("📊 Statistics");
        statsTab.setClosable(false);
        statsTab.setContent(createStatisticsContent(userId));
        
        tabPane.getTabs().addAll(moodTab, chatTab, statsTab);
        
        return tabPane;
    }
    
    private ScrollPane createMoodHistoryContent(int userId) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        List<MoodEntry> recentMoods = moodService.getUserMoodEntries(userId);
        
        if (recentMoods.isEmpty()) {
            VBox emptyState = new VBox(20);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(50));
            
            Label icon = new Label("📈");
            icon.setStyle("-fx-font-size: 48px;");
            
            Label title = new Label("No Mood Entries Yet");
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            Label subtitle = new Label("Start tracking your mood to see your emotional journey!");
            subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
            
            Button trackMoodBtn = new Button("📊 Track Mood Now");
            trackMoodBtn.getStyleClass().add("primary-button");
            trackMoodBtn.setOnAction(e -> controller.showMoodTrackingView());
            
            emptyState.getChildren().addAll(icon, title, subtitle, trackMoodBtn);
            return new ScrollPane(emptyState);
        }
        
        // Show recent mood entries
        Label headerLabel = new Label("Recent Mood Entries");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        VBox entriesList = new VBox(15);
        
        for (MoodEntry mood : recentMoods.stream().limit(10).collect(Collectors.toList())) {
            HBox entryBox = new HBox(15);
            entryBox.getStyleClass().add("history-item");
            entryBox.setPadding(new Insets(15));
            entryBox.setAlignment(Pos.CENTER_LEFT);
            
            // Mood emoji and type
            VBox moodInfo = new VBox(5);
            Label moodEmoji = new Label(mood.getMoodType().getEmoji());
            moodEmoji.setStyle("-fx-font-size: 24px;");
            Label moodType = new Label(mood.getMoodType().getDisplayName());
            moodType.getStyleClass().add("mood-type-label");
            moodInfo.getChildren().addAll(moodEmoji, moodType);
            
            // Mood details
            VBox details = new VBox(5);
            details.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(details, Priority.ALWAYS);
            
            Label intensityLabel = new Label("Intensity: " + mood.getMoodScale() + "/10");
            intensityLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4A90E2;");
            
            Label descLabel = new Label(mood.getDescription());
            descLabel.setStyle("-fx-text-fill: #374151;");
            descLabel.setWrapText(true);
            
            if (mood.getTrigger() != null && !mood.getTrigger().trim().isEmpty()) {
                Label triggerLabel = new Label("Trigger: " + mood.getTrigger());
                triggerLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6B7280;");
                details.getChildren().addAll(intensityLabel, descLabel, triggerLabel);
            } else {
                details.getChildren().addAll(intensityLabel, descLabel);
            }
            
            // Timestamp
            VBox timestamp = new VBox();
            timestamp.setAlignment(Pos.CENTER_RIGHT);
            Label timeLabel = new Label(mood.getCreatedAt().format(dateFormatter));
            timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF;");
            timestamp.getChildren().add(timeLabel);
            
            entryBox.getChildren().addAll(moodInfo, details, timestamp);
            entriesList.getChildren().add(entryBox);
        }
        
        content.getChildren().addAll(headerLabel, entriesList);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private ScrollPane createChatHistoryContent(int userId) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        List<ChatLog> recentChats = chatService.getRecentChatHistory(userId, 20);
        
        if (recentChats.isEmpty()) {
            VBox emptyState = new VBox(20);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(50));
            
            Label icon = new Label("💬");
            icon.setStyle("-fx-font-size: 48px;");
            
            Label title = new Label("No Chat History Yet");
            title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            Label subtitle = new Label("Start chatting with LumosBot to see your conversation history!");
            subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
            
            Button chatBtn = new Button("💬 Start Chatting");
            chatBtn.getStyleClass().add("primary-button");
            chatBtn.setOnAction(e -> controller.showChatView());
            
            emptyState.getChildren().addAll(icon, title, subtitle, chatBtn);
            return new ScrollPane(emptyState);
        }
        
        // Show recent chat logs
        Label headerLabel = new Label("Recent Conversations");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        VBox chatsList = new VBox(15);
        
        for (ChatLog chat : recentChats) {
            VBox chatBox = new VBox(10);
            chatBox.getStyleClass().add("chat-history-item");
            chatBox.setPadding(new Insets(15));
            
            // Timestamp and emotion
            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_LEFT);
            
            Label timeLabel = new Label(chat.getFormattedTimestamp());
            timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF;");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            if (chat.getDetectedEmotion() != null && !chat.getDetectedEmotion().isEmpty() && 
                !chat.getDetectedEmotion().equals("neutral")) {
                Label emotionLabel = new Label("Emotion: " + chat.getDetectedEmotion());
                emotionLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B46C1; -fx-font-style: italic;");
                header.getChildren().addAll(timeLabel, spacer, emotionLabel);
            } else {
                header.getChildren().addAll(timeLabel, spacer);
            }
            
            // User message
            if (chat.getUserMessage() != null && !chat.getUserMessage().trim().isEmpty()) {
                Label userLabel = new Label("You: " + chat.getUserMessage());
                userLabel.setStyle("-fx-text-fill: #1976D2; -fx-font-weight: bold;");
                userLabel.setWrapText(true);
                chatBox.getChildren().addAll(header, userLabel);
            } else {
                chatBox.getChildren().add(header);
            }
            
            // Bot response (truncated)
            if (chat.getBotResponse() != null && !chat.getBotResponse().trim().isEmpty()) {
                String response = chat.getBotResponse();
                if (response.length() > 150) {
                    response = response.substring(0, 150) + "...";
                }
                Label botLabel = new Label("LumosBot: " + response);
                botLabel.setStyle("-fx-text-fill: #7B1FA2;");
                botLabel.setWrapText(true);
                chatBox.getChildren().add(botLabel);
            }
            
            chatsList.getChildren().add(chatBox);
        }
        
        content.getChildren().addAll(headerLabel, chatsList);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private ScrollPane createStatisticsContent(int userId) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(20));
        
        // Mood statistics
        VBox moodStatsSection = createMoodStatisticsSection(userId);
        
        // Chat statistics
        VBox chatStatsSection = createChatStatisticsSection(userId);
        
        content.getChildren().addAll(moodStatsSection, chatStatsSection);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private VBox createMoodStatisticsSection(int userId) {
        VBox section = new VBox(15);
        
        Label title = new Label("📊 Mood Statistics (Last 30 Days)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        List<Object[]> moodStats = moodService.getMoodStatistics(userId, 30);
        
        if (moodStats.isEmpty()) {
            Label noDataLabel = new Label("No mood data available for the last 30 days.");
            noDataLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-style: italic;");
            section.getChildren().addAll(title, noDataLabel);
            return section;
        }
        
        // Create pie chart for mood distribution
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Object[] stat : moodStats) {
            String moodType = (String) stat[0];
            Integer count = (Integer) stat[1];
            pieChartData.add(new PieChart.Data(moodType, count));
        }
        
        PieChart moodChart = new PieChart(pieChartData);
        moodChart.setTitle("Mood Distribution");
        moodChart.setPrefSize(350, 250);
        moodChart.setLegendVisible(true);
        
        section.getChildren().addAll(title, moodChart);
        
        return section;
    }
    
    private VBox createChatStatisticsSection(int userId) {
        VBox section = new VBox(15);
        
        Label title = new Label("💬 Chat Statistics (Last 30 Days)");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        ChatHistoryService.ChatSummary summary = chatService.getChatSummary(userId, 30);
        
        if (summary.getTotalChats() == 0) {
            Label noDataLabel = new Label("No chat data available for the last 30 days.");
            noDataLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-style: italic;");
            section.getChildren().addAll(title, noDataLabel);
            return section;
        }
        
        // Chat summary info
        HBox summaryBox = new HBox(30);
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox totalChatsBox = new VBox(5);
        totalChatsBox.setAlignment(Pos.CENTER);
        Label totalChatsLabel = new Label(String.valueOf(summary.getTotalChats()));
        totalChatsLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4A90E2;");
        Label totalChatsDesc = new Label("Total Conversations");
        totalChatsDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280;");
        totalChatsBox.getChildren().addAll(totalChatsLabel, totalChatsDesc);
        
        VBox sentimentBox = new VBox(5);
        sentimentBox.setAlignment(Pos.CENTER);
        Label sentimentLabel = new Label(summary.getSentimentDescription());
        sentimentLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10B981;");
        Label sentimentDesc = new Label(String.format("Avg: %.2f", summary.getAverageSentiment()));
        sentimentDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280;");
        sentimentBox.getChildren().addAll(sentimentLabel, sentimentDesc);
        
        VBox emotionBox = new VBox(5);
        emotionBox.setAlignment(Pos.CENTER);
        Label emotionLabel = new Label(summary.getDominantEmotion());
        emotionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6B46C1;");
        Label emotionDesc = new Label("Dominant Emotion");
        emotionDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280;");
        emotionBox.getChildren().addAll(emotionLabel, emotionDesc);
        
        summaryBox.getChildren().addAll(totalChatsBox, sentimentBox, emotionBox);
        
        section.getChildren().addAll(title, summaryBox);
        
        return section;
    }
    
    private VBox createHistoryControls() {
        VBox controls = new VBox(15);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(20, 0, 0, 0));
        
        int userId = controller.getCurrentUser() != null ? controller.getCurrentUser().getUserId() : 0;
        
        if (userId <= 0) {
            return controls; // No controls for anonymous users
        }
        
        Label controlsTitle = new Label("🗂️ History Management");
        controlsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button clearMoodBtn = new Button("🗑️ Clear Mood History");
        clearMoodBtn.getStyleClass().add("warning-button");
        clearMoodBtn.setOnAction(e -> handleClearMoodHistory());
        
        Button clearChatBtn = new Button("🗑️ Clear Chat History");
        clearChatBtn.getStyleClass().add("warning-button");
        clearChatBtn.setOnAction(e -> handleClearChatHistory());
        
        Button clearAllBtn = new Button("❌ Clear All History");
        clearAllBtn.getStyleClass().add("danger-button");
        clearAllBtn.setOnAction(e -> handleClearAllHistory());
        
        buttonContainer.getChildren().addAll(clearMoodBtn, clearChatBtn, clearAllBtn);
        
        Label warningLabel = new Label("⚠️ Warning: Clearing history cannot be undone!");
        warningLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #DC2626; -fx-font-style: italic;");
        
        controls.getChildren().addAll(controlsTitle, buttonContainer, warningLabel);
        
        return controls;
    }
    
    private void handleClearMoodHistory() {
        if (!controller.showConfirmation("Clear Mood History", 
            "Are you sure you want to delete all your mood tracking history?\n\nThis action cannot be undone!")) {
            return;
        }
        
        int userId = controller.getCurrentUser().getUserId();
        boolean success = moodService.deleteUserMoodHistory(userId);
        
        if (success) {
            controller.showInfoAlert("Success", "Your mood history has been cleared.");
            // Refresh the view
            controller.showDashboardView();
        } else {
            controller.showWarningAlert("Warning", "No mood history found to delete or an error occurred.");
        }
    }
    
    private void handleClearChatHistory() {
        if (!controller.showConfirmation("Clear Chat History", 
            "Are you sure you want to delete all your chat conversation history?\n\nThis action cannot be undone!")) {
            return;
        }
        
        int userId = controller.getCurrentUser().getUserId();
        boolean success = chatService.deleteUserChatHistory(userId);
        
        if (success) {
            controller.showInfoAlert("Success", "Your chat history has been cleared.");
            // Refresh the view
            controller.showDashboardView();
        } else {
            controller.showWarningAlert("Warning", "No chat history found to delete or an error occurred.");
        }
    }
    
    private void handleClearAllHistory() {
        if (!controller.showConfirmation("Clear All History", 
            "Are you sure you want to delete ALL your history data?\n\nThis includes:\n• All mood entries\n• All chat conversations\n\nThis action cannot be undone!")) {
            return;
        }
        
        int userId = controller.getCurrentUser().getUserId();
        boolean moodDeleted = moodService.deleteUserMoodHistory(userId);
        boolean chatDeleted = chatService.deleteUserChatHistory(userId);
        
        if (moodDeleted || chatDeleted) {
            controller.showInfoAlert("Success", "Your history has been cleared.");
            // Refresh the view
            controller.showDashboardView();
        } else {
            controller.showWarningAlert("Warning", "No history found to delete or an error occurred.");
        }
    }
}
