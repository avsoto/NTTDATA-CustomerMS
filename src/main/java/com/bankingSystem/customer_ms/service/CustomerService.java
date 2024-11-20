package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements CrudService<Customer,Integer>{

    @Value("${bankaccount.ms.url}")
    private String bankAccountMicroserviceUrl;

    private CustomerRepository customerRepository;
    private final ValidationService validationService;
    private final RestTemplate restTemplate;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void update(Integer id, Customer customer) {
        customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setFirstName(customer.getFirstName());
                    existingCustomer.setLastName(customer.getLastName());
                    existingCustomer.setEmail(customer.getEmail());
                    return existingCustomer;
                })
                .map(customerRepository::save)
                .orElseThrow(() -> new BusinessException("Customer wasn't found with ID: " + id));

    }

    @Override
    public void create(Customer customer) {
        validationService.validateCustomerData(customer);
        customerRepository.save(customer);
    }

    @Override
    public void delete(Integer customerId) {
        if (hasActiveAccounts(customerId)) {
            throw new BusinessException("Cannot delete customer with active accounts.");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
        } else {
            throw new BusinessException("Customer with ID " + customerId + " not found.");
        }

    }

    private boolean hasActiveAccounts(Integer customerId) {
        String url = bankAccountMicroserviceUrl + "/customer/" + customerId + "/active";
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, null, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            throw new BusinessException("Error connecting to bank account service: " + e.getMessage());
        }
    }

}
