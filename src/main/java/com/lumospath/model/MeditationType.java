package com.lumospath.model;

/**
 * Enumeration of different meditation types available in LumosPath
 * Each type focuses on different aspects of mental wellness and mindfulness
 */
public enum MeditationType {
    BREATHING("Breathing Exercise", "Focus on breath control and awareness", "🌬️"),
    MINDFULNESS("Mindfulness Meditation", "Present moment awareness and observation", "🧘‍♀️"),
    LOVING_KINDNESS("Loving-Kindness Meditation", "Cultivate compassion and love for self and others", "💕"),
    BODY_SCAN("Body Scan Meditation", "Progressive relaxation and body awareness", "🌸"),
    ANXIETY_RELIEF("Anxiety Relief", "Calming meditation specifically for anxiety", "🕊️"),
    SLEEP("Sleep Meditation", "Guided relaxation for better sleep", "🌙"),
    GRATITUDE("Gratitude Practice", "Focus on appreciation and thankfulness", "🙏"),
    STRESS_RELIEF("Stress Relief", "Release tension and promote relaxation", "🌊"),
    SELF_COMPASSION("Self-Compassion", "Develop kindness and understanding toward yourself", "🤗"),
    FOCUS("Focus & Concentration", "Improve attention and mental clarity", "🎯");

    private final String displayName;
    private final String description;
    private final String emoji;

    MeditationType(String displayName, String description, String emoji) {
        this.displayName = displayName;
        this.description = description;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return emoji + " " + displayName;
    }
}