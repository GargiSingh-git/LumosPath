package com.lumospath.service;

import com.lumospath.model.MeditationSession;
import com.lumospath.model.MeditationType;
import com.lumospath.model.MeditationSession.MeditationStep;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing meditation sessions, guided content, and breathing exercises
 * Provides comprehensive meditation support for mental wellness
 */
public class MeditationService {
    private final List<MeditationSession> meditationSessions;
    private final Map<MeditationType, List<String>> guidedTexts;
    private final Random random;
    
    public MeditationService() {
        this.meditationSessions = new ArrayList<>();
        this.guidedTexts = new HashMap<>();
        this.random = new Random();
        initializeMeditationSessions();
        initializeGuidedTexts();
    }
    
    /**
     * Initialize all meditation sessions with guided content
     */
    private void initializeMeditationSessions() {
        // 5-Minute Breathing Exercise
        MeditationSession breathing5min = new MeditationSession(
            "Quick Breath Reset", MeditationType.BREATHING, 5,
            "A simple breathing exercise to center yourself and find calm in just 5 minutes"
        );
        breathing5min.setPreparationText("Find a comfortable position, sitting or lying down. Close your eyes gently and let your body relax.");
        breathing5min.setClosingText("Take a moment to notice how you feel. Carry this sense of calm with you into your day.");
        breathing5min.setSteps(Arrays.asList(
            new MeditationStep("Begin by taking a deep breath in through your nose for 4 counts...", 30, "Inhale slowly", true),
            new MeditationStep("Hold your breath gently for 4 counts...", 30, "Hold", true),
            new MeditationStep("Exhale slowly through your mouth for 6 counts...", 30, "Exhale gently", true),
            new MeditationStep("Continue this rhythm: In for 4, hold for 4, out for 6...", 240, "4-4-6 breathing", true),
            new MeditationStep("Let your breathing return to its natural rhythm and simply observe...", 60, "Natural breathing", false)
        ));
        
        // 10-Minute Mindfulness Meditation
        MeditationSession mindfulness10min = new MeditationSession(
            "Present Moment Awareness", MeditationType.MINDFULNESS, 10,
            "Cultivate awareness of the present moment through gentle observation and mindfulness"
        );
        mindfulness10min.setPreparationText("Sit comfortably with your spine straight. Rest your hands gently on your knees or in your lap.");
        mindfulness10min.setClosingText("Notice the peace that comes from simply being present. You can return to this awareness anytime.");
        mindfulness10min.setSteps(Arrays.asList(
            new MeditationStep("Close your eyes and take three deep, cleansing breaths...", 60, "Deep breathing", true),
            new MeditationStep("Notice your breath as it naturally flows in and out...", 120, "Observe breath", false),
            new MeditationStep("When thoughts arise, simply acknowledge them and return to your breath...", 180, "Mindful awareness", false),
            new MeditationStep("Expand your awareness to include sounds around you...", 120, "Sound awareness", false),
            new MeditationStep("Feel the sensations in your body, without trying to change anything...", 120, "Body awareness", false),
            new MeditationStep("Return your full attention to your breath for these final moments...", 120, "Breath focus", false),
            new MeditationStep("Wiggle your fingers and toes, take a deep breath, and when ready, open your eyes...", 60, "Gentle awakening", false)
        ));
        
        // 15-Minute Loving-Kindness Meditation
        MeditationSession lovingKindness15min = new MeditationSession(
            "Heart-Opening Compassion", MeditationType.LOVING_KINDNESS, 15,
            "Cultivate love and compassion for yourself and others through guided loving-kindness practice"
        );
        lovingKindness15min.setPreparationText("Sit comfortably and place your hand over your heart. Feel the warmth and rhythm of your heartbeat.");
        lovingKindness15min.setClosingText("May the love you've cultivated continue to grow and spread to all beings you encounter.");
        lovingKindness15min.setSteps(Arrays.asList(
            new MeditationStep("Begin with yourself: 'May I be happy, may I be healthy, may I be at peace...'", 180, "Self-love", false),
            new MeditationStep("Think of someone you love: 'May you be happy, may you be healthy, may you be at peace...'", 180, "Loved one", false),
            new MeditationStep("Think of a neutral person: 'May you be happy, may you be healthy, may you be at peace...'", 180, "Neutral person", false),
            new MeditationStep("Think of someone difficult: 'May you be happy, may you be healthy, may you be at peace...'", 180, "Difficult person", false),
            new MeditationStep("Extend to all beings: 'May all beings be happy, may all beings be healthy, may all beings be at peace...'", 180, "All beings", false),
            new MeditationStep("Return to yourself with gratitude: 'May I be surrounded by love and kindness...'", 120, "Gratitude", false),
            new MeditationStep("Rest in the warmth of the love you've generated...", 90, "Loving awareness", false)
        ));
        
        // 20-Minute Body Scan
        MeditationSession bodyScan20min = new MeditationSession(
            "Complete Body Relaxation", MeditationType.BODY_SCAN, 20,
            "Progressive relaxation through gentle awareness of each part of your body"
        );
        bodyScan20min.setPreparationText("Lie down comfortably on your back. Let your arms rest by your sides, palms facing up.");
        bodyScan20min.setClosingText("Notice the deep relaxation throughout your entire body. Take this feeling of peace with you.");
        bodyScan20min.setSteps(Arrays.asList(
            new MeditationStep("Start by focusing on your toes. Notice any sensations without trying to change them...", 120, "Toes & feet", false),
            new MeditationStep("Move your attention up to your ankles and calves. Let them relax completely...", 120, "Lower legs", false),
            new MeditationStep("Feel your knees and thighs. Allow any tension to melt away...", 120, "Upper legs", false),
            new MeditationStep("Bring attention to your hips and lower back. Breathe into this area...", 120, "Hips & lower back", false),
            new MeditationStep("Notice your abdomen rising and falling with each breath...", 120, "Abdomen", false),
            new MeditationStep("Feel your chest and heart. Let your shoulders drop and soften...", 120, "Chest & shoulders", false),
            new MeditationStep("Scan your arms from shoulders to fingertips. Let them be completely relaxed...", 120, "Arms & hands", false),
            new MeditationStep("Relax your neck and throat. Let your jaw soften...", 120, "Neck & throat", false),
            new MeditationStep("Notice your face, eyes, and the top of your head. Let everything soften...", 120, "Face & head", false),
            new MeditationStep("Feel your whole body as one unified field of relaxation and peace...", 120, "Whole body", false),
            new MeditationStep("Rest in this state of complete relaxation for a few more moments...", 120, "Deep rest", false)
        ));
        
        // 7-Minute Anxiety Relief
        MeditationSession anxietyRelief7min = new MeditationSession(
            "Calm in the Storm", MeditationType.ANXIETY_RELIEF, 7,
            "Soothing meditation specifically designed to ease anxiety and promote inner calm"
        );
        anxietyRelief7min.setPreparationText("Find a safe, quiet space. You can sit or lie down - whatever feels most comfortable right now.");
        anxietyRelief7min.setClosingText("Remember: you are safe, you are strong, and you have the tools to find calm within yourself.");
        anxietyRelief7min.setSteps(Arrays.asList(
            new MeditationStep("Place one hand on your chest, one on your belly. Feel yourself breathing...", 60, "Grounding touch", false),
            new MeditationStep("Breathe in for 4 counts, hold for 7, exhale for 8. This is the calming breath...", 120, "4-7-8 breathing", true),
            new MeditationStep("Continue this calming breath pattern. Let it slow your heart rate...", 120, "Continued calming", true),
            new MeditationStep("Now breathe naturally. Notice that you are safe in this moment...", 90, "Safety awareness", false),
            new MeditationStep("If worried thoughts arise, imagine them as clouds passing through the sky...", 90, "Thoughts like clouds", false),
            new MeditationStep("Repeat to yourself: 'I am calm, I am safe, I am strong'...", 90, "Affirmations", false),
            new MeditationStep("Take three more deep breaths, feeling more centered with each exhale...", 60, "Final grounding", true)
        ));
        
        // 12-Minute Gratitude Practice
        MeditationSession gratitude12min = new MeditationSession(
            "Heart of Gratitude", MeditationType.GRATITUDE, 12,
            "Cultivate appreciation and joy through mindful gratitude practice"
        );
        gratitude12min.setPreparationText("Sit with your spine tall and your heart open. Place your hands over your heart.");
        gratitude12min.setClosingText("Carry this feeling of gratitude with you throughout your day, noticing all the gifts around you.");
        gratitude12min.setSteps(Arrays.asList(
            new MeditationStep("Begin by feeling grateful for this moment of peace you've created...", 90, "Present moment", false),
            new MeditationStep("Think of three things about your body you're grateful for...", 120, "Body gratitude", false),
            new MeditationStep("Bring to mind someone who has shown you kindness recently...", 120, "People gratitude", false),
            new MeditationStep("Consider something in nature that brings you joy...", 120, "Nature gratitude", false),
            new MeditationStep("Think of a simple pleasure you enjoyed today - perhaps a meal, a smile...", 120, "Simple pleasures", false),
            new MeditationStep("Recall a challenge that helped you grow stronger...", 90, "Growth gratitude", false),
            new MeditationStep("Feel gratitude for your ability to experience all of life's moments...", 90, "Life appreciation", false),
            new MeditationStep("Let this feeling of gratitude fill your entire being...", 90, "Full gratitude", false)
        ));
        
        // Add all sessions to the list
        meditationSessions.addAll(Arrays.asList(
            breathing5min, mindfulness10min, lovingKindness15min, 
            bodyScan20min, anxietyRelief7min, gratitude12min
        ));
    }
    
    /**
     * Initialize guided meditation texts for different types
     */
    private void initializeGuidedTexts() {
        guidedTexts.put(MeditationType.BREATHING, Arrays.asList(
            "Focus on the natural rhythm of your breath. Each inhale brings fresh energy, each exhale releases tension.",
            "Breathe in peace and calm. Breathe out worry and stress. Let your breath be your anchor to the present moment.",
            "Notice how your breath flows effortlessly. Trust in this natural process that sustains and nurtures you.",
            "With each breath, feel yourself becoming more centered and grounded. You are exactly where you need to be."
        ));
        
        guidedTexts.put(MeditationType.MINDFULNESS, Arrays.asList(
            "Simply be present with what is, without trying to change or fix anything. This moment is perfect as it is.",
            "Notice thoughts and feelings as they arise, like clouds passing through the vast sky of your awareness.",
            "You are the peaceful observer of your experience. Rest in this spacious awareness that is always available to you.",
            "Each moment offers a fresh opportunity to return to presence. Be gentle with yourself as you practice."
        ));
        
        guidedTexts.put(MeditationType.ANXIETY_RELIEF, Arrays.asList(
            "You are safe in this moment. Breathe deeply and let your nervous system know that all is well.",
            "Anxiety is temporary, but your inner strength is permanent. Trust in your ability to navigate any storm.",
            "Ground yourself in the present. Feel your feet on the earth, your body supported, your breath flowing.",
            "This feeling will pass. You have survived difficult moments before, and you will find your way through this one too."
        ));
    }
    
    // Service methods
    public List<MeditationSession> getAllSessions() {
        return new ArrayList<>(meditationSessions);
    }
    
    public List<MeditationSession> getSessionsByType(MeditationType type) {
        return meditationSessions.stream()
                .filter(session -> session.getType() == type)
                .collect(Collectors.toList());
    }
    
    public List<MeditationSession> getSessionsByDuration(int maxMinutes) {
        return meditationSessions.stream()
                .filter(session -> session.getDurationMinutes() <= maxMinutes)
                .sorted(Comparator.comparingInt(MeditationSession::getDurationMinutes))
                .collect(Collectors.toList());
    }
    
    public MeditationSession getSessionById(String id) {
        return meditationSessions.stream()
                .filter(session -> id.equals(session.getId()))
                .findFirst()
                .orElse(null);
    }
    
    public MeditationSession getRandomSession() {
        if (meditationSessions.isEmpty()) return null;
        return meditationSessions.get(random.nextInt(meditationSessions.size()));
    }
    
    public MeditationSession getRandomSessionByType(MeditationType type) {
        List<MeditationSession> typeSessions = getSessionsByType(type);
        if (typeSessions.isEmpty()) return null;
        return typeSessions.get(random.nextInt(typeSessions.size()));
    }
    
    public String getRandomGuidedText(MeditationType type) {
        List<String> texts = guidedTexts.get(type);
        if (texts == null || texts.isEmpty()) {
            return "Focus on your breath and be present in this moment.";
        }
        return texts.get(random.nextInt(texts.size()));
    }
    
    public List<String> getQuickBreathingInstructions() {
        return Arrays.asList(
            "Inhale deeply for 4 counts...",
            "Hold your breath for 4 counts...",
            "Exhale slowly for 6 counts...",
            "Repeat this cycle..."
        );
    }
    
    public Map<String, Integer> getBreathingPattern(String patternName) {
        Map<String, Integer> patterns = new HashMap<>();
        switch (patternName.toLowerCase()) {
            case "4-4-6":
                patterns.put("inhale", 4);
                patterns.put("hold", 4);
                patterns.put("exhale", 6);
                break;
            case "4-7-8":
                patterns.put("inhale", 4);
                patterns.put("hold", 7);
                patterns.put("exhale", 8);
                break;
            case "equal":
                patterns.put("inhale", 4);
                patterns.put("hold", 4);
                patterns.put("exhale", 4);
                break;
            default:
                patterns.put("inhale", 4);
                patterns.put("hold", 4);
                patterns.put("exhale", 6);
        }
        return patterns;
    }
    
    /**
     * Create a simple breathing exercise session
     */
    public MeditationSession createBreathingExercise(int durationMinutes) {
        MeditationSession session = new MeditationSession(
            "Custom Breathing Exercise", 
            MeditationType.BREATHING, 
            durationMinutes,
            "A personalized breathing exercise for relaxation and centering"
        );
        
        session.setPreparationText("Find a comfortable position and close your eyes gently.");
        session.setClosingText("Notice the calm you've created within yourself.");
        
        // Create simple breathing steps
        List<MeditationStep> steps = new ArrayList<>();
        int totalSeconds = durationMinutes * 60;
        int stepDuration = Math.max(30, totalSeconds / 4); // Divide into 4 main steps
        
        steps.add(new MeditationStep("Focus on your natural breath...", stepDuration / 2, "Natural breathing", true));
        steps.add(new MeditationStep("Begin the 4-4-6 breathing pattern...", stepDuration, "4-4-6 pattern", true));
        steps.add(new MeditationStep("Continue this calming rhythm...", stepDuration, "Continued rhythm", true));
        steps.add(new MeditationStep("Return to natural breathing...", stepDuration / 2, "Natural completion", true));
        
        session.setSteps(steps);
        return session;
    }
    
    /**
     * Get recommended sessions based on current mood/need
     */
    public List<MeditationSession> getRecommendedSessions(String mood) {
        List<MeditationSession> recommended = new ArrayList<>();
        
        switch (mood.toLowerCase()) {
            case "anxious":
            case "worried":
            case "stressed":
                recommended.addAll(getSessionsByType(MeditationType.ANXIETY_RELIEF));
                recommended.addAll(getSessionsByType(MeditationType.BREATHING));
                break;
            case "sad":
            case "depressed":
            case "lonely":
                recommended.addAll(getSessionsByType(MeditationType.LOVING_KINDNESS));
                recommended.addAll(getSessionsByType(MeditationType.SELF_COMPASSION));
                break;
            case "tired":
            case "sleepy":
                recommended.addAll(getSessionsByType(MeditationType.SLEEP));
                recommended.addAll(getSessionsByType(MeditationType.BODY_SCAN));
                break;
            case "grateful":
            case "thankful":
                recommended.addAll(getSessionsByType(MeditationType.GRATITUDE));
                break;
            default:
                recommended.addAll(getSessionsByType(MeditationType.MINDFULNESS));
                recommended.addAll(getSessionsByType(MeditationType.BREATHING));
        }
        
        return recommended.stream().limit(3).collect(Collectors.toList());
    }
    
    public void completeSession(MeditationSession session) {
        session.incrementCompletionCount();
    }
}