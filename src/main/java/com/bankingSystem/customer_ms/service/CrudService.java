package com.bankingSystem.customer_ms.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic service interface providing CRUD (Create, Read, Update, Delete) operations for entities.
 * <p>
 * This interface defines basic CRUD methods that can be applied to any type of entity.
 * The generic type {@link T} represents the entity, and {@link I} represents the type of its identifier.
 * </p>
 *
 * @param <T> the type of the entity.
 * @param <I> the type of the entity's identifier.
 */
public interface CrudService<T, I> {

    /**
     * Creates a new entity.
     *
     * @param t the entity to be created.
     * @return the created entity.
     */
    T create(T t);

    /**
     * Updates an existing entity.
     *
     * @param id the identifier of the entity to be updated.
     * @param t the updated entity data.
     */
    void update(I id, T t);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to be deleted.
     */
    void delete(I id);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the identifier of the entity.
     * @return an {@link Optional} containing the entity, or an empty {@link Optional} if no entity with the given identifier exists.
     */
    Optional<T> getById(I id);

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities.
     */
    List<T> getAll();
}
