package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

/**
 * Service class responsible for validating customer data.
 * <p>
 * This class contains methods to validate various fields of a {@link Customer} entity,
 * such as ensuring required fields are not empty, checking the format of the DNI (identity number)
 * and email, and ensuring the uniqueness of the DNI.
 * </p>
 */
@Service
public class ValidationService {

    private final CustomerRepository customerRepository;

    /**
     * Constructs a new instance of {@link ValidationService}.
     *
     * @param customerRepository the {@link CustomerRepository} used to check if a customer with a given DNI already exists.
     */
    @Autowired
    public ValidationService(CustomerRepository customerRepository) {
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
        validate(customer.getFirstName(), field -> !field.isEmpty(), "FirstName is required.");
        validate(customer.getLastName(), field -> !field.isEmpty(), "LastName is required.");
        validate(customer.getDni(), dni -> dni.matches("[0-9]{8}"), "Invalid DNI format. It must contain exactly 8 digits.");
        validate(customer.getEmail(), email -> email.matches("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+$"), "Invalid email format. It must follow the format 'user123@mail.com'");
        validateUniqueDni(customer.getDni(), customer.getCustomerId());
    }

    /**
     * A generic method that validates a given field against a specified predicate.
     * <p>
     * If the field is invalid (either null or not passing the predicate test), a {@link BusinessException} is thrown with the provided error message.
     * </p>
     *
     * @param <T> the type of the field being validated.
     * @param field the field to validate.
     * @param predicate the predicate that defines the validation logic.
     * @param errorMessage the error message to throw if the validation fails.
     * @throws BusinessException if the field does not pass the validation.
     */
    private <T> void validate(T field, Predicate<T> predicate, String errorMessage) {
        if (field == null || !predicate.test(field)) {
            throw new BusinessException(errorMessage);
        }
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
}
