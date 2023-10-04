package nl.backend.reparatieservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import nl.backend.reparatieservice.dto.CustomerDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.Customer;
import nl.backend.reparatieservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create a new customer
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto) {
        if (customerDto == null || customerDto.name == null || customerDto.email == null || customerDto.phoneNumber == null) {
            return ResponseEntity.badRequest().body("Invalid customer data");
        } try {
            Customer createdCustomer = customerService.createCustomer(customerDto);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create customer: " + e.getMessage());
        }
    }

    // Retrieve a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            CustomerDto customerDto = customerService.getCustomerById(id);
            if (customerDto != null) {
                return new ResponseEntity<>(customerDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customer: " + e.getMessage());
        }
    }

    // Retrieve all customers
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<CustomerDto> customerDtos = customerService.getAllCustomers();
            return new ResponseEntity<>(customerDtos, HttpStatus.OK);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customers: " + e.getMessage());
        }
    }

    // Update customer information by id
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDto customerDto
    ) {
        try {
            Customer updated = customerService.updateCustomer(id, customerDto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update customer: " + e.getMessage());
        }
    }

    // Delete a customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete customer: " + e.getMessage());
        }
    }

    // retrieve customer with repair requests
    @GetMapping("/{id}/requests")
    public ResponseEntity<?> getCustomerWithRepairRequests(@PathVariable Long id) {
        try {
            CustomerDto customerDto = customerService.getCustomerWithRepairRequests(id);
            if (customerDto != null) {
                return new ResponseEntity<>(customerDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve customer with repair requests: " + e.getMessage());
        }
    }
}