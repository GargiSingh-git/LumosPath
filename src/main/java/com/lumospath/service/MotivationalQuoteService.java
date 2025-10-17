package com.lumospath.service;

import com.lumospath.model.MotivationalQuote;
import com.lumospath.model.MotivationalQuote.QuoteSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service class for handling motivational quotes and affirmations
 */
public class MotivationalQuoteService {
    private List<MotivationalQuote> quotes;
    private Random random;

    public MotivationalQuoteService() {
        this.quotes = new ArrayList<>();
        this.random = new Random();
        initializeQuotes();
    }

    /**
     * Initialize the quotes database with scriptural and motivational quotes
     */
    private void initializeQuotes() {
        // Bhagavad Gita quotes
        addQuote("You have the right to perform your actions, but you are not entitled to the fruits of action.", 
                "Lord Krishna", QuoteSource.BHAGAVAD_GITA, "general");
        
        addQuote("The mind is restless and difficult to restrain, but it is subdued by practice.", 
                "Lord Krishna", QuoteSource.BHAGAVAD_GITA, "anxiety");
        
        addQuote("A person can rise through the efforts of his own mind; or draw himself down, in the same manner.", 
                "Lord Krishna", QuoteSource.BHAGAVAD_GITA, "depression");
        
        addQuote("When meditation is mastered, the mind is unwavering like the flame of a candle in a windless place.", 
                "Lord Krishna", QuoteSource.BHAGAVAD_GITA, "stress");
        
        addQuote("The soul is neither born, and nor does it die.", 
                "Lord Krishna", QuoteSource.BHAGAVAD_GITA, "general");

        // Srimad Bhagavatam quotes
        addQuote("The greatest fear people have is that of death, but when we understand that the soul is eternal, this fear diminishes.", 
                "Vyasadeva", QuoteSource.SRIMAD_BHAGAVATAM, "anxiety");
        
        addQuote("Happiness and distress, gain and loss, victory and defeat are all temporary. Learn to remain equipoised.", 
                "Narada Muni", QuoteSource.SRIMAD_BHAGAVATAM, "general");

        // Motivational speakers
        addQuote("The only way to do great work is to love what you do.", 
                "Steve Jobs", QuoteSource.MOTIVATIONAL_SPEAKER, "general");
        
        addQuote("It does not matter how slowly you go as long as you do not stop.", 
                "Confucius", QuoteSource.MOTIVATIONAL_SPEAKER, "depression");
        
        addQuote("You are never too old to set another goal or to dream a new dream.", 
                "C.S. Lewis", QuoteSource.MOTIVATIONAL_SPEAKER, "general");
        
        addQuote("The way to get started is to quit talking and begin doing.", 
                "Walt Disney", QuoteSource.MOTIVATIONAL_SPEAKER, "stress");

        // General internet wisdom
        addQuote("Every storm runs out of rain. Every dark night turns into day.", 
                "Gary Allan", QuoteSource.INTERNET, "depression");
        
        addQuote("You don't have to control your thoughts. You just have to stop letting them control you.", 
                "Dan Millman", QuoteSource.INTERNET, "anxiety");
        
        addQuote("The strongest people are not those who show strength in front of us, but those who win battles we know nothing about.", 
                "Unknown", QuoteSource.INTERNET, "general");
        
        addQuote("Healing doesn't mean the damage never existed. It means the damage no longer controls our lives.", 
                "Akshay Dubey", QuoteSource.INTERNET, "depression");
        
        addQuote("You are braver than you believe, stronger than you seem, and smarter than you think.", 
                "A.A. Milne", QuoteSource.INTERNET, "general");

        // More specific quotes for different moods
        addQuote("This too shall pass. Nothing in life is permanent.", 
                "Persian Proverb", QuoteSource.INTERNET, "depression");
        
        addQuote("Anxiety is the dizziness of freedom.", 
                "Søren Kierkegaard", QuoteSource.MOTIVATIONAL_SPEAKER, "anxiety");
        
        addQuote("The present moment is the only time over which we have dominion.", 
                "Thích Nhất Hạnh", QuoteSource.MOTIVATIONAL_SPEAKER, "stress");
    }

    /**
     * Add a quote to the collection
     */
    private void addQuote(String text, String author, QuoteSource source, String category) {
        MotivationalQuote quote = new MotivationalQuote(text, author, source, category);
        quotes.add(quote);
    }

    /**
     * Get a random quote from all quotes
     */
    public MotivationalQuote getRandomQuote() {
        if (quotes.isEmpty()) {
            return new MotivationalQuote("Stay strong and keep moving forward!", "LumosPath", QuoteSource.CUSTOM, "general");
        }
        return quotes.get(random.nextInt(quotes.size()));
    }

    /**
     * Get a random quote for a specific category
     */
    public MotivationalQuote getQuoteByCategory(String category) {
        List<MotivationalQuote> categoryQuotes = quotes.stream()
                .filter(quote -> quote.getCategory().equalsIgnoreCase(category))
                .toList();
        
        if (categoryQuotes.isEmpty()) {
            return getRandomQuote(); // Fallback to random quote
        }
        
        return categoryQuotes.get(random.nextInt(categoryQuotes.size()));
    }

    /**
     * Get a random quote from scriptures (Bhagavad Gita or Srimad Bhagavatam)
     */
    public MotivationalQuote getScripturalQuote() {
        List<MotivationalQuote> scripturalQuotes = quotes.stream()
                .filter(quote -> quote.getSource() == QuoteSource.BHAGAVAD_GITA || 
                               quote.getSource() == QuoteSource.SRIMAD_BHAGAVATAM)
                .toList();
        
        if (scripturalQuotes.isEmpty()) {
            return getRandomQuote();
        }
        
        return scripturalQuotes.get(random.nextInt(scripturalQuotes.size()));
    }

    /**
     * Display a motivational quote in a formatted way
     */
    public void displayRandomQuote() {
        MotivationalQuote quote = getRandomQuote();
        displayQuote(quote);
    }

    /**
     * Display a quote for specific mood/situation
     */
    public void displayQuoteForMood(String mood) {
        System.out.println("\n🌟 Here's something that might help:");
        MotivationalQuote quote;
        
        // Map moods to categories
        switch (mood.toLowerCase()) {
            case "sad":
            case "depressed":
            case "depression":
                quote = getQuoteByCategory("depression");
                break;
            case "anxious":
            case "anxiety":
            case "worried":
                quote = getQuoteByCategory("anxiety");
                break;
            case "stressed":
            case "stress":
            case "overwhelmed":
                quote = getQuoteByCategory("stress");
                break;
            default:
                quote = getRandomQuote();
        }
        
        displayQuote(quote);
    }

    /**
     * Display daily wisdom from scriptures
     */
    public void displayDailyWisdom() {
        System.out.println("\n📿 Daily Wisdom from Sacred Texts:");
        MotivationalQuote quote = getScripturalQuote();
        displayQuote(quote);
        System.out.println("\nMay this wisdom guide you through your day with peace and clarity.");
    }

    /**
     * Format and display a quote
     */
    private void displayQuote(MotivationalQuote quote) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💫 \"" + quote.getQuote() + "\"");
        System.out.println("\n   - " + quote.getAuthor() + " (" + quote.getSource().getDisplayName() + ")");
        System.out.println("=".repeat(60));
    }

    /**
     * Get quotes by source
     */
    public List<MotivationalQuote> getQuotesBySource(QuoteSource source) {
        return quotes.stream()
                .filter(quote -> quote.getSource() == source)
                .toList();
    }

    /**
     * Get all available categories
     */
    public List<String> getAvailableCategories() {
        return quotes.stream()
                .map(MotivationalQuote::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Interactive quote menu
     */
    public void showQuoteMenu() {
        System.out.println("\n=== Motivational Quotes & Wisdom ===");
        System.out.println("1. 🎲 Random inspirational quote");
        System.out.println("2. 📿 Daily scriptural wisdom");
        System.out.println("3. 💭 Quote for specific mood");
        System.out.println("4. 📚 Browse by category");
        System.out.println("5. 🔙 Back to main menu");
        
        System.out.print("\nSelect an option (1-5): ");
    }
}