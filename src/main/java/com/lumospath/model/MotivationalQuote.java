package com.lumospath.model;

/**
 * MotivationalQuote model class for storing inspirational quotes and affirmations
 */
public class MotivationalQuote {
    private int quoteId;
    private String quote;
    private String author;
    private QuoteSource source;
    private String category; // stress, depression, anxiety, general, etc.
    private String language; // en, hi, etc.

    // Constructors
    public MotivationalQuote() {}

    public MotivationalQuote(String quote, String author, QuoteSource source, String category) {
        this.quote = quote;
        this.author = author;
        this.source = source;
        this.category = category;
        this.language = "en"; // Default to English
    }

    // Getters and Setters
    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public QuoteSource getSource() {
        return source;
    }

    public void setSource(QuoteSource source) {
        this.source = source;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "\"" + quote + "\" - " + author + " (" + source.getDisplayName() + ")";
    }

    /**
     * Enum for different sources of quotes
     */
    public enum QuoteSource {
        BHAGAVAD_GITA("Bhagavad Gita"),
        SRIMAD_BHAGAVATAM("Srimad Bhagavatam"),
        MOTIVATIONAL_SPEAKER("Motivational Speaker"),
        INTERNET("Internet"),
        CUSTOM("Custom");

        private final String displayName;

        QuoteSource(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}