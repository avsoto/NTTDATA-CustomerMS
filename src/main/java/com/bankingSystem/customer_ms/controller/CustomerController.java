package com.bankingSystem.customer_ms.controller;

import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for handling HTTP requests related to customer operations.
 * <p>
 * This class provides endpoints to perform CRUD (Create, Read, Update, Delete) operations
 * on {@link Customer} objects. It interacts with the {@link CustomerService} to manage customer data.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Handles GET requests to fetch all customers.
     *
     * @return a {@link ResponseEntity} containing a list of all customers and an HTTP status of OK.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    /**
     * Handles POST requests to create a new customer.
     *
     * @param customer the {@link Customer} object to be created.
     * @return a {@link ResponseEntity} containing the created customer and an HTTP status of CREATED.
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.create(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Handles GET requests to fetch a specific customer by ID.
     *
     * @param id the ID of the customer to be fetched.
     * @return a {@link ResponseEntity} containing the found customer, or a NOT_FOUND status if no customer is found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id){
        Optional<Customer> currentCustomer = customerService.getById(id);

        return currentCustomer.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Handles PUT requests to update an existing customer's information.
     *
     * @param id the ID of the customer to be updated.
     * @param customer the {@link Customer} object containing the updated information.
     * @return a {@link ResponseEntity} containing the updated customer, or a NOT_FOUND status if the customer doesn't exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer customer){
        Customer updatedCustomer = customerService.update(id, customer); // El servicio valida y actualiza el cliente
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    /**
     * Handles DELETE requests to remove a customer by ID.
     *
     * @param id the ID of the customer to be deleted.
     * @return a {@link ResponseEntity} with an HTTP status of OK if the customer was deleted, or NOT_FOUND if no customer exists with the provided ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Integer id) {
        boolean deleted = customerService.delete(id); // El servicio maneja la eliminación del cliente
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
