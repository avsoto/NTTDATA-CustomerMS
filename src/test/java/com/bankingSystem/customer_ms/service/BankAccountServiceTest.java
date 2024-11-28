package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BankAccountService bankAccountService;

    public BankAccountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return true when customer has active accounts")
    void hasActiveAccounts_CustomerHasActiveAccounts_ReturnsTrue() {
        // Arrange
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";
        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class))
                .thenReturn(ResponseEntity.ok(true));

        // Act
        boolean result = bankAccountService.hasActiveAccounts(customerId);

        // Assert
        assertTrue(result);
        verify(restTemplate, times(1)).exchange(url, HttpMethod.GET, null, Boolean.class);
    }

    @Test
    @DisplayName("Should return false when customer has no active accounts")
    void hasActiveAccounts_CustomerHasNoActiveAccounts_ReturnsFalse() {
        // Arrange
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";
        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class))
                .thenReturn(ResponseEntity.ok(false));

        // Act
        boolean result = bankAccountService.hasActiveAccounts(customerId);

        // Assert
        assertFalse(result);
        verify(restTemplate, times(1)).exchange(url, HttpMethod.GET, null, Boolean.class);
    }

    @Test
    @DisplayName("Should throw BusinessException when an exception occurs in RestTemplate")
    void hasActiveAccounts_RestTemplateThrowsException_ThrowsBusinessException() {
        // Arrange
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";
        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                bankAccountService.hasActiveAccounts(customerId));

        assertEquals("Error connecting to bank account service: Service unavailable", exception.getMessage());
        verify(restTemplate, times(1)).exchange(url, HttpMethod.GET, null, Boolean.class);
    }

    @Test
    @DisplayName("Should return false when response body is null")
    void hasActiveAccounts_ResponseBodyIsNull_ReturnsFalse() {
        // Arrange
        Integer customerId = 123;
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";
        when(restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class))
                .thenReturn(ResponseEntity.ok(null));

        // Act
        boolean result = bankAccountService.hasActiveAccounts(customerId);

        // Assert
        assertFalse(result);
        verify(restTemplate, times(1)).exchange(url, HttpMethod.GET, null, Boolean.class);
    }
}
