import com.lumospath.service.MotivationalQuoteService;
import com.lumospath.service.EmergencyHelplineService;
import com.lumospath.model.MoodType;
import com.lumospath.model.MotivationalQuote;

/**
 * Demo class to showcase LumosPath functionality
 */
public class demo {
    public static void main(String[] args) {
        System.out.println("🌟 LumosPath - Mental Health Support Application Demo");
        System.out.println("=====================================================");
        System.out.println();
        
        // Demo Mood Types
        System.out.println("📊 Available Mood Types:");
        for (MoodType mood : MoodType.values()) {
            System.out.println("  " + mood + " - " + mood.getDescription());
        }
        System.out.println();
        
        // Demo Motivational Quotes
        System.out.println("💫 Sample Motivational Quotes:");
        MotivationalQuoteService quoteService = new MotivationalQuoteService();
        
        for (int i = 0; i < 3; i++) {
            MotivationalQuote quote = quoteService.getRandomQuote();
            System.out.println("  \"" + quote.getQuote() + "\"");
            System.out.println("    - " + quote.getAuthor() + " (" + quote.getSource().getDisplayName() + ")");
            System.out.println();
        }
        
        // Demo scriptural wisdom
        System.out.println("📿 Scriptural Wisdom:");
        MotivationalQuote scripturalQuote = quoteService.getScripturalQuote();
        System.out.println("  \"" + scripturalQuote.getQuote() + "\"");
        System.out.println("    - " + scripturalQuote.getAuthor() + " (" + scripturalQuote.getSource().getDisplayName() + ")");
        System.out.println();
        
        // Demo Emergency Helplines
        System.out.println("📞 Emergency Mental Health Support:");
        System.out.println("  🚨 Vandrevala Foundation: 1860-266-2345 (24x7)");
        System.out.println("  🚨 Kiran Mental Health: 1800-599-0019 (24x7)");
        System.out.println("  🚨 Sneha Foundation: 044-24640050 (24x7)");
        System.out.println();
        
        System.out.println("✨ LumosPath Features:");
        System.out.println("  🏠 Beautiful GUI with Welcome Screen");
        System.out.println("  📊 Interactive Mood Tracking with Visual Selectors");
        System.out.println("  💫 Inspirational Quotes from Scriptures & Speakers");
        System.out.println("  🤖 LumosBot AI Chatbot for Emotional Support");
        System.out.println("  📞 Comprehensive Indian Mental Health Helplines");
        System.out.println("  🔒 Privacy-Protected (Anonymous Usage Available)");
        System.out.println();
        
        System.out.println("💙 Remember: You are stronger than you think, and you are not alone!");
        System.out.println();
        System.out.println("To run the full GUI application:");
        System.out.println("  ./run-gui.sh");
        System.out.println();
        System.out.println("Note: GUI requires a display environment. Perfect for desktop/laptop usage!");
    }
}