package com.lumospath.model;

import java.time.LocalDateTime;

/**
 * ChatLog model class for tracking chatbot conversation history
 */
public class ChatLog {
    private int logId;
    private Integer userId; // Can be null for anonymous users
    private String userMessage;
    private String botResponse;
    private Double sentimentScore;
    private String detectedEmotion;
    private LocalDateTime createdAt;

    // Constructors
    public ChatLog() {
        this.createdAt = LocalDateTime.now();
    }

    public ChatLog(Integer userId, String userMessage, String botResponse) {
        this();
        this.userId = userId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
    }

    public ChatLog(Integer userId, String userMessage, String botResponse, Double sentimentScore, String detectedEmotion) {
        this(userId, userMessage, botResponse);
        this.sentimentScore = sentimentScore;
        this.detectedEmotion = detectedEmotion;
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }

    public Double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(Double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getDetectedEmotion() {
        return detectedEmotion;
    }

    public void setDetectedEmotion(String detectedEmotion) {
        this.detectedEmotion = detectedEmotion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public boolean isAnonymous() {
        return userId == null || userId == 0;
    }

    public String getFormattedTimestamp() {
        if (createdAt == null) {
            return "Unknown time";
        }
        return createdAt.toLocalDate().toString() + " at " + 
               createdAt.toLocalTime().toString().substring(0, 5);
    }

    @Override
    public String toString() {
        return "ChatLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", userMessage='" + userMessage + '\'' +
                ", botResponse='" + (botResponse != null ? botResponse.substring(0, Math.min(50, botResponse.length())) + "..." : null) + '\'' +
                ", sentimentScore=" + sentimentScore +
                ", detectedEmotion='" + detectedEmotion + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ChatLog chatLog = (ChatLog) obj;
        return logId == chatLog.logId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(logId);
    }
}