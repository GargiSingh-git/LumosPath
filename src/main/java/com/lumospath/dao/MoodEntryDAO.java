package com.lumospath.dao;

import com.lumospath.model.MoodEntry;
import com.lumospath.model.MoodType;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object interface for MoodEntry operations
 */
public interface MoodEntryDAO extends BaseDAO<MoodEntry, Integer> {
    
    /**
     * Find mood entries by user ID
     * @param userId The user ID
     * @return List of mood entries for the user
     * @throws SQLException if database operation fails
     */
    List<MoodEntry> findByUserId(Integer userId) throws SQLException;
    
    /**
     * Find mood entries by user ID within date range
     * @param userId The user ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of mood entries within the date range
     * @throws SQLException if database operation fails
     */
    List<MoodEntry> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Find mood entries by mood type
     * @param userId The user ID
     * @param moodType The mood type
     * @return List of mood entries with the specified type
     * @throws SQLException if database operation fails
     */
    List<MoodEntry> findByUserIdAndMoodType(Integer userId, MoodType moodType) throws SQLException;
    
    /**
     * Get recent mood entries for a user
     * @param userId The user ID
     * @param limit Maximum number of entries to return
     * @return List of recent mood entries
     * @throws SQLException if database operation fails
     */
    List<MoodEntry> findRecentByUserId(Integer userId, int limit) throws SQLException;
    
    /**
     * Get average mood scale for a user within date range
     * @param userId The user ID
     * @param startDate Start date
     * @param endDate End date
     * @return Average mood scale
     * @throws SQLException if database operation fails
     */
    double getAverageMoodScale(Integer userId, LocalDateTime startDate, LocalDateTime endDate) throws SQLException;
    
    /**
     * Get mood statistics for a user
     * @param userId The user ID
     * @param days Number of days to look back
     * @return List of mood type counts
     * @throws SQLException if database operation fails
     */
    List<Object[]> getMoodStatistics(Integer userId, int days) throws SQLException;
    
    /**
     * Find mood entries with triggers containing specific text
     * @param userId The user ID
     * @param triggerText Text to search for in triggers
     * @return List of matching mood entries
     * @throws SQLException if database operation fails
     */
    List<MoodEntry> findByTriggerContaining(Integer userId, String triggerText) throws SQLException;
    
    /**
     * Get mood trend data for chart visualization
     * @param userId The user ID
     * @param days Number of days to look back
     * @return List of date and average mood scale pairs
     * @throws SQLException if database operation fails
     */
    List<Object[]> getMoodTrendData(Integer userId, int days) throws SQLException;
    
    /**
     * Count mood entries for a user
     * @param userId The user ID
     * @return Total count of mood entries
     * @throws SQLException if database operation fails
     */
    long countByUserId(Integer userId) throws SQLException;
    
    /**
     * Delete old mood entries (for data cleanup)
     * @param olderThan Delete entries older than this date
     * @return Number of entries deleted
     * @throws SQLException if database operation fails
     */
    int deleteOldEntries(LocalDateTime olderThan) throws SQLException;
}