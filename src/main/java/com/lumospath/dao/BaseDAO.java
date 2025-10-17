package com.lumospath.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Base Data Access Object interface defining common CRUD operations
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface BaseDAO<T, ID> {
    
    /**
     * Save an entity to the database
     * @param entity The entity to save
     * @return The saved entity with generated ID
     * @throws SQLException if database operation fails
     */
    T save(T entity) throws SQLException;
    
    /**
     * Update an existing entity in the database
     * @param entity The entity to update
     * @return The updated entity
     * @throws SQLException if database operation fails
     */
    T update(T entity) throws SQLException;
    
    /**
     * Find an entity by its ID
     * @param id The entity ID
     * @return Optional containing the entity if found
     * @throws SQLException if database operation fails
     */
    Optional<T> findById(ID id) throws SQLException;
    
    /**
     * Find all entities
     * @return List of all entities
     * @throws SQLException if database operation fails
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Delete an entity by its ID
     * @param id The entity ID
     * @return true if entity was deleted, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean deleteById(ID id) throws SQLException;
    
    /**
     * Delete an entity
     * @param entity The entity to delete
     * @return true if entity was deleted, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean delete(T entity) throws SQLException;
    
    /**
     * Check if an entity exists by ID
     * @param id The entity ID
     * @return true if entity exists, false otherwise
     * @throws SQLException if database operation fails
     */
    boolean existsById(ID id) throws SQLException;
    
    /**
     * Count total number of entities
     * @return Total count
     * @throws SQLException if database operation fails
     */
    long count() throws SQLException;
    
    /**
     * Save multiple entities in a batch operation
     * @param entities List of entities to save
     * @return List of saved entities
     * @throws SQLException if database operation fails
     */
    List<T> saveAll(List<T> entities) throws SQLException;
}