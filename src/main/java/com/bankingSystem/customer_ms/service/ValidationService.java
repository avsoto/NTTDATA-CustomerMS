package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class ValidationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public ValidationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateCustomerData(Customer customer) {
        validate(customer.getFirstName(), field -> !field.isEmpty(), "FirstName is required.");
        validate(customer.getLastName(), field -> !field.isEmpty(), "LastName is required.");
        validate(customer.getDni(), dni -> dni.matches("[0-9]{8}"), "Invalid DNI format. It must contain exactly 8 digits.");
        validate(customer.getEmail(), email -> email.matches("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+$"), "Invalid email format. It must follow the format 'user123@mail.com'");
        validateUniqueDni(customer.getDni(), customer.getCustomerId());
    }

    private <T> void validate(T field, Predicate<T> predicate, String errorMessage) {
        if (field == null || !predicate.test(field)) {
            throw new BusinessException(errorMessage);
        }
    }

    private void validateUniqueDni(String dni, Integer id) {
        customerRepository.findByDni(dni)
                .filter(existingCustomer -> !existingCustomer.getCustomerId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("A client with this DNI already exists.");
                });
    }

}
