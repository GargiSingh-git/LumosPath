package com.lumospath.service;

import com.lumospath.dao.ChatLogDAO;
import com.lumospath.dao.impl.ChatLogDAOImpl;
import com.lumospath.model.ChatLog;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing chat history storage and retrieval
 */
public class ChatHistoryService {
    private final ChatLogDAO chatLogDAO;

    public ChatHistoryService() {
        this.chatLogDAO = new ChatLogDAOImpl();
    }

    /**
     * Save a chat interaction to the database
     * @param userId User ID (null for anonymous users)
     * @param userMessage The user's message
     * @param botResponse The bot's response
     * @param sentimentScore Sentiment analysis score
     * @param detectedEmotion Detected emotion
     * @return The saved ChatLog
     */
    public ChatLog saveChatLog(Integer userId, String userMessage, String botResponse, 
                              Double sentimentScore, String detectedEmotion) {
        try {
            // Don't save for anonymous users (userId is null or 0)
            if (userId == null || userId <= 0) {
                // Return a transient chat log for anonymous users
                return new ChatLog(userId, userMessage, botResponse, sentimentScore, detectedEmotion);
            }
            
            ChatLog chatLog = new ChatLog(userId, userMessage, botResponse, sentimentScore, detectedEmotion);
            return chatLogDAO.save(chatLog);
        } catch (SQLException e) {
            System.err.println("Error saving chat log: " + e.getMessage());
            // Return a transient chat log if database save fails
            return new ChatLog(userId, userMessage, botResponse, sentimentScore, detectedEmotion);
        }
    }

    /**
     * Get chat history for a specific user
     * @param userId The user ID
     * @return List of chat logs for the user
     */
    public List<ChatLog> getChatHistory(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                return new ArrayList<>(); // Anonymous users have no persistent history
            }
            return chatLogDAO.findByUserId(userId);
        } catch (SQLException e) {
            System.err.println("Error retrieving chat history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get recent chat history for a user
     * @param userId The user ID
     * @param limit Maximum number of entries to return
     * @return List of recent chat logs
     */
    public List<ChatLog> getRecentChatHistory(Integer userId, int limit) {
        try {
            if (userId == null || userId <= 0) {
                return new ArrayList<>(); // Anonymous users have no persistent history
            }
            return chatLogDAO.findRecentByUserId(userId, limit);
        } catch (SQLException e) {
            System.err.println("Error retrieving recent chat history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get chat history within a date range
     * @param userId The user ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of chat logs within the date range
     */
    public List<ChatLog> getChatHistoryByDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (userId == null || userId <= 0) {
                return new ArrayList<>();
            }
            return chatLogDAO.findByUserIdAndDateRange(userId, startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Error retrieving chat history by date range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get chat logs by detected emotion
     * @param userId The user ID
     * @param emotion The detected emotion
     * @return List of chat logs with the specified emotion
     */
    public List<ChatLog> getChatHistoryByEmotion(Integer userId, String emotion) {
        try {
            if (userId == null || userId <= 0) {
                return new ArrayList<>();
            }
            return chatLogDAO.findByUserIdAndEmotion(userId, emotion);
        } catch (SQLException e) {
            System.err.println("Error retrieving chat history by emotion: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Count total chat interactions for a user
     * @param userId The user ID
     * @return Total count of chat logs
     */
    public long getChatCount(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                return 0;
            }
            return chatLogDAO.countByUserId(userId);
        } catch (SQLException e) {
            System.err.println("Error counting chat logs: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Delete all chat history for a user
     * @param userId The user ID
     * @return true if any records were deleted, false otherwise
     */
    public boolean deleteUserChatHistory(Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                return false;
            }
            
            int deletedCount = chatLogDAO.deleteAllByUserId(userId);
            return deletedCount > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting chat history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up old chat logs (older than specified date)
     * @param olderThan Delete entries older than this date
     * @return Number of entries deleted
     */
    public int cleanupOldChatLogs(LocalDateTime olderThan) {
        try {
            return chatLogDAO.deleteOldEntries(olderThan);
        } catch (SQLException e) {
            System.err.println("Error cleaning up old chat logs: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get conversation summary statistics for a user
     * @param userId The user ID
     * @param days Number of days to look back
     * @return Summary statistics
     */
    public ChatSummary getChatSummary(Integer userId, int days) {
        if (userId == null || userId <= 0) {
            return new ChatSummary(0, 0.0, "none");
        }

        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            List<ChatLog> logs = getChatHistoryByDateRange(userId, startDate, LocalDateTime.now());
            
            long totalChats = logs.size();
            double avgSentiment = logs.stream()
                .filter(log -> log.getSentimentScore() != null)
                .mapToDouble(ChatLog::getSentimentScore)
                .average()
                .orElse(0.0);
            
            String dominantEmotion = logs.stream()
                .filter(log -> log.getDetectedEmotion() != null && !log.getDetectedEmotion().isEmpty())
                .collect(java.util.stream.Collectors.groupingBy(
                    ChatLog::getDetectedEmotion, 
                    java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("neutral");
            
            return new ChatSummary(totalChats, avgSentiment, dominantEmotion);
        } catch (Exception e) {
            System.err.println("Error generating chat summary: " + e.getMessage());
            return new ChatSummary(0, 0.0, "error");
        }
    }

    /**
     * Inner class to represent chat summary statistics
     */
    public static class ChatSummary {
        private final long totalChats;
        private final double averageSentiment;
        private final String dominantEmotion;

        public ChatSummary(long totalChats, double averageSentiment, String dominantEmotion) {
            this.totalChats = totalChats;
            this.averageSentiment = averageSentiment;
            this.dominantEmotion = dominantEmotion;
        }

        public long getTotalChats() { return totalChats; }
        public double getAverageSentiment() { return averageSentiment; }
        public String getDominantEmotion() { return dominantEmotion; }

        public String getSentimentDescription() {
            if (averageSentiment > 0.3) return "Positive";
            if (averageSentiment < -0.3) return "Negative";
            return "Neutral";
        }

        @Override
        public String toString() {
            return String.format("ChatSummary{totalChats=%d, avgSentiment=%.2f (%s), dominantEmotion='%s'}", 
                totalChats, averageSentiment, getSentimentDescription(), dominantEmotion);
        }
    }
}