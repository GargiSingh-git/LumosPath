package com.lumospath;

import com.lumospath.dao.UserDAO;
import com.lumospath.dao.impl.UserDAOImpl;
import com.lumospath.model.User;
import com.lumospath.util.DatabaseUtil;

import java.time.LocalDateTime;

/**
 * Utility class to test database operations
 * This demonstrates the JDBC connectivity and DAO operations
 */
public class DatabaseTestUtil {
    
    public static void main(String[] args) {
        System.out.println("=== LumosPath Database Test Utility ===");
        
        try {
            // Initialize database
            DatabaseUtil.initializeDatabase();
            
            // Print database information
            DatabaseUtil.printDatabaseInfo();
            
            // Print table statistics
            DatabaseUtil.printTableStats();
            
            // Test UserDAO operations
            testUserDAO();
            
        } catch (Exception e) {
            System.err.println("Error during database test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            DatabaseUtil.closeAllConnections();
        }
        
        System.out.println("=== Database Test Complete ===");
    }
    
    private static void testUserDAO() throws Exception {
        System.out.println("\n=== Testing UserDAO Operations ===");
        
        UserDAO userDAO = new UserDAOImpl();
        
        // Create a test user
        User testUser = new User();
        testUser.setUsername("test_user_" + System.currentTimeMillis());
        testUser.setEmail("test@lumospath.com");
        testUser.setAge(25);
        testUser.setLocation("Test City");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setLastActive(LocalDateTime.now());
        testUser.setAnonymous(false);
        
        System.out.println("Creating test user: " + testUser.getUsername());
        
        // Save the user
        User savedUser = userDAO.save(testUser);
        System.out.println("User saved with ID: " + savedUser.getUserId());
        
        // Find user by ID
        var foundUser = userDAO.findById(savedUser.getUserId());
        if (foundUser.isPresent()) {
            System.out.println("User found by ID: " + foundUser.get().getUsername());
        } else {
            System.out.println("User not found by ID!");
        }
        
        // Find user by username
        var userByUsername = userDAO.findByUsername(savedUser.getUsername());
        if (userByUsername.isPresent()) {
            System.out.println("User found by username: " + userByUsername.get().getEmail());
        } else {
            System.out.println("User not found by username!");
        }
        
        // Update user
        savedUser.setLocation("Updated City");
        savedUser.setLastActive(LocalDateTime.now());
        User updatedUser = userDAO.update(savedUser);
        System.out.println("User updated. New location: " + updatedUser.getLocation());
        
        // Test user preferences
        boolean prefSet = userDAO.setUserPreference(savedUser.getUserId(), "theme", "dark");
        System.out.println("User preference set: " + prefSet);
        
        String theme = userDAO.getUserPreference(savedUser.getUserId(), "theme");
        System.out.println("User preference retrieved: theme = " + theme);
        
        // Count users
        long userCount = userDAO.count();
        System.out.println("Total users in database: " + userCount);
        
        // Test exists
        boolean exists = userDAO.existsById(savedUser.getUserId());
        System.out.println("User exists: " + exists);
        
        System.out.println("UserDAO test completed successfully!");
    }
}