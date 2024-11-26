package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ValidationService validationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CustomerService customerService;

    @Value("${bankAccountMicroserviceUrl}")
    private String bankAccountMicroserviceUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create() {
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
    public void getAll() {
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
    public void getById_CustomerExists() {

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
    public void getById_CustomerNotFound() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // Method under test
        Optional<Customer> result = customerService.getById(999);

        assertFalse(result.isPresent());

        verify(customerRepository).findById(999);
    }

    @Test
    public void testUpdateCustomer_Success(){
        Integer id = 1;

        Customer existingCustomer = Customer.builder()
                .customerId(1)
                .firstName("Ana")
                .lastName("Soto")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        Customer updatedCustomer = Customer.builder()
                .customerId(null)
                .firstName("Ana Victoria")
                .lastName("Soto Mejia")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        //Method under test
        customerService.update(id, updatedCustomer);

        verify(customerRepository).findById(id);
        verify(customerRepository).save(existingCustomer); // Verifica que save fue llamado

        assertEquals("Ana Victoria", existingCustomer.getFirstName());
        assertEquals("Soto Mejia", existingCustomer.getLastName());
        assertEquals("ana.soto@mail.com", existingCustomer.getEmail());
    }

    @Test
    public void testUpdateCustomer_NotFound() {
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

        assertEquals("Customer wasn't found with ID: " + id, exception.getMessage());

    }

    @Test
    void testDeleteCustomer_WithActiveAccounts() {
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
    public void testDeleteCustomer_ServiceError() {
        Integer customerId = 1;

        when(restTemplate.exchange(
                eq("http://localhost:8081/accounts" + "/customer/" + customerId + "/active"),
                eq(HttpMethod.GET),
                isNull(),
                eq(Boolean.class))
        ).thenThrow(new RuntimeException("Connection error"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> customerService.delete(customerId)
        );

        assertEquals("Exception:Connection error", exception.getMessage());
    }

    @Test
    public void testDeleteCustomer_NotFound() {
        Integer customerId = 999;

        when(restTemplate.exchange(
                eq("http://localhost:8081/accounts" + "/customer/" + customerId + "/active"),
                eq(HttpMethod.GET),
                isNull(),
                eq(Boolean.class))
        ).thenReturn(ResponseEntity.ok(false));

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> customerService.delete(customerId)
        );

        assertEquals("Customer with ID 999 not found.", exception.getMessage());
    }

    @Test
    public void testHasActiveAccounts_ReturnsTrue() {
        Integer customerId = 1;
        String url = "http://localhost:8081/accounts" + "/customer/" + customerId + "/active";
        ResponseEntity<Boolean> response = new ResponseEntity<>(true, HttpStatus.OK);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), isNull(), eq(Boolean.class)))
                .thenReturn(response);

        boolean result = customerService.hasActiveAccounts(customerId);

        assertTrue(result, "Expected hasActiveAccounts to return true");
    }

    @Test
    public void testDeleteCustomer_Success() {
        Integer customerId = 1;
        Customer customer = Customer.builder()
                .customerId(1)
                .firstName("Ana Victoria")
                .lastName("Soto Mejia")
                .dni("98765432")
                .email("ana.soto@mail.com")
                .build();

        when(restTemplate.exchange(
                eq("http://localhost:8081/accounts" + "/customer/" + customerId + "/active"),
                eq(HttpMethod.GET),
                isNull(),
                eq(Boolean.class))
        ).thenReturn(new ResponseEntity<>(false, HttpStatus.OK));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.delete(customerId);

        verify(customerRepository).delete(customer);
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testHasActiveAccounts_ResponseIsNull() {
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";

        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.hasActiveAccounts(customerId);
        });

        assertEquals("Error connecting to bank account service: Response is null or invalid", exception.getMessage());
    }

    @Test
    void testHasActiveAccounts_ResponseBodyIsNull() {
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";

        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class)).thenReturn(ResponseEntity.ok(null));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.hasActiveAccounts(customerId);
        });

        assertEquals("Error connecting to bank account service: Response is null or invalid", exception.getMessage());
    }

    @Test
    void testHasActiveAccounts_BusinessExceptionRelanzada() {
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";

        BusinessException mockException = new BusinessException("Mocked BusinessException");
        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class)).thenThrow(mockException);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            customerService.hasActiveAccounts(customerId);
        });

        assertEquals("Mocked BusinessException", exception.getMessage());
    }
}