package com.bankingSystem.customer_ms.service;


import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import com.bankingSystem.customer_ms.validators.CustomerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerValidator validationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should pass validation for valid customer data")
    void validateCustomerData_shouldPassForValidData() {
        Customer validCustomer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerRepository.findByDni("98765432")).thenReturn(Optional.empty());

        validationService.validateCustomerData(validCustomer);
    }

    @Test
    @DisplayName("Should throw an exception when the first name is empty")
    void validateCustomerData_shouldThrowExceptionForEmptyFirstName() {
        Customer invalidCustomer = Customer.builder()
                .customerId(1)
                .firstName("")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validationService.validateCustomerData(invalidCustomer));
        assert exception.getMessage().equals("FirstName is required.");
    }

    @Test
    @DisplayName("Should throw an exception when the last name is empty")
    void validateCustomerData_shouldThrowExceptionForEmptyLastName() {
        Customer invalidCustomer = Customer.builder()
                .customerId(1)
                .firstName("Ana") // First name is valid
                .lastName("")     // Last name is empty
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validationService.validateCustomerData(invalidCustomer));
        assert exception.getMessage().equals("LastName is required.");
    }

    @Test
    @DisplayName("Should throw an exception when the DNI is invalid")
    void validateCustomerData_shouldThrowExceptionForInvalidDni() {
        Customer invalidCustomer = Customer.builder()
                .customerId(1)
                .firstName("Ana")
                .lastName("Soto")
                .dni("123")
                .email("ana.soto@mail.com")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validationService.validateCustomerData(invalidCustomer));
        assert exception.getMessage().equals("Invalid DNI format. It must contain exactly 8 digits.");
    }

    @Test
    @DisplayName("Should throw an exception when the DNI is already registered")
    void validateCustomerData_shouldThrowExceptionForDuplicateDni() {

        Customer existingCustomer = Customer.builder()
                .customerId(1)
                .firstName("Luis")
                .lastName("Perez")
                .dni("12345678")
                .email("luis.perez@mail.com")
                .build();

        Customer newCustomer = Customer.builder()
                .customerId(2)
                .firstName("Ana")
                .lastName("Soto")
                .dni("12345678")
                .email("ana.soto@mail.com")
                .build();

        when(customerRepository.findByDni("12345678")).thenReturn(Optional.of(existingCustomer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validationService.validateCustomerData(newCustomer));
        assert exception.getMessage().equals("A client with this DNI already exists.");
    }



    @Test
    @DisplayName("Should throw an exception when a required field is null")
    void validateCustomerData_shouldThrowExceptionWhenFieldIsNull() {
        Customer invalidCustomer = Customer.builder()
                .customerId(1)
                .firstName(null)
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        BusinessException exception = assertThrows(BusinessException.class,
                () -> validationService.validateCustomerData(invalidCustomer));
        assert exception.getMessage().equals("FirstName is required.");
    }
}