package com.lumospath.util;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

/**
 * Enhanced database utility class with connection pooling and comprehensive JDBC operations
 * Supports SQLite (default), MySQL, and PostgreSQL databases
 */
public class DatabaseUtil {
    private static final String DEFAULT_DB_URL = "jdbc:h2:file:./lumospath;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Database configuration
    private static String dbUrl = DEFAULT_DB_URL;
    private static String dbUser = "sa";
    private static String dbPassword = "";
    private static String dbDriver = "org.h2.Driver";
    
    // Connection pool simulation (for production, use HikariCP or similar)
    private static final Map<Thread, Connection> connectionPool = new ConcurrentHashMap<>();
    private static final int MAX_CONNECTIONS = 10;
    
    // Database type enum
    public enum DatabaseType {
        H2("jdbc:h2:", "org.h2.Driver"),
        SQLITE("jdbc:sqlite:", "org.sqlite.JDBC"),
        MYSQL("jdbc:mysql://", "com.mysql.cj.jdbc.Driver"),
        POSTGRESQL("jdbc:postgresql://", "org.postgresql.Driver");
        
        private final String urlPrefix;
        private final String driverClass;
        
        DatabaseType(String urlPrefix, String driverClass) {
            this.urlPrefix = urlPrefix;
            this.driverClass = driverClass;
        }
        
        public String getUrlPrefix() { return urlPrefix; }
        public String getDriverClass() { return driverClass; }
    }
    
    static {
        loadDatabaseConfig();
        initializeDriver();
    }

    /**
     * Load database configuration from properties file or environment variables
     */
    private static void loadDatabaseConfig() {
        try {
            Properties props = new Properties();
            InputStream input = DatabaseUtil.class.getResourceAsStream("/database.properties");
            
            if (input != null) {
                props.load(input);
                dbUrl = props.getProperty("db.url", DEFAULT_DB_URL);
                dbUser = props.getProperty("db.username", "sa");
                dbPassword = props.getProperty("db.password", "");
                dbDriver = props.getProperty("db.driver", "org.h2.Driver");
                input.close();
            }
            
            // Override with environment variables if available
            dbUrl = System.getenv("LUMOS_DB_URL") != null ? System.getenv("LUMOS_DB_URL") : dbUrl;
            dbUser = System.getenv("LUMOS_DB_USER") != null ? System.getenv("LUMOS_DB_USER") : dbUser;
            dbPassword = System.getenv("LUMOS_DB_PASS") != null ? System.getenv("LUMOS_DB_PASS") : dbPassword;
            
        } catch (IOException e) {
            System.out.println("Using default database configuration: " + e.getMessage());
        }
    }
    
    /**
     * Initialize database driver
     */
    private static void initializeDriver() {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found: " + dbDriver, e);
        }
    }
    
    /**
     * Get database connection with enhanced error handling
     */
    public static Connection getConnection() throws SQLException {
        return getConnection(false);
    }
    
    /**
     * Get database connection with option for pooling
     */
    public static Connection getConnection(boolean usePooling) throws SQLException {
        if (usePooling) {
            Thread currentThread = Thread.currentThread();
            Connection pooledConnection = connectionPool.get(currentThread);
            
            if (pooledConnection != null && !pooledConnection.isClosed()) {
                return pooledConnection;
            }
            
            if (connectionPool.size() < MAX_CONNECTIONS) {
                Connection newConnection = createConnection();
                connectionPool.put(currentThread, newConnection);
                return newConnection;
            }
        }
        
        return createConnection();
    }
    
    /**
     * Create a new database connection
     */
    private static Connection createConnection() throws SQLException {
        Connection conn;
        
        // Always use user/password for consistency
        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", dbPassword);
        
        // Additional properties for different databases
        if (dbUrl.startsWith("jdbc:mysql:")) {
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
        } else if (dbUrl.startsWith("jdbc:h2:")) {
            // H2 specific properties
            props.setProperty("DB_CLOSE_ON_EXIT", "FALSE");
            props.setProperty("DB_CLOSE_DELAY", "-1");
        }
        
        conn = DriverManager.getConnection(dbUrl, props);
        
        // Enable foreign key constraints for SQLite
        if (dbUrl.startsWith("jdbc:sqlite:")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
        }
        
        return conn;
    }

    /**
     * Initialize database tables
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            createTables(conn);
            System.out.println("✅ Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Create necessary tables
     */
    private static void createTables(Connection conn) throws SQLException {
        // Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(255) UNIQUE,
                email VARCHAR(255) UNIQUE,
                password_hash VARCHAR(512),
                first_name VARCHAR(255),
                last_name VARCHAR(255),
                age INTEGER,
                location VARCHAR(255),
                created_at TIMESTAMP NOT NULL,
                last_active TIMESTAMP NOT NULL,
                anonymous BOOLEAN DEFAULT FALSE
            )
        """;
        
        // User authentication history table
        String createUserAuthHistoryTable = """
            CREATE TABLE IF NOT EXISTS user_auth_history (
                auth_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_id INTEGER NOT NULL,
                login_time TIMESTAMP NOT NULL,
                logout_time TIMESTAMP,
                ip_address VARCHAR(255),
                user_agent TEXT,
                session_duration_minutes INTEGER,
                FOREIGN KEY (user_id) REFERENCES users (user_id)
            )
        """;

        // Mood entries table
        String createMoodEntriesTable = """
            CREATE TABLE IF NOT EXISTS mood_entries (
                entry_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_id INTEGER NOT NULL,
                mood_type VARCHAR(255) NOT NULL,
                mood_scale INTEGER NOT NULL,
                description TEXT,
                trigger_cause TEXT,
                tags TEXT,
                created_at TIMESTAMP NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (user_id)
            )
        """;

        // Motivational quotes table
        String createQuotesTable = """
            CREATE TABLE IF NOT EXISTS motivational_quotes (
                quote_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                quote_text TEXT NOT NULL,
                author VARCHAR(255) NOT NULL,
                source VARCHAR(255) NOT NULL,
                category VARCHAR(255) NOT NULL,
                language VARCHAR(10) DEFAULT 'en'
            )
        """;

        // Emergency contacts table
        String createContactsTable = """
            CREATE TABLE IF NOT EXISTS emergency_contacts (
                contact_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                phone_number VARCHAR(50) NOT NULL,
                location VARCHAR(255) NOT NULL,
                contact_type VARCHAR(100) NOT NULL,
                description TEXT,
                available_24x7 BOOLEAN DEFAULT FALSE,
                website VARCHAR(500)
            )
        """;

        // Chat logs table (for chatbot interactions)
        String createChatLogsTable = """
            CREATE TABLE IF NOT EXISTS chat_logs (
                log_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_id INTEGER,
                user_message TEXT NOT NULL,
                bot_response TEXT NOT NULL,
                sentiment_score DOUBLE,
                detected_emotion VARCHAR(100),
                created_at TIMESTAMP NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (user_id)
            )
        """;
        
        // Meditation sessions table
        String createMeditationSessionsTable = """
            CREATE TABLE IF NOT EXISTS meditation_sessions (
                session_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                title VARCHAR(255) NOT NULL,
                meditation_type VARCHAR(100) NOT NULL,
                duration_minutes INTEGER NOT NULL,
                description TEXT,
                instructor VARCHAR(255) DEFAULT 'LumosPath Guide',
                preparation_text TEXT,
                closing_text TEXT,
                background_music VARCHAR(255),
                completion_count INTEGER DEFAULT 0,
                average_rating DOUBLE DEFAULT 0.0,
                created_at TIMESTAMP NOT NULL
            )
        """;
        
        // Meditation steps table
        String createMeditationStepsTable = """
            CREATE TABLE IF NOT EXISTS meditation_steps (
                step_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                session_id INTEGER NOT NULL,
                step_order INTEGER NOT NULL,
                instruction TEXT NOT NULL,
                duration_seconds INTEGER NOT NULL,
                visual_cue VARCHAR(255),
                is_breathing_step BOOLEAN DEFAULT FALSE,
                FOREIGN KEY (session_id) REFERENCES meditation_sessions (session_id)
            )
        """;
        
        // User meditation progress table
        String createUserMeditationProgressTable = """
            CREATE TABLE IF NOT EXISTS user_meditation_progress (
                progress_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_id INTEGER NOT NULL,
                session_id INTEGER NOT NULL,
                completed_at TIMESTAMP NOT NULL,
                duration_completed INTEGER NOT NULL,
                rating INTEGER,
                notes TEXT,
                FOREIGN KEY (user_id) REFERENCES users (user_id),
                FOREIGN KEY (session_id) REFERENCES meditation_sessions (session_id)
            )
        """;
        
        // User preferences table
        String createUserPreferencesTable = """
            CREATE TABLE IF NOT EXISTS user_preferences (
                preference_id INTEGER PRIMARY KEY AUTO_INCREMENT,
                user_id INTEGER NOT NULL,
                preference_key VARCHAR(255) NOT NULL,
                preference_value TEXT NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users (user_id),
                UNIQUE(user_id, preference_key)
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createUserAuthHistoryTable);
            stmt.execute(createMoodEntriesTable);
            stmt.execute(createQuotesTable);
            stmt.execute(createContactsTable);
            stmt.execute(createChatLogsTable);
            stmt.execute(createMeditationSessionsTable);
            stmt.execute(createMeditationStepsTable);
            stmt.execute(createUserMeditationProgressTable);
            stmt.execute(createUserPreferencesTable);
            
            // Create indexes for better performance
            createIndexes(stmt);
            
            // Insert default data if tables are empty
            insertDefaultData(conn);
        }
    }
    
    /**
     * Create database indexes for better performance
     */
    private static void createIndexes(Statement stmt) throws SQLException {
        String[] indexes = {
            "CREATE INDEX IF NOT EXISTS idx_users_username ON users(username)",
            "CREATE INDEX IF NOT EXISTS idx_users_email ON users(email)",
            "CREATE INDEX IF NOT EXISTS idx_user_auth_history_user_id ON user_auth_history(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_user_auth_history_login_time ON user_auth_history(login_time)",
            "CREATE INDEX IF NOT EXISTS idx_mood_entries_user_id ON mood_entries(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_mood_entries_created_at ON mood_entries(created_at)",
            "CREATE INDEX IF NOT EXISTS idx_chat_logs_user_id ON chat_logs(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_chat_logs_created_at ON chat_logs(created_at)",
            "CREATE INDEX IF NOT EXISTS idx_meditation_steps_session_id ON meditation_steps(session_id)",
            "CREATE INDEX IF NOT EXISTS idx_user_meditation_progress_user_id ON user_meditation_progress(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON user_preferences(user_id)",
            "CREATE INDEX IF NOT EXISTS idx_quotes_category ON motivational_quotes(category)"
        };
        
        for (String index : indexes) {
            try {
                stmt.execute(index);
            } catch (SQLException e) {
                // Index might already exist, continue
                System.out.println("Index creation note: " + e.getMessage());
            }
        }
    }
    
    /**
     * Insert default data if tables are empty
     */
    private static void insertDefaultData(Connection conn) throws SQLException {
        insertDefaultQuotes(conn);
        insertDefaultEmergencyContacts(conn);
    }
    
    /**
     * Insert default motivational quotes
     */
    private static void insertDefaultQuotes(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM motivational_quotes";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO motivational_quotes (quote_text, author, source, category) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    // Add some default quotes
                    Object[][] quotes = {
                        {"You have the right to perform your actions, but you are not entitled to the fruits of action.", "Lord Krishna", "Bhagavad Gita", "wisdom"},
                        {"When meditation is mastered, the mind is unwavering like the flame of a candle in a windless place.", "Lord Krishna", "Bhagavad Gita", "meditation"},
                        {"The mind is everything. What you think you become.", "Buddha", "Buddhist Teachings", "mindfulness"},
                        {"Peace comes from within. Do not seek it without.", "Buddha", "Buddhist Teachings", "inner_peace"},
                        {"You are braver than you believe, stronger than you seem, and smarter than you think.", "A.A. Milne", "Winnie the Pooh", "encouragement"}
                    };
                    
                    for (Object[] quote : quotes) {
                        pstmt.setString(1, (String) quote[0]);
                        pstmt.setString(2, (String) quote[1]);
                        pstmt.setString(3, (String) quote[2]);
                        pstmt.setString(4, (String) quote[3]);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }
        }
    }
    
    /**
     * Insert default emergency contacts
     */
    private static void insertDefaultEmergencyContacts(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM emergency_contacts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO emergency_contacts (name, phone_number, location, contact_type, description, available_24x7) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    // Add some default emergency contacts
                    Object[][] contacts = {
                        {"National Suicide Prevention Lifeline", "9152987821", "India", "Crisis", "24/7 emotional support and suicide prevention", true},
                        {"Vandrevala Foundation", "9999666555", "India", "Mental Health", "Free counseling and mental health support", true},
                        {"AASRA", "9820466726", "Mumbai", "Crisis", "Suicide prevention and emotional support", true},
                        {"Sneha Foundation", "044-24640050", "Chennai", "Crisis", "24x7 suicide prevention helpline", true},
                        {"Fortis Stress Helpline", "8376804102", "India", "Stress", "Stress management and counseling", false}
                    };
                    
                    for (Object[] contact : contacts) {
                        pstmt.setString(1, (String) contact[0]);
                        pstmt.setString(2, (String) contact[1]);
                        pstmt.setString(3, (String) contact[2]);
                        pstmt.setString(4, (String) contact[3]);
                        pstmt.setString(5, (String) contact[4]);
                        pstmt.setBoolean(6, (Boolean) contact[5]);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }
        }
    }

    /**
     * Convert LocalDateTime to database string format
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }

    /**
     * Parse database string to LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_FORMATTER);
    }

    /**
     * Check if database connection is working
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close database resources safely
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }

    /**
     * Execute a simple query for testing
     */
    public static void testQuery() {
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("📋 Database tables:");
            while (rs.next()) {
                System.out.println("  • " + rs.getString("name"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing test query: " + e.getMessage());
        }
    }

    /**
     * Transaction management interface
     */
    @FunctionalInterface
    public interface TransactionCallback<T> {
        T execute(Connection conn) throws SQLException;
    }
    
    /**
     * Execute operations within a transaction
     */
    public static <T> T executeInTransaction(TransactionCallback<T> callback) throws SQLException {
        Connection conn = null;
        boolean originalAutoCommit = true;
        
        try {
            conn = getConnection();
            originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            
            T result = callback.execute(conn);
            conn.commit();
            return result;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommit);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Execute batch operations
     */
    public static int[] executeBatch(String sql, Object[][] batchData) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Object[] row : batchData) {
                for (int i = 0; i < row.length; i++) {
                    pstmt.setObject(i + 1, row[i]);
                }
                pstmt.addBatch();
            }
            
            return pstmt.executeBatch();
        }
    }
    
    /**
     * Execute a prepared statement with parameters
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            return pstmt.executeUpdate();
        }
    }
    
    /**
     * Execute a query with parameters and return ResultSet handler
     */
    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
    
    public static <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return handler.handle(rs);
            }
        }
    }
    
    /**
     * Get database metadata information
     */
    public static void printDatabaseInfo() {
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            System.out.println("=== Database Information ===");
            System.out.println("Database Product: " + metaData.getDatabaseProductName());
            System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + metaData.getDriverName());
            System.out.println("Driver Version: " + metaData.getDriverVersion());
            System.out.println("URL: " + dbUrl);
            System.out.println("Max Connections: " + metaData.getMaxConnections());
            System.out.println("=============================");
            
        } catch (SQLException e) {
            System.err.println("Error getting database info: " + e.getMessage());
        }
    }
    
    /**
     * Get table row counts for monitoring
     */
    public static void printTableStats() {
        String[] tables = {"users", "mood_entries", "motivational_quotes", "emergency_contacts", 
                          "chat_logs", "meditation_sessions", "user_meditation_progress"};
        
        System.out.println("=== Table Statistics ===");
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (String table : tables) {
                try {
                    String sql = "SELECT COUNT(*) as count FROM " + table;
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        System.out.println(String.format("%-25s: %d records", table, rs.getInt("count")));
                    }
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(String.format("%-25s: Error - %s", table, e.getMessage()));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting table statistics: " + e.getMessage());
        }
        
        System.out.println("=========================");
    }
    
    /**
     * Clean up connection pool
     */
    public static void closeAllConnections() {
        connectionPool.forEach((thread, connection) -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing pooled connection: " + e.getMessage());
            }
        });
        connectionPool.clear();
    }
    
    /**
     * Get user count for testing
     */
    public static int getUserCount() {
        try {
            return executeQuery("SELECT COUNT(*) as count FROM users", rs -> {
                if (rs.next()) {
                    return rs.getInt("count");
                }
                return 0;
            });
        } catch (SQLException e) {
            System.err.println("Error getting user count: " + e.getMessage());
            return 0;
        }
    }
}