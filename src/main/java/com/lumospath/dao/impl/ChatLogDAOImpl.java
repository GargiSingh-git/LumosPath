package com.lumospath.dao.impl;

import com.lumospath.dao.ChatLogDAO;
import com.lumospath.model.ChatLog;
import com.lumospath.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ChatLogDAO for database operations
 */
public class ChatLogDAOImpl implements ChatLogDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ChatLog save(ChatLog chatLog) throws SQLException {
        String sql = "INSERT INTO chat_logs (user_id, user_message, bot_response, sentiment_score, detected_emotion, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Handle null userId for anonymous users
            if (chatLog.getUserId() != null && chatLog.getUserId() != 0) {
                pstmt.setInt(1, chatLog.getUserId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, chatLog.getUserMessage());
            pstmt.setString(3, chatLog.getBotResponse());
            
            // Handle null sentiment score
            if (chatLog.getSentimentScore() != null) {
                pstmt.setDouble(4, chatLog.getSentimentScore());
            } else {
                pstmt.setNull(4, Types.DOUBLE);
            }
            
            pstmt.setString(5, chatLog.getDetectedEmotion());
            pstmt.setString(6, chatLog.getCreatedAt().format(DATE_FORMATTER));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating chat log failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    chatLog.setLogId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating chat log failed, no ID obtained.");
                }
            }
            
            return chatLog;
        }
    }

    @Override
    public ChatLog update(ChatLog chatLog) throws SQLException {
        String sql = "UPDATE chat_logs SET user_message = ?, bot_response = ?, sentiment_score = ?, detected_emotion = ? WHERE log_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, chatLog.getUserMessage());
            pstmt.setString(2, chatLog.getBotResponse());
            
            if (chatLog.getSentimentScore() != null) {
                pstmt.setDouble(3, chatLog.getSentimentScore());
            } else {
                pstmt.setNull(3, Types.DOUBLE);
            }
            
            pstmt.setString(4, chatLog.getDetectedEmotion());
            pstmt.setInt(5, chatLog.getLogId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating chat log failed, no rows affected.");
            }
            
            return chatLog;
        }
    }

    @Override
    public Optional<ChatLog> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM chat_logs WHERE log_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToChatLog(rs));
                }
            }
        }
        
        return Optional.empty();
    }

    @Override
    public List<ChatLog> findAll() throws SQLException {
        String sql = "SELECT * FROM chat_logs ORDER BY created_at DESC";
        List<ChatLog> chatLogs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                chatLogs.add(mapResultSetToChatLog(rs));
            }
        }
        
        return chatLogs;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM chat_logs WHERE log_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(ChatLog chatLog) throws SQLException {
        return deleteById(chatLog.getLogId());
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM chat_logs WHERE log_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM chat_logs";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            return rs.next() ? rs.getLong(1) : 0;
        }
    }

    @Override
    public List<ChatLog> saveAll(List<ChatLog> entities) throws SQLException {
        List<ChatLog> savedEntities = new ArrayList<>();
        for (ChatLog log : entities) {
            savedEntities.add(save(log));
        }
        return savedEntities;
    }

    @Override
    public List<ChatLog> findByUserId(Integer userId) throws SQLException {
        String sql = "SELECT * FROM chat_logs WHERE user_id = ? ORDER BY created_at ASC";
        List<ChatLog> chatLogs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chatLogs.add(mapResultSetToChatLog(rs));
                }
            }
        }
        
        return chatLogs;
    }

    @Override
    public List<ChatLog> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT * FROM chat_logs WHERE user_id = ? AND created_at BETWEEN ? AND ? ORDER BY created_at ASC";
        List<ChatLog> chatLogs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, startDate.format(DATE_FORMATTER));
            pstmt.setString(3, endDate.format(DATE_FORMATTER));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chatLogs.add(mapResultSetToChatLog(rs));
                }
            }
        }
        
        return chatLogs;
    }

    @Override
    public List<ChatLog> findRecentByUserId(Integer userId, int limit) throws SQLException {
        String sql = "SELECT * FROM chat_logs WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        List<ChatLog> chatLogs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chatLogs.add(mapResultSetToChatLog(rs));
                }
            }
        }
        
        return chatLogs;
    }

    @Override
    public long countByUserId(Integer userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM chat_logs WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        }
    }

    @Override
    public int deleteOldEntries(LocalDateTime olderThan) throws SQLException {
        String sql = "DELETE FROM chat_logs WHERE created_at < ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, olderThan.format(DATE_FORMATTER));
            return pstmt.executeUpdate();
        }
    }

    @Override
    public int deleteAllByUserId(Integer userId) throws SQLException {
        String sql = "DELETE FROM chat_logs WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate();
        }
    }

    @Override
    public List<ChatLog> findByUserIdAndEmotion(Integer userId, String emotion) throws SQLException {
        String sql = "SELECT * FROM chat_logs WHERE user_id = ? AND detected_emotion = ? ORDER BY created_at DESC";
        List<ChatLog> chatLogs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, emotion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chatLogs.add(mapResultSetToChatLog(rs));
                }
            }
        }
        
        return chatLogs;
    }

    /**
     * Map ResultSet to ChatLog object
     */
    private ChatLog mapResultSetToChatLog(ResultSet rs) throws SQLException {
        ChatLog log = new ChatLog();
        log.setLogId(rs.getInt("log_id"));
        
        // Handle nullable user_id for anonymous users
        int userId = rs.getInt("user_id");
        if (rs.wasNull()) {
            log.setUserId(null);
        } else {
            log.setUserId(userId);
        }
        
        log.setUserMessage(rs.getString("user_message"));
        log.setBotResponse(rs.getString("bot_response"));
        
        // Handle nullable sentiment_score
        double sentimentScore = rs.getDouble("sentiment_score");
        if (rs.wasNull()) {
            log.setSentimentScore(null);
        } else {
            log.setSentimentScore(sentimentScore);
        }
        
        log.setDetectedEmotion(rs.getString("detected_emotion"));
        
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            log.setCreatedAt(LocalDateTime.parse(createdAtStr, DATE_FORMATTER));
        }
        
        return log;
    }
}