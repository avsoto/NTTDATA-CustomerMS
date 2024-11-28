package com.bankingSystem.customer_ms.repository;

import com.bankingSystem.customer_ms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Customer} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and more on the
 * {@link Customer} entity, without the need for explicit implementation. The repository
 * allows searching for customers by their DNI.
 * </p>
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Finds a customer by their DNI (unique identification number).
     *
     * @param dni the DNI of the customer.
     * @return an {@link Optional} containing the found customer, or an empty {@link Optional} if no customer with the given DNI exists.
     */
    Optional<Customer> findByDni(String dni);

}


