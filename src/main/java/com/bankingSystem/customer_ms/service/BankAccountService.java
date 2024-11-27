package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final RestTemplate restTemplate;

    /**
     * Checks if a customer has any active bank accounts.
     *
     * @param customerId the ID of the customer to check.
     * @return true if the customer has active accounts, false otherwise.
     * @throws BusinessException if there is an error connecting to the bank account service.
     */
    public boolean hasActiveAccounts(Integer customerId) {
        String url = "http://localhost:8081/accounts/customer/" + customerId + "/active";

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class);
            return Optional.ofNullable(response.getBody()).orElse(false);
        } catch (Exception e) {
            throw new BusinessException("Error connecting to bank account service: " + e.getMessage());
        }
    }
}
