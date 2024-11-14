package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements CrudService<Customer,Integer>{

    @Autowired
    private CustomerRepository customerRepository;
    private final ValidationService validationService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ValidationService validationService) {
        this.customerRepository = customerRepository;
        this.validationService = validationService;
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public Customer getById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Integer id, Customer customer) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer wasn't found with the DNI: " + id));


        customer.setDni(existingCustomer.getDni());
        customer.setId(existingCustomer.getId());

        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());

        customerRepository.save(existingCustomer);
    }

    @Override
    public void create(Customer customer) {
        validationService.validateCustomerData(customer);
        customerRepository.save(customer);
    }

    @Override
    public void delete(Integer id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with the ID" + id));

        customerRepository.delete(existingCustomer);
    }

}
