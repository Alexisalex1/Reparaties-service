package nl.backend.reparatieservice.service;

import nl.backend.reparatieservice.dto.CustomerDto;
import nl.backend.reparatieservice.dto.RepairRequestDto;
import nl.backend.reparatieservice.exception.CustomException;
import nl.backend.reparatieservice.exception.EntityNotFoundException;
import nl.backend.reparatieservice.model.Customer;
import nl.backend.reparatieservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RepairRequestService repairRequestService;

    @Autowired
    public CustomerService(
            CustomerRepository customerRepository,
            RepairRequestService repairRequestService
            ) {
        this.customerRepository = customerRepository;
        this.repairRequestService = repairRequestService;
    }

    private CustomerDto convertToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.customerId = customer.getCustomerId();
        customerDto.name = customer.getName();
        customerDto.email = customer.getEmail();
        customerDto.phoneNumber = customer.getPhoneNumber();
        return customerDto;
    }

    // Create a new customer
    public Customer createCustomer(CustomerDto customerDto) {
        try {
            Customer customer = new Customer();
            customer.setName(customerDto.name);
            customer.setEmail(customerDto.email);
            customer.setPhoneNumber(customerDto.phoneNumber);
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new CustomException("Failed to create customer", e);
        }
    }

    // Retrieve a customer by ID
    public CustomerDto getCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found."));
            return convertToCustomerDto(customer);
        }  catch (Exception e) {
            throw new CustomException("Failed to retrieve customer", e);
        }
    }

    // all customers
    public List<CustomerDto> getAllCustomers() {
        try {
            List<Customer> customers = (List<Customer>) customerRepository.findAll();
            return customers.stream()
                    .map(this::convertToCustomerDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve customers", e);
        }
    }

    // Update customer information
    public Customer updateCustomer(Long customerId, CustomerDto customerDto) throws EntityNotFoundException {
        try {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        if (customerDto.name != null) {
            customer.setName(customerDto.name);
        }
        if (customerDto.email != null) {
            customer.setEmail(customerDto.email);
        }
        if (customerDto.phoneNumber != null) {
            customer.setPhoneNumber(customerDto.phoneNumber);
        }
        return customerRepository.save(customer);
        } catch (Exception e) {
            throw new CustomException("Failed to update customer", e);
        }
    }

    // Delete a customer by ID
    public void deleteCustomer(Long customerId) throws EntityNotFoundException {
        try {
            if (customerRepository.existsById(customerId)) {
                customerRepository.deleteById(customerId);
            } else {
                throw new EntityNotFoundException("Customer not found");
            }
        } catch (Exception e) {
            throw new CustomException("Failed to delete customer", e);
        }
    }
    //customer with repair requests
    public CustomerDto getCustomerWithRepairRequests(Long customerId) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            List<RepairRequestDto> repairRequests = repairRequestService.getRepairRequestsByCustomerId(customerId);

            CustomerDto customerDto = convertToCustomerDto(customer);
            customerDto.setRepairRequests(repairRequests);

            return customerDto;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve customer with repair requests", e);
        }
    }
}