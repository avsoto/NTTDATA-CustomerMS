package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import com.bankingSystem.customer_ms.validators.CustomerValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerValidator validationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private BankAccountService bankAccountService;

    @Value("${bankAccountMicroserviceUrl}")
    private String bankAccountMicroserviceUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a customer and return the created customer")
    public void shouldCreateCustomerSuccessfully_WhenProvidedWithValidData() {
        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        doNothing().when(validationService).validateCustomerData(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        // Method under test
        Customer result = customerService.create(customer);

        assertNotNull(result);

        assertEquals("Ana", result.getFirstName());
        assertEquals("Soto", result.getLastName());
        assertEquals("98765432", result.getDni());
        assertEquals("ana.soto@mail.com", result.getEmail());

        verify(validationService).validateCustomerData(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    @DisplayName("Should return all customers when customers exist")
    public void getAll_ShouldReturnAllCustomers_WhenCustomersExist() {
        Customer customer1 = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        Customer customer2 = Customer.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .dni("12345678")
                .email("juan.perez@mail.com")
                .build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        List<Customer> result = customerService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getFirstName());
        assertEquals("Juan", result.get(1).getFirstName());

        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should return customer when customer exists")
    public void getById_ShouldReturnCustomer_WhenCustomerExists() {

        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        // Method under test
        Optional<Customer> result = customerService.getById(1);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getFirstName());
        assertEquals("Soto", result.get().getLastName());

        verify(customerRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when customer is not found")
    public void getById_ShouldReturnEmpty_WhenCustomerNotFound() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // Method under test
        Optional<Customer> result = customerService.getById(999);

        assertFalse(result.isPresent());

        verify(customerRepository).findById(999);
    }

    @Test
    @DisplayName("Should throw an exception when customer to update is not found")
    public void updateCustomer_ShouldThrowException_WhenCustomerNotFound() {
        Integer id = 1;

        Customer updatedCustomer = Customer.builder()
                .customerId(2)
                .firstName("Ana Victoria")
                .lastName("Soto Mejia")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> customerService.update(id, updatedCustomer)
        );

        assertEquals("Customer not found with id: " + id, exception.getMessage());

    }

    @Test
    @DisplayName("Should throw an exception when the customer has active accounts")
    void deleteCustomer_ShouldThrowException_WhenCustomerHasActiveAccounts() {
        Integer customerId = 1;

        ResponseEntity<Boolean> responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
        when(restTemplate.exchange(
                eq("http://localhost:8081/accounts" + "/customer/" + customerId + "/active"),
                eq(HttpMethod.GET),
                isNull(),
                eq(Boolean.class)
        )).thenReturn(responseEntity);

        assertThrows(BusinessException.class, () -> customerService.delete(customerId));

        verify(customerRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw an exception when there is a service error during customer deletion")
    public void deleteCustomer_ShouldThrowException_WhenServiceErrorOccurs() {
        Integer customerId = 1;

        when(customerRepository.findById(customerId)).thenThrow(new RuntimeException("Connection error"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> customerService.delete(customerId)
        );

        assertEquals("Exception: Connection error", exception.getMessage());
    }

    @Test
    void updateCustomer_Success() {
        // Arrange
        Integer customerId = 1;
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Updated Name");

        Mockito.when(customerRepository.existsById(customerId)).thenReturn(true);
        Mockito.when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        // Act
        Customer result = customerService.update(customerId, updatedCustomer);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Updated Name", result.getFirstName());
        Mockito.verify(customerRepository).save(updatedCustomer);
    }

    @Test
    void updateCustomer_NotFound_ThrowsException() {
        // Arrange
        Integer customerId = 1;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Updated Name");

        Mockito.when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act & Assert
        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> customerService.update(customerId, updatedCustomer));

        Assertions.assertEquals("Customer not found with id: " + customerId, exception.getMessage());
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteCustomer_NoActiveAccounts_Success() {
        // Arrange
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(bankAccountService.hasActiveAccounts(customerId)).thenReturn(false);

        // Act
        boolean result = customerService.delete(customerId);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_HasActiveAccounts_ThrowsException() {
        // Arrange
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(bankAccountService.hasActiveAccounts(customerId)).thenReturn(true);

        // Act & Assert
        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> customerService.delete(customerId));

        Assertions.assertEquals("Exception: Cannot delete customer with active accounts.", exception.getMessage());
        Mockito.verify(customerRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteCustomer_NotFound_ThrowsException() {
        // Arrange
        Integer customerId = 1;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> customerService.delete(customerId));

        Assertions.assertEquals(String.format("Exception: Customer with ID %d not found.", customerId), exception.getMessage());
        Mockito.verify(customerRepository, Mockito.never()).delete(Mockito.any());
    }

}



