package com.bankingSystem.customer_ms.controller;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void testGetAllCustomers() {
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
    void testCreateCustomer() {
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
    void testGetCustomerById_Found() {
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
    void testGetCustomerById_NotFound() {
        when(customerService.getById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerController.getCustomerById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, times(1)).getById(1);
    }

    @Test
    void testPutCustomer_Found() {
        Customer existingCustomer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        Customer updatedCustomer = Customer.builder()
                .firstName("Victoria")
                .lastName("Mejía")
                .dni("12345678")
                .email("victoria.mejia@mail.com")
                .build();

        when(customerService.getById(1)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerService).update(1, updatedCustomer);

        ResponseEntity<Customer> response = customerController.putCustomer(1, updatedCustomer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomer, response.getBody());
        verify(customerService, times(1)).getById(1);
        verify(customerService, times(1)).update(1, updatedCustomer);
    }

    @Test
    void testPutCustomer_NotFound() {
        Customer updatedCustomer = Customer.builder()
                .firstName("Victoria")
                .lastName("Mejía")
                .dni("12345678")
                .email("victoria.mejia@mail.com")
                .build();

        when(customerService.getById(1)).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.putCustomer(1, updatedCustomer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, times(1)).getById(1);
        verify(customerService, times(0)).update(anyInt(), any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Found() {
        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerService.getById(1)).thenReturn(Optional.of(customer));
        doNothing().when(customerService).delete(1);

        ResponseEntity<Customer> response = customerController.deleteCustomer(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerService, times(1)).getById(1);
        verify(customerService, times(1)).delete(1);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerService.getById(1)).thenReturn(Optional.empty());

        ResponseEntity<Customer> response = customerController.deleteCustomer(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService, times(1)).getById(1);
        verify(customerService, times(0)).delete(anyInt());
    }




}