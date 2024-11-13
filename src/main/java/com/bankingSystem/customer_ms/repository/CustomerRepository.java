package com.bankingSystem.customer_ms.repository;

import com.bankingSystem.customer_ms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByDni(String dni);

}


