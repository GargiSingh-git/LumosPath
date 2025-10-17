package com.lumospath.dao;

import com.lumospath.model.User;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Data Access Object interface for User operations
 */
public interface UserDAO extends BaseDAO<User, Integer> {
    
    /**
     * Find user by username
     * @param username The username to search for
     * @return Optional containing the user if found
     * @throws SQLException if database operation fails
     */
    Optional<User> findByUsername(String username) throws SQLException;
    
    /**
     * Find user by email
     * @param email The email to search for
     * @return Optional containing the user if found
     * @throws SQLException if database operation fails
     */
    Optional<User> findByEmail(String email) throws SQLException;
    
    /**
     * Update user's last active timestamp
     * @param userId The user ID
     * @param lastActive The last active timestamp
     * @return true if updated successfully
     * @throws SQLException if database operation fails
     */
    boolean updateLastActive(Integer userId, LocalDateTime lastActive) throws SQLException;
    
    /**
     * Check if username already exists
     * @param username The username to check
     * @return true if username exists
     * @throws SQLException if database operation fails
     */
    boolean existsByUsername(String username) throws SQLException;
    
    /**
     * Check if email already exists
     * @param email The email to check
     * @return true if email exists
     * @throws SQLException if database operation fails
     */
    boolean existsByEmail(String email) throws SQLException;
    
    /**
     * Get count of anonymous users
     * @return Count of anonymous users
     * @throws SQLException if database operation fails
     */
    long countAnonymousUsers() throws SQLException;
    
    /**
     * Set user preference
     * @param userId User ID
     * @param key Preference key
     * @param value Preference value
     * @return true if set successfully
     * @throws SQLException if database operation fails
     */
    boolean setUserPreference(Integer userId, String key, String value) throws SQLException;
    
    /**
     * Get user preference
     * @param userId User ID
     * @param key Preference key
     * @return Preference value or null if not found
     * @throws SQLException if database operation fails
     */
    String getUserPreference(Integer userId, String key) throws SQLException;
    
    /**
     * Authenticate user with username/email and password
     * @param usernameOrEmail The username or email
     * @param passwordHash The hashed password
     * @return Optional containing the user if authentication succeeds
     * @throws SQLException if database operation fails
     */
    Optional<User> authenticateUser(String usernameOrEmail, String passwordHash) throws SQLException;
    
    /**
     * Record user login in authentication history
     * @param userId User ID
     * @param ipAddress IP address of login
     * @param userAgent User agent string
     * @return Authentication history ID
     * @throws SQLException if database operation fails
     */
    Integer recordLogin(Integer userId, String ipAddress, String userAgent) throws SQLException;
    
    /**
     * Record user logout in authentication history
     * @param authId Authentication history ID from login
     * @return true if updated successfully
     * @throws SQLException if database operation fails
     */
    boolean recordLogout(Integer authId) throws SQLException;
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPasswordHash New hashed password
     * @return true if updated successfully
     * @throws SQLException if database operation fails
     */
    boolean updatePassword(Integer userId, String newPasswordHash) throws SQLException;
}