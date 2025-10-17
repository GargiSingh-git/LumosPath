package com.lumospath.service;

import com.lumospath.dao.impl.MoodEntryDAOImpl;
import com.lumospath.dao.MoodEntryDAO;
import com.lumospath.model.MoodEntry;
import com.lumospath.model.MoodType;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for handling mood tracking functionality with database persistence
 */
public class MoodTrackingService {
    private final MoodEntryDAO moodEntryDAO;
    private Scanner scanner;

    public MoodTrackingService() {
        this.moodEntryDAO = new MoodEntryDAOImpl();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Interactive method for user to enter their mood
     * @param userId The user ID
     * @return The created mood entry
     */
    public MoodEntry recordMood(int userId) {
        System.out.println("\n=== How are you feeling today? ===");
        
        // Display mood options
        System.out.println("\nChoose how you're feeling:");
        MoodType[] moods = MoodType.values();
        for (int i = 0; i < moods.length; i++) {
            System.out.println((i + 1) + ". " + moods[i]);
        }

        // Get mood choice
        MoodType selectedMood = null;
        while (selectedMood == null) {
            System.out.print("\nEnter your choice (1-" + moods.length + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= moods.length) {
                    selectedMood = moods[choice - 1];
                } else {
                    System.out.println("Please enter a number between 1 and " + moods.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        // Get mood scale (1-10)
        int moodScale = 0;
        while (moodScale < 1 || moodScale > 10) {
            System.out.print("\nOn a scale of 1-10, how intense is this feeling? (1 = very low, 10 = very high): ");
            try {
                moodScale = Integer.parseInt(scanner.nextLine());
                if (moodScale < 1 || moodScale > 10) {
                    System.out.println("Please enter a number between 1 and 10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        // Get description
        System.out.print("\nCan you describe what you're feeling? (optional): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = "No description provided";
        }

        // Get trigger (optional)
        System.out.print("What triggered this feeling? (optional): ");
        String trigger = scanner.nextLine().trim();

        // Create mood entry
        MoodEntry entry = new MoodEntry(userId, selectedMood, moodScale, description);
        entry.setTrigger(trigger.isEmpty() ? null : trigger);
        
        // Store the entry in database (only if not anonymous user)
        try {
            if (userId > 0) { // Only save for registered users
                entry = moodEntryDAO.save(entry);
                System.out.println("\n✅ Your mood has been recorded and saved!");
            } else {
                System.out.println("\n✅ Your mood has been recorded (anonymous session)!");
            }
        } catch (SQLException e) {
            System.err.println("Error saving mood entry: " + e.getMessage());
            System.out.println("\n✅ Your mood has been recorded for this session!");
        }
        
        // Provide immediate support if mood is concerning
        if (selectedMood.isConcerning() || moodScale <= 4) {
            provideConcernResponse(selectedMood, moodScale);
        }

        return entry;
    }

    /**
     * Provide supportive response for concerning moods
     */
    private void provideConcernResponse(MoodType moodType, int scale) {
        System.out.println("\n💙 I understand you're going through a difficult time.");
        
        if (scale <= 2) {
            System.out.println("⚠️  You seem to be feeling very low. Please consider:");
            System.out.println("   • Reaching out to a trusted friend or family member");
            System.out.println("   • Calling a mental health helpline (use option 4 from main menu)");
            System.out.println("   • Consulting with a mental health professional");
        }

        System.out.println("\n🌟 Remember:");
        switch (moodType) {
            case DEPRESSED:
            case SAD:
                System.out.println("   • This feeling is temporary and will pass");
                System.out.println("   • You are not alone in this journey");
                System.out.println("   • Small steps forward are still progress");
                break;
            case ANXIOUS:
            case STRESSED:
                System.out.println("   • Take deep breaths and focus on the present moment");
                System.out.println("   • Break down overwhelming tasks into smaller steps");
                System.out.println("   • It's okay to ask for help");
                break;
            case ANGRY:
                System.out.println("   • It's normal to feel angry sometimes");
                System.out.println("   • Try to identify what's really bothering you");
                System.out.println("   • Physical activity can help release tension");
                break;
            default:
                System.out.println("   • Your feelings are valid and important");
                System.out.println("   • Consider what small action might help you feel better");
        }
    }

    /**
     * Display recent mood entries for a user
     */
    public void showMoodHistory(int userId) {
        System.out.println("\n=== Your Recent Mood History ===");
        
        List<MoodEntry> userEntries;
        try {
            if (userId > 0) {
                userEntries = moodEntryDAO.findRecentByUserId(userId, 5);
            } else {
                userEntries = new ArrayList<>(); // Anonymous users have no persistent history
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving mood history: " + e.getMessage());
            userEntries = new ArrayList<>();
        }

        if (userEntries.isEmpty()) {
            if (userId > 0) {
                System.out.println("No mood entries recorded yet. Start tracking your mood to see your progress!");
            } else {
                System.out.println("Anonymous users don't have persistent mood history. Sign up to track your progress!");
            }
            return;
        }

        for (MoodEntry entry : userEntries) {
            System.out.println("\n📅 " + entry.getCreatedAt().toLocalDate() + 
                             " at " + entry.getCreatedAt().toLocalTime().toString().substring(0, 5));
            System.out.println("   Mood: " + entry.getMoodType() + " (Intensity: " + entry.getMoodScale() + "/10)");
            System.out.println("   Description: " + entry.getDescription());
            if (entry.getTrigger() != null && !entry.getTrigger().isEmpty()) {
                System.out.println("   Trigger: " + entry.getTrigger());
            }
        }

        // Show mood trend
        if (userEntries.size() >= 2) {
            MoodEntry latest = userEntries.get(0);
            MoodEntry previous = userEntries.get(1);
            
            System.out.println("\n📊 Mood Trend:");
            if (latest.getMoodScale() > previous.getMoodScale()) {
                System.out.println("   📈 Your mood is improving! Keep it up!");
            } else if (latest.getMoodScale() < previous.getMoodScale()) {
                System.out.println("   📉 You're having some challenges. Remember, it's okay to have ups and downs.");
            } else {
                System.out.println("   ➡️  Your mood has been stable.");
            }
        }
    }

    /**
     * Get all mood entries for a user
     */
    public List<MoodEntry> getUserMoodEntries(int userId) {
        try {
            if (userId > 0) {
                return moodEntryDAO.findByUserId(userId);
            } else {
                return new ArrayList<>(); // Anonymous users have no persistent history
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user mood entries: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get average mood score for a user
     */
    public double getAverageMoodScore(int userId) {
        List<MoodEntry> userEntries = getUserMoodEntries(userId);
        if (userEntries.isEmpty()) {
            return 0.0;
        }
        
        return userEntries.stream()
                .mapToInt(MoodEntry::getMoodScale)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Get mood statistics for a user over a period
     */
    public List<Object[]> getMoodStatistics(int userId, int days) {
        try {
            if (userId > 0) {
                return moodEntryDAO.getMoodStatistics(userId, days);
            } else {
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving mood statistics: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get mood trend data for visualization
     */
    public List<Object[]> getMoodTrendData(int userId, int days) {
        try {
            if (userId > 0) {
                return moodEntryDAO.getMoodTrendData(userId, days);
            } else {
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving mood trend data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get average mood scale for a date range
     */
    public double getAverageMoodScale(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (userId > 0) {
                return moodEntryDAO.getAverageMoodScale(userId, startDate, endDate);
            } else {
                return 0.0;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving average mood scale: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Count total mood entries for a user
     */
    public long getMoodEntryCount(int userId) {
        try {
            if (userId > 0) {
                return moodEntryDAO.countByUserId(userId);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error counting mood entries: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Delete all mood history for a user
     */
    public boolean deleteUserMoodHistory(int userId) {
        try {
            if (userId > 0) {
                MoodEntryDAOImpl moodDAO = (MoodEntryDAOImpl) moodEntryDAO;
                int deletedCount = moodDAO.deleteAllByUserId(userId);
                return deletedCount > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error deleting mood history: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save a mood entry for GUI usage
     */
    public MoodEntry saveMoodEntry(MoodEntry moodEntry) throws SQLException {
        // Don't save for anonymous users
        if (moodEntry.getUserId() <= 0) {
            return moodEntry; // Return without saving
        }
        
        return moodEntryDAO.save(moodEntry);
    }
}
