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
     * <p>
     * This method is used to create and persist a new entity of type {@link T}.
     * </p>
     */
    T create(T t);

    /**
     * Updates an existing entity.
     *
     * @param id the identifier of the entity to be updated.
     * @param t the updated entity data.
     * @return the updated entity.
     * <p>
     * This method updates an entity of type {@link T} using the provided identifier {@link I}.
     * </p>
     */
    T update(I id, T t);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to be deleted.
     * @return {@code true} if the entity was successfully deleted, {@code false} otherwise.
     * <p>
     * This method removes an entity of type {@link T} from the data store based on its identifier {@link I}.
     * </p>
     */
    boolean delete(I id);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id the identifier of the entity.
     * @return an {@link Optional} containing the entity, or an empty {@link Optional} if no entity with the given identifier exists.
     * <p>
     * This method retrieves an entity of type {@link T} from the data store using the provided identifier {@link I}.
     * </p>
     */
    Optional<T> getById(I id);

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities.
     * <p>
     * This method fetches a list of all entities of type {@link T} from the data store.
     * </p>
     */
    List<T> getAll();
}

