package com.lumospath.gui.views;

import java.util.ArrayList;
import java.util.List;

import com.lumospath.chatbot.LumosBot;
import com.lumospath.gui.controllers.MainController;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatView {
    private final MainController controller;
    private final LumosBot chatbot;
    private final List<Label> chatMessages;
    private ScrollPane chatScrollPane;
    private VBox chatMessagesContainer;
    
    public ChatView(MainController controller, LumosBot chatbot) {
        this.controller = controller;
        this.chatbot = chatbot;
        this.chatMessages = new ArrayList<>();
    }
    
    public ScrollPane createView() {
        VBox container = new VBox(24);
        container.getStyleClass().add("main-container");
        container.setPadding(new Insets(32));
        
        // Enhanced header with gradient background
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));
        
        Label title = new Label("🤖 LumosBot AI Companion");
        title.getStyleClass().add("header-title");
        
        Label subtitle = new Label("Your empathetic AI friend for emotional support and spiritual guidance");
        subtitle.getStyleClass().add("header-subtitle");
        
        header.getChildren().addAll(title, subtitle);
        
        // Enhanced chat messages area with modern styling
        chatMessagesContainer = new VBox(16);
        chatMessagesContainer.setPadding(new Insets(24));
        chatMessagesContainer.getStyleClass().add("chat-messages");
        
        // Add welcome message with enhanced styling
        addBotMessage("🌟 Welcome, beautiful soul! I'm LumosBot, your AI companion for emotional support and spiritual guidance.\n\n" +
                     "I'm here to:\n" +
                     "💙 Listen with empathy and understanding\n" +
                     "📚 Share wisdom from various inspirational sources\n" +
                     "🤗 Provide emotional support and encouragement\n" +
                     "✨ Help you navigate life's challenges\n\n" +
                     "What's on your heart today? I'm here to listen and support you. 💕");
        
        chatScrollPane = new ScrollPane(chatMessagesContainer);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setPrefHeight(450);
        chatScrollPane.getStyleClass().add("scroll-pane");
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Enhanced input area with modern design
        VBox inputContainer = new VBox(16);
        inputContainer.setPadding(new Insets(20));
        inputContainer.getStyleClass().add("surface-card");
        
        Label inputLabel = new Label("Share your thoughts with LumosBot:");
        inputLabel.getStyleClass().add("form-label");
        
        TextArea messageInput = new TextArea();
        messageInput.setPromptText("Type your message here...\nTry: 'I feel sad today' or 'Give me wisdom from the Gita' or 'I need encouragement'");
        messageInput.getStyleClass().add("form-field");
        messageInput.setPrefRowCount(3);
        messageInput.setWrapText(true);
        messageInput.setMaxHeight(120);
        
        // Enhanced button layout with better spacing
        HBox buttonContainer = new HBox(12);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(8, 0, 0, 0));
        
        Button sendBtn = new Button("💬 Send");
        sendBtn.getStyleClass().add("primary-button");
        
        Button quoteBtn = new Button("✨ Quote");
        quoteBtn.getStyleClass().add("secondary-button");
        
        Button wisdomBtn = new Button("📿 Wisdom");
        wisdomBtn.getStyleClass().add("secondary-button");
        
        Button clearBtn = new Button("🔄 Clear");
        clearBtn.getStyleClass().add("secondary-button");
        
        // Event handlers
        sendBtn.setOnAction(e -> {
            String message = messageInput.getText().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                messageInput.clear();
            }
        });
        
        quoteBtn.setOnAction(e -> {
            sendMessage("I would like an inspirational quote please");
        });
        
        wisdomBtn.setOnAction(e -> {
            sendMessage("Please share some inspirational wisdom");
        });
        
        clearBtn.setOnAction(e -> {
            clearChat();
        });
        
        // Enhanced Enter key handling
        messageInput.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER") && !e.isShiftDown()) {
                String message = messageInput.getText().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    messageInput.clear();
                }
                e.consume();
            }
        });
        
        buttonContainer.getChildren().addAll(sendBtn, quoteBtn, wisdomBtn, clearBtn);
        inputContainer.getChildren().addAll(inputLabel, messageInput, buttonContainer);
        
        container.getChildren().addAll(header, chatScrollPane, inputContainer);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        return scrollPane;
    }
    
    private void sendMessage(String userMessage) {
        // Add user message to chat
        addUserMessage(userMessage);
        
        // Get AI response using enhanced chatbot
        Platform.runLater(() -> {
            try {
                String botResponse = chatbot.getResponse(userMessage);
                addBotMessage(botResponse);
                
                // Save chat interaction to database
                saveChatInteraction(userMessage, botResponse);
                
                // Show conversation summary after every 5 messages
                if (chatMessages.size() % 10 == 0 && chatMessages.size() > 0) {
                    String summary = chatbot.getConversationSummary();
                    addSystemMessage("💭 Conversation insight: " + summary);
                }
                
            } catch (Exception e) {
                String errorResponse = "I apologize, but I encountered a small hiccup. Let me try again...\n\n" +
                                     "I'm here to support you. Please tell me how you're feeling or what you'd like to talk about.";
                addBotMessage(errorResponse);
                
                // Save error interaction too
                saveChatInteraction(userMessage, errorResponse);
            }
        });
    }
    
    private void saveChatInteraction(String userMessage, String botResponse) {
        try {
            Integer userId = controller.getCurrentUser() != null ? controller.getCurrentUser().getUserId() : null;
            
            // Don't save for user ID 0 or null (anonymous users)
            if (userId == null || userId <= 0) {
                return; // Skip saving for anonymous users
            }
            
            // For now, we'll save with basic sentiment analysis from the chatbot
            Double sentimentScore = 0.0; // You could enhance this with actual sentiment analysis
            String detectedEmotion = "neutral"; // You could enhance this with actual emotion detection
            
            controller.getChatHistoryService().saveChatLog(userId, userMessage, botResponse, sentimentScore, detectedEmotion);
            
        } catch (Exception e) {
            System.err.println("Error saving chat interaction: " + e.getMessage());
            // Don't show error to user, just log it
        }
    }
    
    private void addUserMessage(String message) {
        Label userMsg = new Label("You: " + message);
        userMsg.getStyleClass().add("user-message");
        userMsg.setWrapText(true);
        userMsg.setMaxWidth(400);
        
        HBox userMsgContainer = new HBox();
        userMsgContainer.setAlignment(Pos.CENTER_RIGHT);
        userMsgContainer.getChildren().add(userMsg);
        
        chatMessagesContainer.getChildren().add(userMsgContainer);
        chatMessages.add(userMsg);
        scrollToBottom();
    }
    
    private void addBotMessage(String message) {
        Label botMsg = new Label("🤖 LumosBot: " + message);
        botMsg.getStyleClass().add("bot-message");
        botMsg.setWrapText(true);
        botMsg.setMaxWidth(400);
        
        HBox botMsgContainer = new HBox();
        botMsgContainer.setAlignment(Pos.CENTER_LEFT);
        botMsgContainer.getChildren().add(botMsg);
        
        chatMessagesContainer.getChildren().add(botMsgContainer);
        chatMessages.add(botMsg);
        scrollToBottom();
    }
    
    private void addSystemMessage(String message) {
        Label sysMsg = new Label("📊 " + message);
        sysMsg.getStyleClass().add("system-message");
        sysMsg.setWrapText(true);
        sysMsg.setMaxWidth(400);
        sysMsg.setStyle("-fx-background-color: #fef3c7; -fx-padding: 12px 16px; -fx-background-radius: 12px; -fx-text-fill: #92400e; -fx-font-style: italic; -fx-font-size: 13px;");
        
        HBox sysMsgContainer = new HBox();
        sysMsgContainer.setAlignment(Pos.CENTER);
        sysMsgContainer.getChildren().add(sysMsg);
        
        chatMessagesContainer.getChildren().add(sysMsgContainer);
        scrollToBottom();
    }
    
    private void scrollToBottom() {
        Platform.runLater(() -> {
            chatScrollPane.setVvalue(1.0);
        });
    }
    
    private void clearChat() {
        chatMessagesContainer.getChildren().clear();
        chatMessages.clear();
        chatbot.resetConversation();
        
        // Add fresh welcome message with enhanced styling
        addBotMessage("🌟 Fresh start! I love new beginnings - they're like clean slates full of possibilities!\n\n" +
                     "I'm here to support you with:\n" +
                     "💙 Empathetic listening and understanding\n" +
                     "📿 Wisdom from the Bhagavad-gita\n" +
                     "🤗 Emotional support and encouragement\n" +
                     "✨ Guidance through life's challenges\n\n" +
                     "What's happening in your world right now? I'm here with my full attention and an open heart. 💕");
    }
}
