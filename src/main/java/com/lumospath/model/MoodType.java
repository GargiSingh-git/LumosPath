package com.lumospath.model;

/**
 * Enum representing different types of moods/feelings
 */
public enum MoodType {
    VERY_HAPPY("Very Happy", "😊", "Feeling fantastic and joyful"),
    HAPPY("Happy", "🙂", "Feeling good and content"),
    NEUTRAL("Neutral", "😐", "Feeling okay, neither good nor bad"),
    ANXIOUS("Anxious", "😰", "Feeling worried and restless"),
    SAD("Sad", "😢", "Feeling down and melancholy"),
    DEPRESSED("Depressed", "😞", "Feeling very low and hopeless"),
    ANGRY("Angry", "😠", "Feeling frustrated and upset"),
    STRESSED("Stressed", "😵", "Feeling overwhelmed and pressured"),
    CONFUSED("Confused", "😕", "Feeling uncertain and lost"),
    HOPEFUL("Hopeful", "🌟", "Feeling optimistic about the future");

    private final String displayName;
    private final String emoji;
    private final String description;

    MoodType(String displayName, String emoji, String description) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get a mood type based on a mood scale (1-10)
     * @param scale The mood scale (1 = very depressed, 10 = very happy)
     * @return Corresponding MoodType
     */
    public static MoodType fromScale(int scale) {
        switch (scale) {
            case 1:
            case 2:
                return DEPRESSED;
            case 3:
            case 4:
                return SAD;
            case 5:
                return NEUTRAL;
            case 6:
            case 7:
                return HAPPY;
            case 8:
            case 9:
            case 10:
                return VERY_HAPPY;
            default:
                return NEUTRAL;
        }
    }

    /**
     * Check if this mood type indicates a low/concerning mood
     * @return true if the mood is concerning (sad, depressed, angry, etc.)
     */
    public boolean isConcerning() {
        return this == DEPRESSED || this == SAD || this == ANGRY || 
               this == STRESSED || this == ANXIOUS;
    }

    @Override
    public String toString() {
        return emoji + " " + displayName;
    }
}