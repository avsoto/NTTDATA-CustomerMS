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
     * <p>
     * This method will throw a {@link BusinessException} if any of the following conditions are not met:
     * <ul>
     *     <li>The first name is empty.</li>
     *     <li>The last name is empty.</li>
     *     <li>The DNI is not exactly 8 digits.</li>
     *     <li>The email format is invalid.</li>
     * </ul>
     * </p>
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
     * <p>
     * This method checks if the given DNI already exists in the database. If another customer with the same DNI
     * is found (and it is not the same customer being updated), a {@link BusinessException} will be thrown.
     * </p>
     */
    private void validateUniqueDni(String dni, Integer id) {
        customerRepository.findByDni(dni)
                .filter(existingCustomer -> !existingCustomer.getCustomerId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("A client with this DNI already exists.");
                });
    }

    /**
     * Validates that a given field is not empty.
     *
     * @param field the field to check.
     * @param errorMessage the error message to throw if the field is empty.
     * @throws BusinessException if the field is empty.
     * <p>
     * This method checks if the provided field is null or empty and throws a {@link BusinessException} with the
     * given error message if it is.
     * </p>
     */
    private void validateNotEmpty(String field, String errorMessage) {
        if (field == null || field.isEmpty()) {
            throw new BusinessException(errorMessage);
        }
    }

    /**
     * Validates that a given field matches a specific pattern.
     *
     * @param field the field to check.
     * @param pattern the regex pattern to validate the field against.
     * @param errorMessage the error message to throw if the field does not match the pattern.
     * @throws BusinessException if the field does not match the pattern.
     * <p>
     * This method checks if the provided field matches the specified regex pattern and throws a
     * {@link BusinessException} with the given error message if it does not.
     * </p>
     */
    private void validatePattern(String field, String pattern, String errorMessage) {
        if (field == null || !field.matches(pattern)) {
            throw new BusinessException(errorMessage);
        }
    }
}
