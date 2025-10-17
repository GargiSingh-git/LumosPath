package com.lumospath.model;

import java.time.LocalDateTime;

/**
 * MoodEntry model class for tracking user mood and feelings
 */
public class MoodEntry {
    private int entryId;
    private int userId;
    private MoodType moodType;
    private int moodScale; // 1-10 scale
    private String description;
    private String trigger; // What caused this mood
    private LocalDateTime createdAt;
    private String tags; // Comma-separated tags like "stress,work,family"

    // Constructors
    public MoodEntry() {
        this.createdAt = LocalDateTime.now();
    }

    public MoodEntry(int userId, MoodType moodType, int moodScale, String description) {
        this();
        this.userId = userId;
        this.moodType = moodType;
        this.moodScale = Math.max(1, Math.min(10, moodScale)); // Ensure scale is 1-10
        this.description = description;
    }

    // Getters and Setters
    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MoodType getMoodType() {
        return moodType;
    }

    public void setMoodType(MoodType moodType) {
        this.moodType = moodType;
    }

    public int getMoodScale() {
        return moodScale;
    }

    public void setMoodScale(int moodScale) {
        this.moodScale = Math.max(1, Math.min(10, moodScale));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "MoodEntry{" +
                "entryId=" + entryId +
                ", userId=" + userId +
                ", moodType=" + moodType +
                ", moodScale=" + moodScale +
                ", description='" + description + '\'' +
                ", trigger='" + trigger + '\'' +
                ", createdAt=" + createdAt +
                ", tags='" + tags + '\'' +
                '}';
    }
}