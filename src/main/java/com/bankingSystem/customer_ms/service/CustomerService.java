package com.bankingSystem.customer_ms.service;

import com.bankingSystem.customer_ms.exceptions.BusinessException;
import com.bankingSystem.customer_ms.model.Customer;
import com.bankingSystem.customer_ms.repository.CustomerRepository;
import com.bankingSystem.customer_ms.validators.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing {@link Customer} entities.
 * <p>
 * This class provides the business logic for handling customer operations, such as creating,
 * updating, retrieving, and deleting customers. It interacts with the {@link CustomerRepository}
 * for database operations and performs additional business logic like validation and checking for
 * active bank accounts before allowing deletion of a customer.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomerService implements CrudService<Customer,Integer>{

    @Value("${bankaccount.ms.url}")
    private String bankAccountMicroserviceUrl;

    private final CustomerRepository customerRepository;
    private final CustomerValidator customerValidator;
    private final BankAccountService bankAccountService;
    private final RestTemplate restTemplate;

    /**
     * Retrieves all customers.
     *
     * @return a list of all {@link Customer} entities.
     */
    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves a customer by its ID.
     *
     * @param id the ID of the customer to retrieve.
     * @return an {@link Optional} containing the customer, or an empty {@link Optional} if no customer
     *         is found with the given ID.
     */
    @Override
    public Optional<Customer> getById(Integer id) {
        return customerRepository.findById(id);
    }

    /**
     * Updates an existing customer.
     *
     * @param id the ID of the customer to update.
     * @param customer the updated {@link Customer} data.
     * @throws BusinessException if the customer with the given ID does not exist.
     */
    @Override
    public Customer update(Integer id, Customer customer) {
        customerValidator.validateCustomerData(customer);

        if (customerRepository.existsById(id)) {
            customer.setCustomerId(id);
            return customerRepository.save(customer);
        } else {
            throw new BusinessException("Customer not found with id: " + id);
        }
    }


    /**
     * Creates a new customer.
     *
     * @param customer the {@link Customer} to create.
     * @return the created {@link Customer}.
     */
    @Override
    public Customer create(Customer customer) {
        customerValidator.validateCustomerData(customer);
        return customerRepository.save(customer);
    }

    /**
     * Deletes a customer by its ID.
     * <p>
     * A customer can only be deleted if they have no active bank accounts.
     * </p>
     *
     * @param customerId the ID of the customer to delete.
     * @return
     * @throws BusinessException if the customer has active accounts or is not found.
     */
    @Override
    public boolean delete(Integer customerId) {
        customerRepository.findById(customerId).filter(customer -> !bankAccountService.hasActiveAccounts(customerId))
                .ifPresentOrElse(
                        customer -> customerRepository.delete(customer),
                        () -> {
                            if (bankAccountService.hasActiveAccounts(customerId)) {
                                throw new BusinessException("Cannot delete customer with active accounts.");
                            }
                            throw new BusinessException(String.format("Customer with ID %d not found.", customerId));
                        });
        return false;
    }
}
