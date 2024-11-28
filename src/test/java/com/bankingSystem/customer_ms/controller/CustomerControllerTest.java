package com.bankingSystem.customer_ms.controller;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    public CustomerControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a list of customers when successful")
    void getAllCustomers_ShouldReturnListOfCustomers_WhenSuccessful() {
        List<Customer> mockCustomers = Arrays.asList(
                Customer.builder()
                        .firstName("Ana")
                        .lastName("Soto")
                        .dni("98765432")
                        .email("ana.soto@mail.com")
                        .build(),
                Customer.builder()
                        .firstName("Victoria")
                        .lastName("Mejía")
                        .dni("12345678")
                        .email("victoria.mejia@mail.com")
                        .build()
        );

        when(customerService.getAll()).thenReturn(mockCustomers);

        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(customerService, times(1)).getAll();
    }

    @Test
    @DisplayName("Should return the created customer when successful")
    void createCustomer_ShouldReturnCreatedCustomer_WhenSuccessful() {
        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerService.create(customer)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).create(customer);
    }

    @Test
    @DisplayName("Should return the customer when found by ID")
    void getCustomerById_ShouldReturnCustomer_WhenFound() {
        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerService.getById(1)).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = customerController.getCustomerById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
        verify(customerService, times(1)).getById(1);
    }

    @Test
    @DisplayName("Should return NotFound when the customer is not found by ID")
    void getCustomerById_ShouldReturnNotFound_WhenCustomerNotFound() {
        when(customerService.getById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerController.getCustomerById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, times(1)).getById(1);
    }

    @Test
    void putCustomer_Success() {
        // Arrange
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setFirstName("John Doe");
        customer.setEmail("john.doe@example.com");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setFirstName("John Doe Updated");
        updatedCustomer.setEmail("john.doe.updated@example.com");

        Mockito.when(customerService.update(customerId, customer)).thenReturn(updatedCustomer);

        // Act
        ResponseEntity<Customer> response = customerController.putCustomer(customerId, customer);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(updatedCustomer, response.getBody());
        Mockito.verify(customerService).update(customerId, customer);
    }

    @Test
    void deleteCustomer_Success() {
        // Arrange
        Integer customerId = 1;

        Mockito.when(customerService.delete(customerId)).thenReturn(true);

        // Act
        ResponseEntity<Customer> response = customerController.deleteCustomer(customerId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(customerService).delete(customerId);
    }

    @Test
    void deleteCustomer_NotFound() {
        // Arrange
        Integer customerId = 1;

        Mockito.when(customerService.delete(customerId)).thenReturn(false);

        // Act
        ResponseEntity<Customer> response = customerController.deleteCustomer(customerId);

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Mockito.verify(customerService).delete(customerId);
    }

    @Test
    void customerExists_CustomerExists_ReturnsTrue() {
        // Arrange
        Integer customerId = 1;
        Customer mockCustomer = new Customer();
        when(customerService.getById(customerId)).thenReturn(Optional.of(mockCustomer));

        // Act
        ResponseEntity<Boolean> response = customerController.customerExists(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(customerService, times(1)).getById(customerId);
    }

    @Test
    void customerExists_CustomerDoesNotExist_ReturnsFalse() {
        // Arrange
        Integer customerId = 2;
        when(customerService.getById(customerId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Boolean> response = customerController.customerExists(customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(customerService, times(1)).getById(customerId);
    }
}
