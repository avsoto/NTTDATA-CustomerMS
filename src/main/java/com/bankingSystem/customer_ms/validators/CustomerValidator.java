package com.bankingSystem.customer_ms.validators;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for validating customer data.
 * <p>
 * This class contains methods to validate various fields of a {@link Customer} entity,
 * such as ensuring required fields are not empty, checking the format of the DNI (identity number)
 * and email, and ensuring the uniqueness of the DNI.
 * </p>
 */
@Service
public class CustomerValidator {

    private final CustomerRepository customerRepository;

    /**
     * Constructs a new instance of {@link CustomerValidator}.
     *
     * @param customerRepository the {@link CustomerRepository} used to check if a customer with a given DNI already exists.
     */
    @Autowired
    public CustomerValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Validates the customer data to ensure required fields are populated and follow the correct formats.
     * <p>
     * This method checks the following:
     * - First Name: Must not be empty.
     * - Last Name: Must not be empty.
     * - DNI: Must follow the pattern of exactly 8 digits.
     * - Email: Must follow the format "user123@mail.com".
     * </p>
     *
     * @param customer the {@link Customer} object whose data is to be validated.
     * @throws BusinessException if any validation fails.
     */
    public void validateCustomerData(Customer customer) {
        validateNotEmpty(customer.getFirstName(), "FirstName is required.");
        validateNotEmpty(customer.getLastName(), "LastName is required.");
        validatePattern(customer.getDni(), "[0-9]{8}", "Invalid DNI format. It must contain exactly 8 digits.");
        validatePattern(customer.getEmail(), "^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+$", "Invalid email format.");
        validateUniqueDni(customer.getDni(), customer.getCustomerId());
    }

    /**
     * Validates that the given DNI is unique.
     * <p>
     * This method checks if the DNI already exists in the database for a different customer. If a customer with
     * the same DNI is found, a {@link BusinessException} is thrown.
     * </p>
     *
     * @param dni the DNI to validate.
     * @param id the ID of the customer being updated, to allow the same DNI for an existing customer.
     * @throws BusinessException if a customer with the same DNI already exists (and it is not the same customer being updated).
     */
    private void validateUniqueDni(String dni, Integer id) {
        customerRepository.findByDni(dni)
                .filter(existingCustomer -> !existingCustomer.getCustomerId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("A client with this DNI already exists.");
                });
    }

    private void validateNotEmpty(String field, String errorMessage) {
        if (field == null || field.isEmpty()) {
            throw new BusinessException(errorMessage);
        }
    }

    private void validatePattern(String field, String pattern, String errorMessage) {
        if (field == null || !field.matches(pattern)) {
            throw new BusinessException(errorMessage);
        }
    }
}
