package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService{

    @Autowired
    private CustomerRepository customerRepository;
    private final ValidationService validationService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ValidationService validationService) {
        this.customerRepository = customerRepository;
        this.validationService = validationService;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream().collect(Collectors.toList());
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> updateCustomerById(int id, Customer customer) {
        validationService.validateCustomerData(customer);

        return customerRepository.findById(id).map(existingCustomer -> {
            customer.setId(id);
            return customerRepository.save(customer);
        });
    }

    public Customer createCustomer(Customer customer) {
        validationService.validateCustomerData(customer);
        return customerRepository.save(customer);
    }

    public Optional<Customer> deleteCustomerById(int id) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    customerRepository.delete(existingCustomer);  // Delete customer
                    return existingCustomer;
                });
    }






}
