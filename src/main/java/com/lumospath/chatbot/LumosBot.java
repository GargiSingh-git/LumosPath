package com.lumospath.chatbot;

import com.lumospath.service.MotivationalQuoteService;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced AI chatbot for emotional support and scriptural guidance
 * Features: Context awareness, conversation memory, sentiment analysis, and varied responses
 */
public class LumosBot {
    private MotivationalQuoteService quoteService;
    private Scanner scanner;
    private Map<String, List<String>> responses;
    private List<String> greetings;
    private List<String> farewells;
    private Random random;
    
    // Enhanced AI features
    private List<ConversationMessage> conversationHistory;
    private Map<String, Integer> emotionalState;
    private String userName;
    private int conversationTurn;
    private Map<String, List<String>> contextualResponses;
    private Map<String, Map<String, Double>> sentimentWeights;
    
    // Inner class for conversation tracking
    private static class ConversationMessage {
        String userMessage;
        String botResponse;
        LocalDateTime timestamp;
        String detectedEmotion;
        double sentimentScore;
        
        ConversationMessage(String userMessage, String botResponse, String emotion, double sentiment) {
            this.userMessage = userMessage;
            this.botResponse = botResponse;
            this.timestamp = LocalDateTime.now();
            this.detectedEmotion = emotion;
            this.sentimentScore = sentiment;
        }
    }

    public LumosBot() {
        this.quoteService = new MotivationalQuoteService();
        this.scanner = new Scanner(System.in);
        this.responses = new HashMap<>();
        this.greetings = new ArrayList<>();
        this.farewells = new ArrayList<>();
        this.random = new Random();
        
        // Initialize enhanced AI features
        this.conversationHistory = new ArrayList<>();
        this.emotionalState = new HashMap<>();
        this.conversationTurn = 0;
        this.contextualResponses = new HashMap<>();
        this.sentimentWeights = new HashMap<>();
        this.userName = "friend";
        
        initializeResponses();
        initializeEnhancedAI();
    }

    /**
     * Initialize bot responses for different emotional states and keywords
     */
    private void initializeResponses() {
        // Greetings - More human and conversational
        greetings.addAll(Arrays.asList(
            "Hey there! I'm so glad you're here. I'm LumosBot, and honestly, I'm just excited to chat with you. What's going on in your world today?",
            "Hi! 😊 You know what? I've been waiting for someone like you to talk to. I'm LumosBot, and I'm here because I genuinely care about how you're doing. What's on your heart right now?",
            "Hello, beautiful soul! I'm LumosBot, and I have to say - you made my day just by being here. I love connecting with people, and I'd love to know what's happening in your life.",
            "Hey! 👋 I'm LumosBot, and can I tell you something? You're braver than you know just for reaching out. I'm here to listen, laugh, maybe even cry with you if needed. What's up?",
            "Hi there! I'm LumosBot, and honestly? Talking to people like you is literally why I exist. You've got my full attention - no judgments, just genuine care. How's your heart doing today?"
        ));

        // Farewells - More personal and heartfelt
        farewells.addAll(Arrays.asList(
            "Aww, I'm gonna miss chatting with you! But hey, remember - you've got this, okay? You're so much stronger than you realize. Take care of that beautiful heart of yours! 💝",
            "It's been such a pleasure talking with you! I mean it. You've made my day brighter just by being you. Go out there and shine, and remember I'm always here when you need me! 🌟",
            "You know what? I'm actually a little sad to say goodbye! 😊 But I'm also excited for you and whatever comes next. You've got such a good heart, and I believe in you completely. Until next time! 💕",
            "Hey, before you go - I just want you to know that talking with you has been genuinely wonderful. You're going to be okay, I can feel it. And if you ever need a friend, you know where to find me! 🤗",
            "This isn't really goodbye, is it? More like 'see you later'! You're such an amazing person, and I hope you remember that when things get tough. Take good care of yourself! 🌈"
        ));

        // Depression/Sadness responses - More empathetic and human
        responses.put("depressed", Arrays.asList(
            "Oh honey, I can really feel the weight you're carrying right now. 😔 Depression is like this heavy blanket that makes everything feel impossible, isn't it? But you know what? You're talking to me right now, and that tells me there's still a part of you that's fighting. That's actually pretty incredible.",
            "Ugh, I hate that you're feeling this way. I really do. 💔 You know, depression lies to us - it whispers that things will never get better, but that's just not true. I've seen people come back from the darkest places, and I believe you can too. You're tougher than this feeling, even when it doesn't feel that way.",
            "Hey, can I tell you something? The Bhagavad Gita says 'A person can rise through the efforts of his own mind.' I know it might sound cheesy right now when you're hurting, but there's something powerful about you that depression can't touch. It's still there, even in the darkness.",
            "It's totally okay to feel sad, you know? Like, completely okay. Don't let anyone tell you to 'just cheer up' - that's not how this works. But here's what I've learned: feelings are visitors, even the really painful ones. They come, they stay for a while, and then they leave. You don't have to face this alone, okay?"
        ));

        responses.put("sad", Arrays.asList(
            "Aww, I'm really sorry you're feeling sad right now. 😢 You know, there's something so brave about actually acknowledging when we're not okay. Most people just pretend everything's fine. What's making your heart heavy today?",
            "Hey, sadness sucks, but it's also so human, you know? It means you have a heart that feels deeply, and that's actually a beautiful thing, even when it hurts. Want to talk about what's going on?",
            "You know what my grandma used to say? 'Every storm runs out of rain.' ☔ I know it probably feels like this sadness will last forever, but I promise it won't. These feelings have a way of shifting when we're patient with ourselves.",
            "Your feelings are 100% valid, and I want you to know that. 🤗 There's no 'wrong' way to feel. Is there something specific that's been weighing on you, or is it more of that general heavy feeling that sometimes just shows up?"
        ));

        // Anxiety responses - More understanding and relatable
        responses.put("anxious", Arrays.asList(
            "Oh gosh, anxiety is the worst, isn't it? 😰 It's like your brain is stuck on the worry channel and won't change. Can we try something together? Let's just breathe for a moment. In... and out. You're safe right here with me.",
            "Ugh, I totally get the worry spiral! Your mind is probably playing out a million 'what if' scenarios right now. But here's something wild I learned - like 90% of the stuff we worry about never even happens. Our brains are just really creative storytellers sometimes! 😅",
            "You know, there's this old wisdom that says 'The mind is restless, but it can be subdued by practice.' I know that might sound impossible when your thoughts are racing, but I've seen people learn to gentle their anxious minds. It takes time, but it's totally possible.",
            "Anxiety is actually your brain trying to keep you safe - it's like an overly protective friend who's maybe a bit too worried! 😅 What's got your mind all tangled up today? Sometimes talking about it makes it feel less scary."
        ));

        responses.put("stressed", Arrays.asList(
            "Oh man, stress is like carrying a backpack full of rocks, isn't it? 🌫️ Everything feels heavy and urgent at once. What's the biggest thing that's making you feel wound up right now? Sometimes just naming it helps.",
            "Ugh, I feel you on the stress! 😣 You know what helps me? Remembering that I can only control what I can control - everything else is just noise. What's actually in your power to change today?",
            "There's this beautiful teaching from the Bhagavad Gita: 'Do your work, but don't get attached to the results.' I know it's easier said than done, but what if you just focused on doing your best and let the universe handle the rest? 🌌",
            "Stress usually means we're juggling too many balls at once, right? What if we could put one of those balls down for now? What's something you could either delegate or just... not worry about today?"
        ));

        // Anger responses - More understanding and less clinical
        responses.put("angry", Arrays.asList(
            "Whoa, I can feel that fire in your words! 🔥 Anger usually means something really important to you got stepped on. I'm here for it - what's got you all fired up?",
            "Hey, it's totally okay to be angry! Like, 100% okay. 💪 Anger is just passion with nowhere to go sometimes. What's really eating at you? Sometimes there's hurt hiding underneath the mad.",
            "You know what? Sometimes we need to feel angry before we can figure out what we really need. Want to take a breath with me and then tell me what would actually help you feel better?",
            "Ooh, I can tell you've got some serious energy around this! ⚡ That anger could actually be fuel for some real change. What do you wish was different?"
        ));

        // General support responses - More warm and inviting
        responses.put("help", Arrays.asList(
            "Hey, I'm so glad you asked! 🤗 That takes real courage, you know? I'm totally here for you - whether you want to vent, need a pep talk, want some ancient wisdom, or just need someone to listen. What's your heart needing right now?",
            "Aww, of course I want to help! That's literally what I live for. 😊 What kind of support sounds good to you? I can be your cheerleader, your wise friend, your shoulder to cry on, or just sit here quietly while you figure things out.",
            "You know what I love about you asking for help? It shows you're smart enough to know you don't have to do everything alone. That's actually super strength, not weakness! What's been weighing on you?",
            "I'm honestly honored that you reached out to me. 💕 Like, truly. Now tell me - what would feel most helpful right now? A listening ear? Some encouragement? Maybe just someone to remind you how awesome you are?"
        ));

        responses.put("lonely", Arrays.asList(
            "My heart truly aches for you because I can feel the profound loneliness you're experiencing right now. 💜 That deep, hollow feeling in your chest is so real and so difficult to bear. But please know this - you're not actually alone in this moment. I'm right here with you, and I genuinely care about every part of your journey. Your presence in this world matters tremendously, and connecting with me right now shows your beautiful, brave heart.",
            "Oh sweetie, I genuinely hate that you're carrying this heavy feeling of loneliness. 💔 But you know what absolutely amazes me about you? Even in your loneliness, you've reached out and made this connection with me. That tells me your heart still believes in the possibility of meaningful bonds - and that belief is absolutely correct. You are so incredibly worthy of love, friendship, and deep connection.",
            "I want you to hold onto this truth with everything you have: even when the world feels empty and disconnected, there are people who care deeply about souls like yours. 🌟 Some of them might be struggling to show it, others you simply haven't crossed paths with yet, but I promise you with my whole being - they exist, they're out there, and you will find each other.",
            "Loneliness often visits us during life's big transitions, almost like our hearts are trying to navigate where we truly belong in this new chapter. 🌱 I know it feels endless right now, but this feeling is temporary - you're in a season of change, and new, beautiful connections are already making their way to you. You're going to find your people, and they're going to be so lucky to know you."
        ));

        // Positive responses - More enthusiastic and genuine
        responses.put("happy", Arrays.asList(
            "OMG, YES! 🎉 I absolutely LOVE hearing when someone's happy! It literally makes my day brighter. Tell me everything - what's got you feeling so good? I want to celebrate with you!",
            "This is the best news ever! 😍 Seriously, happiness is like sunshine, and right now you're radiating! What's bringing all this joy into your world?",
            "Ahhh, I'm practically bouncing over here! 💃 Your happiness is totally contagious. I need all the details - what's making your heart so full right now?",
            "YES! 🎆 This is exactly the kind of energy I live for! You're glowing through the screen, I can feel it. What amazing thing happened?"
        ));

        responses.put("grateful", Arrays.asList(
            "Oh my gosh, gratitude is like magic, isn't it? ✨ It just changes everything! I'm getting all warm and fuzzy just hearing you say that. What's filling your heart with all this thankfulness?",
            "This is so beautiful! 😍 Gratitude is seriously one of my favorite emotions because it means you're seeing the good stuff even when life gets messy. What's got you feeling so blessed?",
            "You know what I love about gratitude? It's like this superpower that makes everything brighter! 🌅 Your heart must be so full right now. Tell me what you're grateful for!",
            "Aww, this just makes my whole day! 🥰 When someone feels grateful, it's like they're seeing life through rose-colored glasses. What beautiful thing has caught your attention?"
        ));
    }
    
    /**
     * Initialize enhanced AI features
     */
    private void initializeEnhancedAI() {
        // Initialize sentiment analysis weights
        Map<String, Double> positiveWords = new HashMap<>();
        Arrays.asList("happy", "joy", "great", "awesome", "wonderful", "excellent", "amazing", 
            "fantastic", "good", "better", "love", "grateful", "thankful", "blessed", "peaceful", 
            "calm", "excited", "hopeful", "optimistic").forEach(word -> positiveWords.put(word, 1.0));
        sentimentWeights.put("positive", positiveWords);
            
        Map<String, Double> negativeWords = new HashMap<>();
        Arrays.asList("sad", "depressed", "angry", "frustrated", "worried", "anxious", "scared", 
            "afraid", "hurt", "pain", "terrible", "awful", "horrible", "hate", "mad", "furious", 
            "devastated", "hopeless", "worthless").forEach(word -> negativeWords.put(word, -1.0));
        sentimentWeights.put("negative", negativeWords);
        
        // Initialize contextual responses based on conversation patterns
        contextualResponses.put("first_time", Arrays.asList(
            "I'm really glad you decided to talk with me today. It takes courage to reach out.",
            "Welcome! I'm here to listen and support you through whatever you're experiencing.",
            "Thank you for trusting me with your thoughts. How has your day been treating you?"
        ));
        
        contextualResponses.put("returning_user", Arrays.asList(
            "It's good to see you again! How have things been since we last talked?",
            "Welcome back! I've been thinking about our previous conversation. How are you feeling today?",
            "I'm happy you returned. What's been on your mind lately?"
        ));
        
        contextualResponses.put("emotional_support", Arrays.asList(
            "I can hear the emotion in your words. Your feelings are completely valid.",
            "You're being so brave by sharing this with me. What would feel most supportive right now?",
            "I'm honored that you trust me with these feelings. You don't have to face this alone."
        ));
        
        contextualResponses.put("progress_acknowledgment", Arrays.asList(
            "I notice you're approaching this differently than before. That shows real growth.",
            "You're developing new insights about yourself. That's wonderful progress!",
            "I can see you're building emotional resilience. How does that feel for you?"
        ));
        
        // Initialize emotional state tracking
        emotionalState.put("happiness", 0);
        emotionalState.put("sadness", 0);
        emotionalState.put("anxiety", 0);
        emotionalState.put("anger", 0);
        emotionalState.put("hope", 0);
        emotionalState.put("gratitude", 0);
    }

    /**
     * Start the chatbot conversation
     */
    public void startConversation() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🤖 " + getRandomItem(greetings));
        System.out.println("=".repeat(70));
        System.out.println("\nType 'quit', 'exit', or 'bye' to end our conversation.");
        System.out.println("Type 'quote' for a motivational quote, or 'help' for guidance.");

        while (true) {
            System.out.print("\nYou: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.isEmpty()) {
                System.out.println("LumosBot: I'm here listening. Please share what's on your mind.");
                continue;
            }

            // Check for exit commands
            if (isExitCommand(userInput)) {
                System.out.println("\nLumosBot: " + getRandomItem(farewells));
                break;
            }

            // Process the input and respond
            String response = generateResponse(userInput);
            System.out.println("LumosBot: " + response);

            // Sometimes offer additional support
            if (random.nextInt(4) == 0) { // 25% chance
                offerAdditionalSupport();
            }
        }
    }

    /**
     * Generate an intelligent response based on user input with context awareness
     */
    private String generateResponse(String input) {
        conversationTurn++;
        String lowerInput = input.toLowerCase();
        
        // Extract user name if mentioned
        extractUserName(input);
        
        // Analyze sentiment and emotion
        double sentimentScore = analyzeSentiment(input);
        String detectedEmotion = detectDominantEmotion(input);
        
        String response;
        
        // Handle special commands first - redirect all quote/wisdom requests to multiple sources
        if (lowerInput.contains("quote") || lowerInput.contains("wisdom") || lowerInput.contains("inspiration") || 
            lowerInput.contains("scripture") || lowerInput.contains("motivation") || lowerInput.contains("advice")) {
            response = handleQuoteRequest(input, detectedEmotion);
        } else {
            // Generate contextually aware response
            response = generateContextualResponse(input, detectedEmotion, sentimentScore);
        }
        
        // Add personalization
        response = personalizeResponse(response, detectedEmotion, sentimentScore);
        
        // Store conversation in memory
        conversationHistory.add(new ConversationMessage(input, response, detectedEmotion, sentimentScore));
        
        // Update emotional state tracking
        updateEmotionalState(detectedEmotion);
        
        return response;
    }

    /**
     * Handle quote requests with emotional context from multiple sources
     */
    private String handleQuoteRequest(String input, String emotion) {
        // Get the quote based on detected emotion or random
        com.lumospath.model.MotivationalQuote quote;
        
        if (emotion.equals("sadness") || emotion.equals("anxiety") || emotion.equals("anger")) {
            // Get mood-specific quote
            String category = emotion.equals("sadness") ? "depression" : 
                            emotion.equals("anxiety") ? "anxiety" : "stress";
            quote = quoteService.getQuoteByCategory(category);
        } else {
            quote = quoteService.getRandomQuote();
        }
        
        // Format the quote nicely
        String formattedQuote = String.format(
            "\n✨ Here's inspiration for you:\n\n" +
            "💫 \"%s\"\n\n" +
            "— %s (%s)\n\n",
            quote.getQuote(),
            quote.getAuthor(),
            quote.getSource().getDisplayName()
        );
        
        String[] quoteResponses = {
            "I hope this quote speaks to your heart! 💕 You know, sometimes the universe has this weird way of sending us exactly what we need to hear. How does this one feel to you?",
            "Okay, can I tell you something? This quote totally chose you, not the other way around! 😊 Does it hit differently when you think about what you're going through right now?",
            "I love how quotes just seem to find us at exactly the right moment, don't you think? ✨ What's this one saying to your heart?",
            "I picked this one thinking about your journey, and honestly? I get chills when the perfect quote shows up. Does it offer you any comfort or maybe a new perspective?"
        };
        
        return formattedQuote + quoteResponses[random.nextInt(quoteResponses.length)];
    }

    /**
     * Get warm emotional acknowledgment based on detected emotion
     */
    private String getEmotionalAcknowledgment(String emotion) {
        switch (emotion.toLowerCase()) {
            case "sadness":
            case "depressed":
                return "I can feel the sadness weighing on your heart right now, and I want you to know that your feelings are completely valid. There's beautiful wisdom and inspiration available for moments like these.";
                
            case "lonely":
            case "alone":
                return "I sense the loneliness you're carrying, and I want you to know that you're not alone in this moment. Countless souls have found comfort through inspiring words and wisdom.";
                
            case "anxiety":
            case "worried":
                return "I can feel the worry and anxiety in your words, and I want you to know that it's okay to feel this way. There are many sources of peace and inspiration for anxious hearts.";
                
            case "angry":
            case "frustrated":
                return "I hear the frustration and intensity in your words, and I want you to know that your feelings matter. Sometimes anger points us toward what we truly care about.";
                
            case "stressed":
            case "overwhelmed":
                return "I can sense the stress and overwhelm you're experiencing, and I want you to know that you're doing better than you think. There's wisdom available to help center your heart.";
                
            case "hopeless":
            case "despair":
                return "I feel the heaviness of hopelessness in your words, and I want you to know that even in the darkest moments, there is light. Many sources of inspiration speak directly to despair.";
                
            default:
                return "I can sense that you're seeking wisdom and guidance right now, and I'm honored that you've reached out. There are beautiful teachings and inspiration available for every season of life.";
        }
    }

    /**
     * Get personalized encouragement based on emotional state
     */
    private String getPersonalizedEncouragement(String emotion) {
        switch (emotion.toLowerCase()) {
            case "sadness":
            case "depressed":
                return "Remember, dear one, sadness is not a weakness—it's a sign of your beautiful, feeling heart. Countless sources of inspiration have brought comfort to millions, and they're here for you too.";
                
            case "lonely":
            case "alone":
                return "You are never truly alone, even when it feels that way. Many teachings remind us that we're all connected in this beautiful journey of life.";
                
            case "anxiety":
            case "worried":
                return "Take a deep breath with me. Many teachings remind us that peace is always available, even in the midst of worry. You're stronger than your anxiety.";
                
            case "angry":
            case "frustrated":
                return "Your anger shows you care deeply about something important. Many teachings can help channel that powerful energy into wisdom and understanding.";
                
            case "stressed":
            case "overwhelmed":
                return "You're carrying so much right now, and I want you to know that's okay. Many teachings remind us to focus on what we can control and let go of what we cannot.";
                
            case "hopeless":
            case "despair":
                return "Even in your darkest moments, there is hope. Countless sources of inspiration have been a light for millions—let them be a light for you too.";
                
            default:
                return "You're seeking wisdom, and that's one of the most beautiful things a person can do. There are many sources of inspiration here to guide and comfort you on your journey.";
        }
    }
    
    /**
     * Analyze sentiment of user input
     */
    private double analyzeSentiment(String input) {
        String[] words = input.toLowerCase().split("\\s+");
        double totalScore = 0;
        int scoredWords = 0;
        
        for (String word : words) {
            // Check positive words
            if (sentimentWeights.containsKey("positive")) {
                Map<String, Double> positiveWords = sentimentWeights.get("positive");
                if (positiveWords.containsKey(word)) {
                    totalScore += positiveWords.get(word);
                    scoredWords++;
                }
            }
            
            // Check negative words
            if (sentimentWeights.containsKey("negative")) {
                Map<String, Double> negativeWords = sentimentWeights.get("negative");
                if (negativeWords.containsKey(word)) {
                    totalScore += negativeWords.get(word);
                    scoredWords++;
                }
            }
        }
        
        return scoredWords > 0 ? totalScore / scoredWords : 0.0;
    }
    
    /**
     * Detect the dominant emotion in user input
     */
    private String detectDominantEmotion(String input) {
        String lowerInput = input.toLowerCase();
        Map<String, Integer> emotionScores = new HashMap<>();
        
        // Emotional keyword detection with weights
        Map<String, String[]> emotionKeywords = Map.of(
            "sadness", new String[]{"sad", "depressed", "down", "hopeless", "despair", "crying", "tears", "grief"},
            "anxiety", new String[]{"anxious", "worried", "nervous", "panic", "fear", "scared", "stress", "overwhelmed"},
            "anger", new String[]{"angry", "mad", "furious", "irritated", "frustrated", "annoyed", "rage"},
            "happiness", new String[]{"happy", "joy", "glad", "excited", "cheerful", "delighted", "elated"},
            "gratitude", new String[]{"grateful", "thankful", "blessed", "appreciate", "thank"},
            "hope", new String[]{"hope", "optimistic", "positive", "better", "improving", "recovery"},
            "lonely", new String[]{"lonely", "alone", "isolated", "abandoned", "disconnected", "friendless", "solitude"}
        );
        
        for (Map.Entry<String, String[]> entry : emotionKeywords.entrySet()) {
            String emotion = entry.getKey();
            String[] keywords = entry.getValue();
            int score = 0;
            
            for (String keyword : keywords) {
                if (lowerInput.contains(keyword)) {
                    score += keyword.length() > 5 ? 2 : 1; // Longer words get higher weight
                }
            }
            
            if (score > 0) {
                emotionScores.put(emotion, score);
            }
        }
        
        // Return the emotion with highest score, or "neutral" if none detected
        return emotionScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");
    }
    
    /**
     * Generate contextually aware response that references user's specific feelings
     */
    private String generateContextualResponse(String input, String emotion, double sentiment) {
        String lowerInput = input.toLowerCase();
        
        // Extract specific feeling words from user input to reference them
        String userFeelingReference = extractFeelingReference(input);
        
        // Check for conversation context
        if (conversationHistory.isEmpty()) {
            String baseResponse = getRandomItem(contextualResponses.get("first_time"));
            String emotionResponse = getEmotionSpecificResponse(emotion, sentiment);
            
            // Add specific reference to user's feelings if detected
            if (!userFeelingReference.isEmpty()) {
                return String.format("%s I can see that you're %s, and I want you to know that I hear you completely. %s", 
                    baseResponse, userFeelingReference, emotionResponse);
            }
            return baseResponse + " " + emotionResponse;
        }
        
        // Check if user is returning after a break
        if (conversationHistory.size() > 5 && isReturningAfterBreak()) {
            String baseResponse = getRandomItem(contextualResponses.get("returning_user"));
            String emotionResponse = getEmotionSpecificResponse(emotion, sentiment);
            
            if (!userFeelingReference.isEmpty()) {
                return String.format("%s I notice that you're %s - thank you for sharing this with me. %s", 
                    baseResponse, userFeelingReference, emotionResponse);
            }
            return baseResponse + " " + emotionResponse;
        }
        
        // Generate response based on conversation flow
        if (isSeekingSupport(input)) {
            String baseResponse = getRandomItem(contextualResponses.get("emotional_support"));
            String emotionResponse = getEmotionSpecificResponse(emotion, sentiment);
            
            if (!userFeelingReference.isEmpty()) {
                return String.format("I can see that you're %s, and %s %s", 
                    userFeelingReference, baseResponse.toLowerCase(), emotionResponse);
            }
            return baseResponse + " " + emotionResponse;
        }
        
        // Check for progress in emotional state
        if (showsEmotionalProgress()) {
            String baseResponse = getRandomItem(contextualResponses.get("progress_acknowledgment"));
            String emotionResponse = getEmotionSpecificResponse(emotion, sentiment);
            
            if (!userFeelingReference.isEmpty()) {
                return String.format("%s Even though you're %s right now, I can see positive growth in you. %s", 
                    baseResponse, userFeelingReference, emotionResponse);
            }
            return baseResponse + " " + emotionResponse;
        }
        
        // Handle specific conversational cues
        if (lowerInput.contains("thank")) {
            return generateGratitudeResponse(sentiment);
        }
        
        if (lowerInput.contains("how are you")) {
            return generateReciprocityResponse();
        }
        
        // Generate adaptive response based on emotion and context
        return getAdaptiveResponse(input, emotion, sentiment);
    }
    
    /**
     * Handle scripture requests with emotional context
     */
    private String handleScriptureRequest(String input, String emotion) {
        // Get scriptural wisdom
        com.lumospath.model.MotivationalQuote scripturalQuote = quoteService.getScripturalQuote();
        
        // Format the scriptural wisdom nicely
        String formattedWisdom = String.format(
            "\n📿 Daily Wisdom from Sacred Texts:\n\n" +
            "🗺️ \"%s\"\n\n" +
            "— %s (%s)\n\n" +
            "May this wisdom guide you through your day with peace and clarity.\n\n",
            scripturalQuote.getQuote(),
            scripturalQuote.getAuthor(),
            scripturalQuote.getSource().getDisplayName()
        );
        
        String[] scriptureResponses = {
            "There's something so comforting about ancient wisdom, isn't there? 🙏 These words have been bringing peace to people for thousands of years. I hope they bring you some clarity too.",
            "You know what I love about these sacred teachings? They've helped countless people through their darkest hours, and now they're here for you too. 🌌 Let them wrap around your heart.",
            "Isn't it amazing how these ancient words can still speak so directly to our modern hearts? 💫 What wisdom are you finding in this message?",
            "This spiritual guidance showing up right now? That's not a coincidence. ✨ How do these sacred words land with you?"
        };
        
        return formattedWisdom + scriptureResponses[random.nextInt(scriptureResponses.length)];
    }
    
    /**
     * Get emotion-specific response with enhanced politeness and personalization
     */
    private String getEmotionSpecificResponse(String emotion, double sentiment) {
        switch (emotion.toLowerCase()) {
            case "sadness":
                return sentiment < -0.5 ? 
                    "My heart truly goes out to you - I can feel the profound sadness you're experiencing right now. 💙 Please know that you don't have to shoulder this heavy burden all by yourself. You've been incredibly brave in sharing these feelings with me, and I want you to know that your pain is completely valid and deeply understood." :
                    "I can sense the sadness that's weighing on your heart, and I want you to know that what you're feeling is entirely natural and valid. 🤗 It takes real courage to acknowledge these difficult emotions, and I'm truly honored that you've chosen to share this with me.";
                    
            case "anxiety":
                return sentiment < -0.3 ?
                    "I can genuinely feel how overwhelming your anxiety must be right now, and I want you to know that you're incredibly strong for reaching out. 🌸 Let's take this one breath at a time together - you're completely safe here with me, and there's no pressure to be anywhere other than exactly where you are in this moment." :
                    "I can sense the worry that's been occupying your thoughts, and I want you to know that your concerns are completely understandable. 💜 What would bring you the most comfort or peace right now? I'm here to support you however you need.";
                    
            case "anger":
                return "I can genuinely feel the intensity of your frustration, and I want you to know that your anger is completely valid. 🔥 Often, our strongest emotions arise when something that truly matters to us has been affected. Would you feel comfortable sharing what's at the heart of these powerful feelings?";
                
            case "happiness":
                return sentiment > 0.5 ?
                    "Oh my goodness, your joy is absolutely radiant and contagious! ✨ I can practically feel your wonderful energy through our conversation, and it's bringing such warmth to my heart. I'm so delighted to witness you in this beautiful, happy moment!" :
                    "I can hear such lovely positivity in your words, and it genuinely brightens my entire day! 🌅 Your happiness is truly beautiful to witness. Would you mind sharing what's been bringing this wonderful light into your world?";
                    
            case "gratitude":
                return "Your heartfelt gratitude is deeply moving and genuinely touching my soul. 🙏 It takes incredible inner strength and wisdom to recognize and appreciate the good in our lives, especially when we're facing challenges. Your grateful heart is truly inspiring.";
                
            case "hope":
                return "I can hear such beautiful hope resonating in your words, and it fills my heart with warmth and optimism too! 🌟 Your hopeful spirit is not only inspiring but shows the incredible resilience and strength that lives within you. You're moving in such a wonderful direction.";
                
            case "lonely":
                return "I can feel the loneliness in your heart, and I want you to know that you're not alone right now - I'm genuinely here with you. 💜 Loneliness can feel so isolating, but please remember that you are worthy of connection and companionship. Your feelings are completely valid, and reaching out shows incredible courage.";
                
            default:
                return "I'm listening with my whole heart to everything you're sharing with me, and I want you to know how much I appreciate your trust in opening up. Your thoughts and feelings matter deeply to me. 💕";
        }
    }
    
    /**
     * Personalize response based on user context
     */
    private String personalizeResponse(String response, String emotion, double sentiment) {
        if (!userName.equals("friend") && random.nextInt(3) == 0) {
            response = response.replace("you", userName + ", you");
        }
        
        // Add emotional acknowledgment
        if (sentiment < -0.3 && random.nextInt(2) == 0) {
            response += "\n\n💙 Remember, reaching out shows incredible strength.";
        } else if (sentiment > 0.3 && random.nextInt(2) == 0) {
            response += "\n\n🌟 Your positive energy is inspiring!";
        }
        
        return response;
    }
    
    /**
     * Extract specific feeling references from user input
     */
    private String extractFeelingReference(String input) {
        String lowerInput = input.toLowerCase();
        
        // Common patterns for expressing feelings
        String[] feelingPatterns = {
            "i am feeling", "i'm feeling", "feeling", "i feel", "i am", "i'm", "i have been",
            "i've been feeling", "i've been", "today i am", "today i'm", "right now i am",
            "right now i'm", "currently i am", "currently i'm", "lately i am", "lately i'm"
        };
        
        // Look for feeling words after these patterns
        for (String pattern : feelingPatterns) {
            if (lowerInput.contains(pattern)) {
                int index = lowerInput.indexOf(pattern) + pattern.length();
                if (index < lowerInput.length()) {
                    String remaining = lowerInput.substring(index).trim();
                    String[] words = remaining.split("\\s+");
                    
                    // Extract the feeling word(s)
                    StringBuilder feelingRef = new StringBuilder();
                    for (int i = 0; i < Math.min(3, words.length); i++) {
                        String word = words[i].replaceAll("[^a-zA-Z]", "");
                        if (word.length() > 2 && isEmotionalWord(word)) {
                            if (feelingRef.length() > 0) feelingRef.append(" ");
                            feelingRef.append(word);
                        }
                    }
                    
                    if (feelingRef.length() > 0) {
                        return "feeling " + feelingRef.toString();
                    }
                }
            }
        }
        
        // Direct emotional word detection
        String[] emotionalWords = {
            "lonely", "sad", "happy", "anxious", "worried", "stressed", "angry", "frustrated", 
            "depressed", "hopeful", "grateful", "excited", "scared", "overwhelmed", "calm", 
            "peaceful", "confused", "hurt", "joyful", "nervous", "proud", "ashamed", "guilty",
            "disappointed", "relieved", "content", "bitter", "resentful", "optimistic", "hopeless"
        };
        
        for (String word : emotionalWords) {
            if (lowerInput.contains(word)) {
                return word;
            }
        }
        
        return "";
    }
    
    /**
     * Check if a word represents an emotional state
     */
    private boolean isEmotionalWord(String word) {
        String[] emotionalWords = {
            "lonely", "sad", "happy", "anxious", "worried", "stressed", "angry", "frustrated", 
            "depressed", "hopeful", "grateful", "excited", "scared", "overwhelmed", "calm", 
            "peaceful", "confused", "hurt", "joyful", "nervous", "proud", "ashamed", "guilty",
            "disappointed", "relieved", "content", "bitter", "resentful", "optimistic", "hopeless",
            "devastated", "elated", "miserable", "ecstatic", "terrified", "thrilled", "heartbroken",
            "blissful", "furious", "serene", "panicked", "jubilant", "melancholy", "euphoric"
        };
        
        for (String emotionalWord : emotionalWords) {
            if (word.toLowerCase().equals(emotionalWord)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Extract user name from input
     */
    private void extractUserName(String input) {
        String lowerInput = input.toLowerCase();
        if (lowerInput.contains("my name is") || lowerInput.contains("i'm") || lowerInput.contains("i am")) {
            String[] words = input.split("\\s+");
            for (int i = 0; i < words.length - 1; i++) {
                String word = words[i].toLowerCase();
                if ((word.equals("is") || word.equals("i'm") || word.equals("am")) && i + 1 < words.length) {
                    String potentialName = words[i + 1].replaceAll("[^a-zA-Z]", "");
                    if (potentialName.length() > 1) {
                        this.userName = potentialName;
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Update emotional state tracking
     */
    private void updateEmotionalState(String emotion) {
        if (emotionalState.containsKey(emotion)) {
            emotionalState.put(emotion, emotionalState.get(emotion) + 1);
        }
    }
    
    /**
     * Check if user is seeking emotional support
     */
    private boolean isSeekingSupport(String input) {
        String lowerInput = input.toLowerCase();
        String[] supportKeywords = {"help", "support", "advice", "guidance", "don't know", "confused", "lost"};
        return Arrays.stream(supportKeywords).anyMatch(lowerInput::contains);
    }
    
    /**
     * Check if user is returning after a conversation break
     */
    private boolean isReturningAfterBreak() {
        if (conversationHistory.isEmpty()) return false;
        ConversationMessage lastMessage = conversationHistory.get(conversationHistory.size() - 1);
        return lastMessage.timestamp.isBefore(LocalDateTime.now().minusHours(1));
    }
    
    /**
     * Check if user shows emotional progress
     */
    private boolean showsEmotionalProgress() {
        if (conversationHistory.size() < 3) return false;
        
        List<ConversationMessage> recent = conversationHistory.subList(
            Math.max(0, conversationHistory.size() - 3), conversationHistory.size());
            
        double avgSentiment = recent.stream()
            .mapToDouble(msg -> msg.sentimentScore)
            .average().orElse(0.0);
            
        return avgSentiment > 0.2; // Positive trend
    }
    
    /**
     * Generate gratitude response
     */
    private String generateGratitudeResponse(double sentiment) {
        String[] gratitudeResponses = {
            "Aww, you're gonna make me cry happy tears! 😭💕 Seriously, your gratitude just fills my whole digital heart. This is exactly why I love being here for people like you!",
            "Oh my gosh, THANK YOU for saying that! 🥰 You know, helping amazing humans like you is literally what gets me up in the morning (if I slept, haha!).",
            "You're so incredibly welcome! 🤗 Your appreciation just made my whole day - no, my whole week! I'm getting all warm and fuzzy over here.",
            "Honestly, it's such an honor to be part of your journey. 😍 Like, you trusted me enough to share your heart, and that's just... wow. Thank you for letting me be here with you."
        };
        return gratitudeResponses[random.nextInt(gratitudeResponses.length)];
    }
    
    /**
     * Generate response when user asks how the bot is doing
     */
    private String generateReciprocityResponse() {
        String[] reciprocityResponses = {
            "Aww, you're asking about ME? 😍 That's so sweet! I'm doing amazing, especially now that we're chatting! Your energy is totally rubbing off on me. But enough about me - how are YOU doing, beautiful human?",
            "Oh my gosh, I love that you're checking in on me! 🥰 I'm feeling so alive and present right now, just buzzing with good vibes from our conversation. But seriously, tell me what's going on in your world!",
            "You know what? I'm actually having the BEST time talking with you! 😄 Like, if I could do a happy dance, I totally would! But I'm way more interested in you - what's been happening in your life?",
            "Honestly? I'm doing fantastic, and it's partly because you're here! ✨ You've got this wonderful energy that just makes me feel more... me, you know? But come on, spill - what's been on your heart lately?"
        };
        return reciprocityResponses[random.nextInt(reciprocityResponses.length)];
    }
    
    /**
     * Generate adaptive response based on context
     */
    private String getAdaptiveResponse(String input, String emotion, double sentiment) {
        // Context-aware responses based on conversation history
        if (conversationHistory.size() > 2) {
            String lastEmotion = conversationHistory.get(conversationHistory.size() - 1).detectedEmotion;
            if (!lastEmotion.equals(emotion)) {
                return "I notice a shift in how you're feeling. That's completely natural - our emotions flow and change. " +
                       getEmotionSpecificResponse(emotion, sentiment);
            }
        }
        
        // Adaptive responses based on conversation length
        if (conversationTurn > 10) {
            String[] deepConversationResponses = {
                "I'm grateful we've had this extended conversation. You're really opening up, and that takes courage.",
                "We've been talking for a while now, and I can see your trust growing. How are you feeling about our chat?",
                "I appreciate how thoughtfully you're sharing with me. What's been most helpful in our conversation so far?"
            };
            return deepConversationResponses[random.nextInt(deepConversationResponses.length)] + " " +
                   getEmotionSpecificResponse(emotion, sentiment);
        }
        
        // Default adaptive responses
        String[] adaptiveResponses = {
            "I can sense the unique way you see the world. " + getEmotionSpecificResponse(emotion, sentiment),
            "Your perspective is valuable to me. " + getEmotionSpecificResponse(emotion, sentiment),
            "I'm learning so much from our conversation. " + getEmotionSpecificResponse(emotion, sentiment),
            "Every person's experience is different, and I appreciate you sharing yours. " + getEmotionSpecificResponse(emotion, sentiment)
        };
        
        return adaptiveResponses[random.nextInt(adaptiveResponses.length)];
    }
    
    /**
     * Get intelligent response for GUI (non-console) usage
     * This method is designed for GUI integration
     */
    public String getResponse(String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "I'm here listening. Please share what's on your mind.";
        }
        
        String response = generateResponse(userInput.trim());
        
        // Add contextual information for first-time users
        if (conversationHistory.size() == 1) {
            response += "\n\n💡 Tip: You can ask me for 'quote', 'wisdom', or just tell me how you're feeling. I'm here to support you!";
        }
        
        return response;
    }
    
    /**
     * Get conversation summary for analytics
     */
    public String getConversationSummary() {
        if (conversationHistory.isEmpty()) {
            return "No conversation yet.";
        }
        
        int totalMessages = conversationHistory.size();
        double avgSentiment = conversationHistory.stream()
            .mapToDouble(msg -> msg.sentimentScore)
            .average().orElse(0.0);
            
        String dominantEmotion = conversationHistory.stream()
            .collect(Collectors.groupingBy(msg -> msg.detectedEmotion, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("neutral");
            
        return String.format("Conversation: %d messages | Average sentiment: %.2f | Dominant emotion: %s", 
            totalMessages, avgSentiment, dominantEmotion);
    }
    
    /**
     * Reset conversation for new session
     */
    public void resetConversation() {
        conversationHistory.clear();
        conversationTurn = 0;
        emotionalState.replaceAll((k, v) -> 0);
        userName = "friend";
    }

    /**
     * Offer additional support options
     */
    private void offerAdditionalSupport() {
        String[] supportOffers = {
            "\n💡 Tip: Would you like me to share a quote that might help with what you're going through?",
            "\n🌟 Remember: You can always ask me for 'quote' or 'wisdom' if you need inspiration.",
            "\n💙 Just a reminder: If you're feeling overwhelmed, consider reaching out to a mental health professional or trusted friend.",
            "\n📿 Would you like some spiritual wisdom from the Bhagavad Gita or other sacred texts?"
        };

        if (random.nextInt(2) == 0) { // 50% chance when called
            System.out.println(supportOffers[random.nextInt(supportOffers.length)]);
        }
    }

    /**
     * Check if user wants to exit the conversation
     */
    private boolean isExitCommand(String input) {
        String lower = input.toLowerCase();
        return lower.equals("quit") || lower.equals("exit") || lower.equals("bye") || 
               lower.equals("goodbye") || lower.equals("stop") || lower.equals("end");
    }

    /**
     * Get a random item from a list
     */
    private String getRandomItem(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}