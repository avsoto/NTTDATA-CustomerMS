package com.bankingSystem.customer_ms.controller;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        customerService.create(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id){
        Optional<Customer> currentCustomer = customerService.getById(id);

        return currentCustomer.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer customer){
        return customerService.getById(id)
                .map(existingCustomer -> {
                    customer.setCustomerId(id);
                    customerService.update(id, customer);
                    return new ResponseEntity<>(customer, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id) {
        return customerService.getById(id)
                .map(existingCustomer -> {
                    customerService.delete(id);
                    return new ResponseEntity<Customer>(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<Customer>(HttpStatus.NOT_FOUND));
    }


}
