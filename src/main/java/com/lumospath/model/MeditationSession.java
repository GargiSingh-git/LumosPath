package com.lumospath.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a meditation session with guided instructions and timing
 * Contains all information needed for a complete meditation experience
 */
public class MeditationSession {
    private String id;
    private String title;
    private MeditationType type;
    private int durationMinutes;
    private String description;
    private String instructor; // e.g., "LumosPath Guide"
    private List<MeditationStep> steps;
    private String backgroundMusic;
    private String preparationText;
    private String closingText;
    private LocalDateTime createdAt;
    private int completionCount;
    private double averageRating;
    
    // Inner class for meditation steps/instructions
    public static class MeditationStep {
        private String instruction;
        private int durationSeconds;
        private String visualCue;
        private boolean isBreathingStep;
        
        public MeditationStep(String instruction, int durationSeconds) {
            this.instruction = instruction;
            this.durationSeconds = durationSeconds;
            this.isBreathingStep = false;
        }
        
        public MeditationStep(String instruction, int durationSeconds, String visualCue, boolean isBreathingStep) {
            this.instruction = instruction;
            this.durationSeconds = durationSeconds;
            this.visualCue = visualCue;
            this.isBreathingStep = isBreathingStep;
        }
        
        // Getters
        public String getInstruction() { return instruction; }
        public int getDurationSeconds() { return durationSeconds; }
        public String getVisualCue() { return visualCue; }
        public boolean isBreathingStep() { return isBreathingStep; }
        
        // Setters
        public void setInstruction(String instruction) { this.instruction = instruction; }
        public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
        public void setVisualCue(String visualCue) { this.visualCue = visualCue; }
        public void setBreathingStep(boolean breathingStep) { this.isBreathingStep = breathingStep; }
    }
    
    // Constructors
    public MeditationSession() {
        this.createdAt = LocalDateTime.now();
        this.completionCount = 0;
        this.averageRating = 0.0;
    }
    
    public MeditationSession(String title, MeditationType type, int durationMinutes, String description) {
        this();
        this.title = title;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.description = description;
        this.instructor = "LumosPath Guide";
    }
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public MeditationType getType() { return type; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getDescription() { return description; }
    public String getInstructor() { return instructor; }
    public List<MeditationStep> getSteps() { return steps; }
    public String getBackgroundMusic() { return backgroundMusic; }
    public String getPreparationText() { return preparationText; }
    public String getClosingText() { return closingText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getCompletionCount() { return completionCount; }
    public double getAverageRating() { return averageRating; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setType(MeditationType type) { this.type = type; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setDescription(String description) { this.description = description; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setSteps(List<MeditationStep> steps) { this.steps = steps; }
    public void setBackgroundMusic(String backgroundMusic) { this.backgroundMusic = backgroundMusic; }
    public void setPreparationText(String preparationText) { this.preparationText = preparationText; }
    public void setClosingText(String closingText) { this.closingText = closingText; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setCompletionCount(int completionCount) { this.completionCount = completionCount; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    
    // Utility methods
    public void incrementCompletionCount() {
        this.completionCount++;
    }
    
    public String getFormattedDuration() {
        if (durationMinutes < 60) {
            return durationMinutes + " min";
        } else {
            int hours = durationMinutes / 60;
            int mins = durationMinutes % 60;
            return hours + "h " + (mins > 0 ? mins + "m" : "");
        }
    }
    
    public int getTotalStepsDuration() {
        return steps != null ? steps.stream().mapToInt(MeditationStep::getDurationSeconds).sum() : 0;
    }
    
    @Override
    public String toString() {
        return type.getEmoji() + " " + title + " (" + getFormattedDuration() + ")";
    }
}