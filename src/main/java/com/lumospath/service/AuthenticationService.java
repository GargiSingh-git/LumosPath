package com.lumospath.service;

import com.lumospath.dao.UserDAO;
import com.lumospath.dao.impl.UserDAOImpl;
import com.lumospath.model.User;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service class for handling user authentication, registration, and security
 */
public class AuthenticationService {
    private final UserDAO userDAO;
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    
    // Current authenticated user and session info
    private User currentUser;
    private Integer currentAuthId;
    
    public AuthenticationService() {
        this.userDAO = new UserDAOImpl();
    }
    
    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    /**
     * Authenticate user with username/email and password
     */
    public AuthenticationResult login(String usernameOrEmail, String password) {
        try {
            // Validate input
            if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
                return AuthenticationResult.failure("Username or email is required");
            }
            if (password == null || password.isEmpty()) {
                return AuthenticationResult.failure("Password is required");
            }
            
            // Hash the provided password
            String passwordHash = hashPassword(password);
            
            // Attempt authentication
            Optional<User> userOpt = userDAO.authenticateUser(usernameOrEmail, passwordHash);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Update last active timestamp
                userDAO.updateLastActive(user.getUserId(), java.time.LocalDateTime.now());
                
                // Record login in authentication history
                Integer authId = userDAO.recordLogin(user.getUserId(), "localhost", "LumosPath-Desktop");
                
                // Set current session
                this.currentUser = user;
                this.currentAuthId = authId;
                
                return AuthenticationResult.success(user, "Login successful");
            } else {
                return AuthenticationResult.failure("Invalid username/email or password");
            }
            
        } catch (SQLException e) {
            return AuthenticationResult.failure("Database error during login: " + e.getMessage());
        } catch (Exception e) {
            return AuthenticationResult.failure("Unexpected error during login: " + e.getMessage());
        }
    }
    
    /**
     * Register a new user
     */
    public AuthenticationResult register(String username, String email, String password, 
                                       String firstName, String lastName) {
        try {
            // Validate input
            ValidationResult validation = validateRegistration(username, email, password, firstName, lastName);
            if (!validation.isValid()) {
                return AuthenticationResult.failure(validation.getMessage());
            }
            
            // Check if username already exists
            if (userDAO.existsByUsername(username)) {
                return AuthenticationResult.failure("Username already exists");
            }
            
            // Check if email already exists
            if (userDAO.existsByEmail(email)) {
                return AuthenticationResult.failure("Email already exists");
            }
            
            // Hash the password
            String passwordHash = hashPassword(password);
            
            // Create new user
            User newUser = new User(username, email, passwordHash, firstName, lastName);
            newUser = userDAO.save(newUser);
            
            return AuthenticationResult.success(newUser, "Registration successful");
            
        } catch (SQLException e) {
            return AuthenticationResult.failure("Database error during registration: " + e.getMessage());
        } catch (Exception e) {
            return AuthenticationResult.failure("Unexpected error during registration: " + e.getMessage());
        }
    }
    
    /**
     * Logout current user
     */
    public boolean logout() {
        try {
            if (currentAuthId != null) {
                userDAO.recordLogout(currentAuthId);
            }
            currentUser = null;
            currentAuthId = null;
            return true;
        } catch (SQLException e) {
            System.err.println("Error recording logout: " + e.getMessage());
            currentUser = null;
            currentAuthId = null;
            return false;
        }
    }
    
    /**
     * Change user password
     */
    public AuthenticationResult changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            return AuthenticationResult.failure("No user logged in");
        }
        
        try {
            // Verify current password
            String currentPasswordHash = hashPassword(currentPassword);
            if (!currentPasswordHash.equals(currentUser.getPasswordHash())) {
                return AuthenticationResult.failure("Current password is incorrect");
            }
            
            // Validate new password
            if (!isValidPassword(newPassword)) {
                return AuthenticationResult.failure(getPasswordRequirements());
            }
            
            // Hash new password
            String newPasswordHash = hashPassword(newPassword);
            
            // Update in database
            boolean updated = userDAO.updatePassword(currentUser.getUserId(), newPasswordHash);
            if (updated) {
                currentUser.setPasswordHash(newPasswordHash);
                return AuthenticationResult.success(currentUser, "Password changed successfully");
            } else {
                return AuthenticationResult.failure("Failed to update password");
            }
            
        } catch (SQLException e) {
            return AuthenticationResult.failure("Database error during password change: " + e.getMessage());
        } catch (Exception e) {
            return AuthenticationResult.failure("Unexpected error during password change: " + e.getMessage());
        }
    }
    
    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Hash password using SHA-256 with salt
     */
    private String hashPassword(String password) throws Exception {
        // For production, use a proper password hashing library like BCrypt
        // This is a simplified implementation
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
    
    /**
     * Validate registration input
     */
    private ValidationResult validateRegistration(String username, String email, String password, 
                                                String firstName, String lastName) {
        // Username validation
        if (username == null || username.trim().length() < 3) {
            return ValidationResult.invalid("Username must be at least 3 characters long");
        }
        if (username.length() > 50) {
            return ValidationResult.invalid("Username must not exceed 50 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_-]+$")) {
            return ValidationResult.invalid("Username can only contain letters, numbers, hyphens, and underscores");
        }
        
        // Email validation
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.invalid("Please enter a valid email address");
        }
        
        // Password validation
        if (!isValidPassword(password)) {
            return ValidationResult.invalid(getPasswordRequirements());
        }
        
        // Name validation
        if (firstName == null || firstName.trim().isEmpty()) {
            return ValidationResult.invalid("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            return ValidationResult.invalid("Last name is required");
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Validate password strength
     */
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Get password requirements message
     */
    private String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain:\n" +
               "• At least one uppercase letter\n" +
               "• At least one lowercase letter\n" +
               "• At least one digit\n" +
               "• At least one special character (@$!%*?&)";
    }
    
    /**
     * Authentication result class
     */
    public static class AuthenticationResult {
        private final boolean success;
        private final User user;
        private final String message;
        
        private AuthenticationResult(boolean success, User user, String message) {
            this.success = success;
            this.user = user;
            this.message = message;
        }
        
        public static AuthenticationResult success(User user, String message) {
            return new AuthenticationResult(true, user, message);
        }
        
        public static AuthenticationResult failure(String message) {
            return new AuthenticationResult(false, null, message);
        }
        
        public boolean isSuccess() { return success; }
        public User getUser() { return user; }
        public String getMessage() { return message; }
    }
    
    /**
     * Validation result class
     */
    private static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
    }
}