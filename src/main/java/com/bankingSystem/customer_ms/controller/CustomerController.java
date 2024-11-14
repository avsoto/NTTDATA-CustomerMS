package com.bankingSystem.customer_ms.controller;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id){
        Customer currentCustomer = customerService.getById(id);
        if( currentCustomer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(currentCustomer, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Customer> putCustomer(@RequestBody Customer customer){
        if (customerService.getById(customer.getId()) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        customerService.update(customer.getId(), customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id){
        if( customerService.getById(id) == null ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        customerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
