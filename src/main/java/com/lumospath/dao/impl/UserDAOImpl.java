package com.lumospath.dao.impl;

import com.lumospath.dao.UserDAO;
import com.lumospath.model.User;
import com.lumospath.util.DatabaseUtil;
import com.lumospath.util.DatabaseUtil.ResultSetHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of UserDAO
 */
public class UserDAOImpl implements UserDAO {
    
    private static final String TABLE_NAME = "users";
    
    // SQL Queries
    private static final String INSERT_USER = 
        "INSERT INTO " + TABLE_NAME + " (username, email, password_hash, first_name, last_name, age, location, created_at, last_active, anonymous) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_USER = 
        "UPDATE " + TABLE_NAME + " SET username = ?, email = ?, first_name = ?, last_name = ?, age = ?, location = ?, last_active = ?, anonymous = ? WHERE user_id = ?";
    
    private static final String FIND_BY_ID = 
        "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";
    
    private static final String FIND_ALL = 
        "SELECT * FROM " + TABLE_NAME + " ORDER BY created_at DESC";
    
    private static final String FIND_BY_USERNAME = 
        "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
    
    private static final String FIND_BY_EMAIL = 
        "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
    
    private static final String DELETE_BY_ID = 
        "DELETE FROM " + TABLE_NAME + " WHERE user_id = ?";
    
    private static final String COUNT_ALL = 
        "SELECT COUNT(*) FROM " + TABLE_NAME;
    
    private static final String EXISTS_BY_ID = 
        "SELECT 1 FROM " + TABLE_NAME + " WHERE user_id = ?";
    
    private static final String UPDATE_LAST_ACTIVE = 
        "UPDATE " + TABLE_NAME + " SET last_active = ? WHERE user_id = ?";
    
    private static final String EXISTS_BY_USERNAME = 
        "SELECT 1 FROM " + TABLE_NAME + " WHERE username = ?";
    
    private static final String EXISTS_BY_EMAIL = 
        "SELECT 1 FROM " + TABLE_NAME + " WHERE email = ?";
    
    private static final String COUNT_ANONYMOUS = 
        "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE anonymous = 1";
    
    private static final String SET_USER_PREFERENCE = 
        "INSERT OR REPLACE INTO user_preferences (user_id, preference_key, preference_value, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
    
    private static final String GET_USER_PREFERENCE = 
        "SELECT preference_value FROM user_preferences WHERE user_id = ? AND preference_key = ?";
    
    private static final String AUTHENTICATE_USER = 
        "SELECT * FROM " + TABLE_NAME + " WHERE (username = ? OR email = ?) AND password_hash = ?";
    
    private static final String UPDATE_PASSWORD = 
        "UPDATE " + TABLE_NAME + " SET password_hash = ? WHERE user_id = ?";
    
    private static final String INSERT_AUTH_HISTORY = 
        "INSERT INTO user_auth_history (user_id, login_time, ip_address, user_agent) VALUES (?, ?, ?, ?)";
    
    private static final String UPDATE_AUTH_LOGOUT = 
        "UPDATE user_auth_history SET logout_time = ?, session_duration_minutes = ? WHERE auth_id = ?";
    
    @Override
    public User save(User user) throws SQLException {
        if (user.getUserId() != null && user.getUserId() > 0) {
            return update(user);
        }
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.setObject(6, user.getAge());
            pstmt.setString(7, user.getLocation());
            pstmt.setString(8, DatabaseUtil.formatDateTime(user.getCreatedAt()));
            pstmt.setString(9, DatabaseUtil.formatDateTime(user.getLastActive()));
            pstmt.setBoolean(10, user.isAnonymous());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
        
        return user;
    }
    
    @Override
    public User update(User user) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setObject(5, user.getAge());
            pstmt.setString(6, user.getLocation());
            pstmt.setString(7, DatabaseUtil.formatDateTime(user.getLastActive()));
            pstmt.setBoolean(8, user.isAnonymous());
            pstmt.setInt(9, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
        
        return user;
    }
    
    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        return DatabaseUtil.executeQuery(FIND_BY_ID, this::mapResultSetToUser, id);
    }
    
    @Override
    public List<User> findAll() throws SQLException {
        return DatabaseUtil.executeQuery(FIND_ALL, this::mapResultSetToUserList);
    }
    
    @Override
    public boolean deleteById(Integer id) throws SQLException {
        int rowsAffected = DatabaseUtil.executeUpdate(DELETE_BY_ID, id);
        return rowsAffected > 0;
    }
    
    @Override
    public boolean delete(User user) throws SQLException {
        return deleteById(user.getUserId());
    }
    
    @Override
    public boolean existsById(Integer id) throws SQLException {
        return DatabaseUtil.executeQuery(EXISTS_BY_ID, rs -> rs.next(), id);
    }
    
    @Override
    public long count() throws SQLException {
        return DatabaseUtil.executeQuery(COUNT_ALL, rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        });
    }
    
    @Override
    public List<User> saveAll(List<User> users) throws SQLException {
        List<User> savedUsers = new ArrayList<>();
        for (User user : users) {
            savedUsers.add(save(user));
        }
        return savedUsers;
    }
    
    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        return DatabaseUtil.executeQuery(FIND_BY_USERNAME, this::mapResultSetToUser, username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        return DatabaseUtil.executeQuery(FIND_BY_EMAIL, this::mapResultSetToUser, email);
    }
    
    @Override
    public boolean updateLastActive(Integer userId, LocalDateTime lastActive) throws SQLException {
        int rowsAffected = DatabaseUtil.executeUpdate(UPDATE_LAST_ACTIVE, 
                                                      DatabaseUtil.formatDateTime(lastActive), userId);
        return rowsAffected > 0;
    }
    
    @Override
    public boolean existsByUsername(String username) throws SQLException {
        return DatabaseUtil.executeQuery(EXISTS_BY_USERNAME, rs -> rs.next(), username);
    }
    
    @Override
    public boolean existsByEmail(String email) throws SQLException {
        return DatabaseUtil.executeQuery(EXISTS_BY_EMAIL, rs -> rs.next(), email);
    }
    
    @Override
    public long countAnonymousUsers() throws SQLException {
        return DatabaseUtil.executeQuery(COUNT_ANONYMOUS, rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;
        });
    }
    
    @Override
    public boolean setUserPreference(Integer userId, String key, String value) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        int rowsAffected = DatabaseUtil.executeUpdate(SET_USER_PREFERENCE,
                                                      userId, key, value, 
                                                      DatabaseUtil.formatDateTime(now),
                                                      DatabaseUtil.formatDateTime(now));
        return rowsAffected > 0;
    }
    
    @Override
    public String getUserPreference(Integer userId, String key) throws SQLException {
        return DatabaseUtil.executeQuery(GET_USER_PREFERENCE, rs -> {
            if (rs.next()) {
                return rs.getString("preference_value");
            }
            return null;
        }, userId, key);
    }
    
    @Override
    public Optional<User> authenticateUser(String usernameOrEmail, String passwordHash) throws SQLException {
        return DatabaseUtil.executeQuery(AUTHENTICATE_USER, this::mapResultSetToUser, 
                                        usernameOrEmail, usernameOrEmail, passwordHash);
    }
    
    @Override
    public Integer recordLogin(Integer userId, String ipAddress, String userAgent) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_AUTH_HISTORY, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, DatabaseUtil.formatDateTime(LocalDateTime.now()));
            pstmt.setString(3, ipAddress);
            pstmt.setString(4, userAgent);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Recording login failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Recording login failed, no ID obtained.");
                }
            }
        }
    }
    
    @Override
    public boolean recordLogout(Integer authId) throws SQLException {
        // Calculate session duration
        LocalDateTime logoutTime = LocalDateTime.now();
        
        // First, get the login time to calculate duration
        String getLoginTimeSql = "SELECT login_time FROM user_auth_history WHERE auth_id = ?";
        LocalDateTime loginTime = DatabaseUtil.executeQuery(getLoginTimeSql, rs -> {
            if (rs.next()) {
                return DatabaseUtil.parseDateTime(rs.getString("login_time"));
            }
            return null;
        }, authId);
        
        if (loginTime != null) {
            long sessionMinutes = java.time.Duration.between(loginTime, logoutTime).toMinutes();
            int rowsAffected = DatabaseUtil.executeUpdate(UPDATE_AUTH_LOGOUT,
                                                          DatabaseUtil.formatDateTime(logoutTime),
                                                          sessionMinutes,
                                                          authId);
            return rowsAffected > 0;
        }
        
        return false;
    }
    
    @Override
    public boolean updatePassword(Integer userId, String newPasswordHash) throws SQLException {
        int rowsAffected = DatabaseUtil.executeUpdate(UPDATE_PASSWORD, newPasswordHash, userId);
        return rowsAffected > 0;
    }
    
    // Helper methods for mapping ResultSet to User objects
    private Optional<User> mapResultSetToUser(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return Optional.of(extractUserFromResultSet(rs));
        }
        return Optional.empty();
    }
    
    private List<User> mapResultSetToUserList(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(extractUserFromResultSet(rs));
        }
        return users;
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        
        Object ageObj = rs.getObject("age");
        if (ageObj != null) {
            user.setAge((Integer) ageObj);
        }
        
        user.setLocation(rs.getString("location"));
        user.setCreatedAt(DatabaseUtil.parseDateTime(rs.getString("created_at")));
        user.setLastActive(DatabaseUtil.parseDateTime(rs.getString("last_active")));
        user.setAnonymous(rs.getBoolean("anonymous"));
        
        return user;
    }
}