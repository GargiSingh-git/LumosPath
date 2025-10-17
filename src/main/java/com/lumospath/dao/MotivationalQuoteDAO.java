package com.lumospath.dao;

import com.lumospath.model.MotivationalQuote;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for MotivationalQuote operations
 */
public interface MotivationalQuoteDAO extends BaseDAO<MotivationalQuote, Integer> {
    
    /**
     * Find quotes by category
     * @param category The quote category
     * @return List of quotes in the category
     * @throws SQLException if database operation fails
     */
    List<MotivationalQuote> findByCategory(String category) throws SQLException;
    
    /**
     * Find quotes by author
     * @param author The quote author
     * @return List of quotes by the author
     * @throws SQLException if database operation fails
     */
    List<MotivationalQuote> findByAuthor(String author) throws SQLException;
    
    /**
     * Find quotes by source
     * @param source The quote source
     * @return List of quotes from the source
     * @throws SQLException if database operation fails
     */
    List<MotivationalQuote> findBySource(String source) throws SQLException;
    
    /**
     * Get a random quote
     * @return Random motivational quote
     * @throws SQLException if database operation fails
     */
    Optional<MotivationalQuote> findRandomQuote() throws SQLException;
    
    /**
     * Get a random quote from specific category
     * @param category The category to select from
     * @return Random quote from the category
     * @throws SQLException if database operation fails
     */
    Optional<MotivationalQuote> findRandomQuoteByCategory(String category) throws SQLException;
    
    /**
     * Search quotes by text content
     * @param searchText Text to search for
     * @return List of matching quotes
     * @throws SQLException if database operation fails
     */
    List<MotivationalQuote> searchQuotes(String searchText) throws SQLException;
    
    /**
     * Get all available categories
     * @return List of distinct categories
     * @throws SQLException if database operation fails
     */
    List<String> findAllCategories() throws SQLException;
    
    /**
     * Get all available authors
     * @return List of distinct authors
     * @throws SQLException if database operation fails
     */
    List<String> findAllAuthors() throws SQLException;
    
    /**
     * Get scriptural quotes (from religious texts)
     * @return List of scriptural quotes
     * @throws SQLException if database operation fails
     */
    List<MotivationalQuote> findScripturalQuotes() throws SQLException;
    
    /**
     * Get daily wisdom quote (could be based on date algorithm)
     * @return Daily wisdom quote
     * @throws SQLException if database operation fails
     */
    Optional<MotivationalQuote> getDailyWisdom() throws SQLException;
}