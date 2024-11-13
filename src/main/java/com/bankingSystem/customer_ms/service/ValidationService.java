package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
public class ValidationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public ValidationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateCustomerData(Customer customer) {
        validateRequiredFields(customer);
        validateDniFormat(customer.getDni());
        validateEmailFormat(customer.getEmail());
        validateUniqueDni(customer.getDni(), customer.getId());
    }

    private void validateRequiredFields(Customer customer) {
        validateField(customer.getFirstName(), "FirstName");
        validateField(customer.getLastName(), "LastName");
        validateField(customer.getDni(), "DNI");
        validateField(customer.getEmail(), "Email");
    }

    private static void validateField(String field, String fieldName) {
        if (field == null || field.isEmpty()) {
            throw new BusinessException(fieldName + " is required.");
        }
    }

    private void validateDniFormat(String dni) {
        if (!dni.matches("[0-9]{8}")) {
            throw new BusinessException("Invalid DNI format. It must contain exactly 8 digits.");
        }
    }

    private void validateEmailFormat(String email) {
        Predicate<String> isEmailValid = input -> input.matches("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+$");

        if (!isEmailValid.test(email)) {
            throw new BusinessException("Invalid email format. It must follow the following format 'user123@mail.com'");
        }
    }

    private void validateUniqueDni(String dni, Integer id) {
        Optional<Customer> existingCustomer = customerRepository.findByDni(dni);
        if (existingCustomer.isPresent() && !existingCustomer.get().getId().equals(id)) {
            throw new BusinessException("A client with this DNI already exists.");
        }
    }

}
