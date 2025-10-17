package com.lumospath.dao;

import com.lumospath.model.MeditationSession;
import com.lumospath.model.MeditationType;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for MeditationSession operations
 */
public interface MeditationSessionDAO extends BaseDAO<MeditationSession, Integer> {
    
    /**
     * Find meditation sessions by type
     * @param type The meditation type
     * @return List of sessions with the specified type
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> findByType(MeditationType type) throws SQLException;
    
    /**
     * Find meditation sessions by duration range
     * @param minMinutes Minimum duration in minutes
     * @param maxMinutes Maximum duration in minutes
     * @return List of sessions within the duration range
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> findByDurationRange(int minMinutes, int maxMinutes) throws SQLException;
    
    /**
     * Find meditation sessions by instructor
     * @param instructor The instructor name
     * @return List of sessions by the instructor
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> findByInstructor(String instructor) throws SQLException;
    
    /**
     * Get most popular meditation sessions based on completion count
     * @param limit Maximum number of sessions to return
     * @return List of popular sessions
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> findMostPopular(int limit) throws SQLException;
    
    /**
     * Get highest rated meditation sessions
     * @param limit Maximum number of sessions to return
     * @return List of highly rated sessions
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> findHighestRated(int limit) throws SQLException;
    
    /**
     * Update completion count for a session
     * @param sessionId The session ID
     * @return true if updated successfully
     * @throws SQLException if database operation fails
     */
    boolean incrementCompletionCount(Integer sessionId) throws SQLException;
    
    /**
     * Update average rating for a session
     * @param sessionId The session ID
     * @param newRating New rating to include in average
     * @return true if updated successfully
     * @throws SQLException if database operation fails
     */
    boolean updateAverageRating(Integer sessionId, double newRating) throws SQLException;
    
    /**
     * Search meditation sessions by title or description
     * @param searchText Text to search for
     * @return List of matching sessions
     * @throws SQLException if database operation fails
     */
    List<MeditationSession> searchSessions(String searchText) throws SQLException;
    
    /**
     * Get sessions with steps loaded
     * @param sessionId The session ID
     * @return Meditation session with steps
     * @throws SQLException if database operation fails
     */
    MeditationSession findByIdWithSteps(Integer sessionId) throws SQLException;
    
    /**
     * Save session with steps in a transaction
     * @param session The meditation session to save
     * @return Saved session with generated ID
     * @throws SQLException if database operation fails
     */
    MeditationSession saveWithSteps(MeditationSession session) throws SQLException;
}