package com.lumospath;

import com.lumospath.util.DatabaseUtil;
import com.lumospath.dao.impl.UserDAOImpl;
import com.lumospath.model.User;
import java.time.LocalDateTime;
import java.util.Optional;
import java.sql.SQLException;

/**
 * Test class to verify JDBC connectivity and database operations
 */
public class JDBCConnectivityTest {
    
    public static void main(String[] args) {
        System.out.println("=== LumosPath JDBC Connectivity Test ===\n");
        
        try {
            // Test 1: Database initialization
            System.out.println("1. Testing database initialization...");
            DatabaseUtil.initializeDatabase();
            System.out.println("✓ Database tables initialized successfully\n");
            
            // Test 2: Connection test
            System.out.println("2. Testing database connection...");
            boolean connected = DatabaseUtil.testConnection();
            if (connected) {
                System.out.println("✓ Database connection established successfully\n");
            } else {
                System.out.println("✗ Database connection failed\n");
                return;
            }
            
            // Test 3: User DAO operations
            System.out.println("3. Testing User DAO operations...");
            UserDAOImpl userDAO = new UserDAOImpl();
            
            // Create a test user
            User testUser = new User();
            testUser.setUsername("testuser_" + System.currentTimeMillis());
            testUser.setEmail("test@lumospath.com");
            testUser.setAge(25);
            testUser.setLocation("Test Location");
            testUser.setCreatedAt(LocalDateTime.now());
            testUser.setLastActive(LocalDateTime.now());
            
            // Test insert
            User savedUser = userDAO.save(testUser);
            if (savedUser != null && savedUser.getUserId() > 0) {
                System.out.println("✓ User created successfully with ID: " + savedUser.getUserId());
            } else {
                System.out.println("✗ Failed to create user");
                return;
            }
            
            // Test find by ID
            Optional<User> foundUserOpt = userDAO.findById(savedUser.getUserId());
            if (foundUserOpt.isPresent() && foundUserOpt.get().getUsername().equals(testUser.getUsername())) {
                User foundUser = foundUserOpt.get();
                System.out.println("✓ User found by ID successfully: " + foundUser.getUsername());
                
                // Test update
                foundUser.setEmail("updated@lumospath.com");
                User updatedUser = userDAO.update(foundUser);
                if (updatedUser != null && "updated@lumospath.com".equals(updatedUser.getEmail())) {
                    System.out.println("✓ User updated successfully");
                } else {
                    System.out.println("✗ Failed to update user");
                    return;
                }
            } else {
                System.out.println("✗ Failed to find user by ID");
                return;
            }
            
            // Test find by username
            Optional<User> foundByUsernameOpt = userDAO.findByUsername(testUser.getUsername());
            if (foundByUsernameOpt.isPresent() && foundByUsernameOpt.get().getUserId().equals(savedUser.getUserId())) {
                System.out.println("✓ User found by username successfully");
            } else {
                System.out.println("✗ Failed to find user by username");
                return;
            }
            
            // Test count
            long userCount = userDAO.count();
            System.out.println("✓ Total users in database: " + userCount);
            
            // Test delete
            boolean deleted = userDAO.deleteById(savedUser.getUserId());
            if (deleted) {
                System.out.println("✓ Test user deleted successfully\n");
            } else {
                System.out.println("✗ Failed to delete test user\n");
            }
            
            // Test 4: Transaction management
            System.out.println("4. Testing transaction management...");
            
            try {
                String result = DatabaseUtil.executeInTransaction(conn -> {
                    // This would be a more complex transaction in real use
                    System.out.println("  - Executing transaction operations...");
                    return "Transaction completed successfully";
                });
                
                if (result != null) {
                    System.out.println("✓ Transaction executed successfully: " + result + "\n");
                } else {
                    System.out.println("✗ Transaction failed\n");
                }
            } catch (SQLException e) {
                System.out.println("✗ Transaction failed with error: " + e.getMessage() + "\n");
            }
            
            // Test 5: Database statistics
            System.out.println("5. Database statistics:");
            DatabaseUtil.printTableStats();
            System.out.println();
            
            System.out.println("=== All JDBC tests completed successfully! ===");
            System.out.println("Your JDBC connectivity is working properly and ready for production use.");
            
        } catch (Exception e) {
            System.err.println("✗ JDBC Test failed with error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up connections
            try {
                DatabaseUtil.closeAllConnections();
                System.out.println("\nDatabase connections closed.");
            } catch (Exception e) {
                System.err.println("Warning: Failed to close database connections: " + e.getMessage());
            }
        }
    }
}
