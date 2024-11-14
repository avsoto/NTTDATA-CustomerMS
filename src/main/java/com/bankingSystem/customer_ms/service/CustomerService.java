package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
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
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getById(Integer id) {
        return customerRepository.findById(id);  // Devuelve un Optional<Customer>
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
    public void delete(Integer id) {
        getById(id).ifPresentOrElse(
                customerRepository::delete, // Si el cliente existe, lo elimina
                () -> { throw new BusinessException("Customer not found with ID: " + id); } // Si no existe, lanza la excepción
        );
    }

}
