package com.lumospath.dao;

import com.lumospath.model.ChatLog;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object interface for ChatLog operations
 */
public interface ChatLogDAO extends BaseDAO<ChatLog, Integer> {
    
    /**
     * Find chat logs by user ID
     * @param userId The user ID
     * @return List of chat logs for the user
     * @throws SQLException if database operation fails
     */
    List<ChatLog> findByUserId(Integer userId) throws SQLException;
    
    /**
     * Find chat logs by user ID within date range
     * @param userId The user ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of chat logs within the date range
     * @throws SQLException if database operation fails
     */
    List<ChatLog> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Get recent chat logs for a user
     * @param userId The user ID
     * @param limit Maximum number of entries to return
     * @return List of recent chat logs
     * @throws SQLException if database operation fails
     */
    List<ChatLog> findRecentByUserId(Integer userId, int limit) throws SQLException;
    
    /**
     * Count chat logs for a user
     * @param userId The user ID
     * @return Total count of chat logs
     * @throws SQLException if database operation fails
     */
    long countByUserId(Integer userId) throws SQLException;
    
    /**
     * Delete old chat logs (for data cleanup)
     * @param olderThan Delete entries older than this date
     * @return Number of entries deleted
     * @throws SQLException if database operation fails
     */
    int deleteOldEntries(LocalDateTime olderThan) throws SQLException;
    
    /**
     * Delete all chat logs for a specific user
     * @param userId The user ID
     * @return Number of entries deleted
     * @throws SQLException if database operation fails
     */
    int deleteAllByUserId(Integer userId) throws SQLException;
    
    /**
     * Find chat logs by detected emotion
     * @param userId The user ID
     * @param emotion The detected emotion
     * @return List of chat logs with the specified emotion
     * @throws SQLException if database operation fails
     */
    List<ChatLog> findByUserIdAndEmotion(Integer userId, String emotion) throws SQLException;
}