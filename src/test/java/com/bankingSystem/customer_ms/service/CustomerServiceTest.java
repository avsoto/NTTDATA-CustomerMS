package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void create() {
        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Gómez")
                .dni("98765432")
                .email("ana.gomez@mail.com")
                .build();

        doNothing().when(validationService).validateCustomerData(customer);
        when(customerRepository.save(customer)).thenReturn(customer); // Simula guardado

        // Method under test
        Customer result = customerService.create(customer);

        assertNotNull(result);

        assertEquals("Ana", result.getFirstName());
        assertEquals("Gómez", result.getLastName());
        assertEquals("98765432", result.getDni());
        assertEquals("ana.gomez@mail.com", result.getEmail());

        verify(validationService).validateCustomerData(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void getAll() {
        Customer customer1 = Customer.builder()
                .firstName("Ana")
                .lastName("Gómez")
                .dni("98765432")
                .email("ana.gomez@mail.com")
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
    public void getById_CustomerExists() {

        Customer customer = Customer.builder()
                .firstName("Ana")
                .lastName("Gómez")
                .dni("98765432")
                .email("ana.gomez@mail.com")
                .build();

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        // Method under test
        Optional<Customer> result = customerService.getById(1);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getFirstName());
        assertEquals("Gómez", result.get().getLastName());

        verify(customerRepository).findById(1);
    }

    @Test
    public void getById_CustomerNotFound() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // Method under test
        Optional<Customer> result = customerService.getById(999);

        assertFalse(result.isPresent());

        verify(customerRepository).findById(999);
    }
}