package com.bsupply.productdashboard.service;

import com.bsupply.productdashboard.dto.PageResponseDto;
import com.bsupply.productdashboard.dto.request.CustomerRequest;
import com.bsupply.productdashboard.dto.response.CustomerResponse;
import com.bsupply.productdashboard.entity.Customer;
import com.bsupply.productdashboard.exception.CustomerNotFoundException;
import com.bsupply.productdashboard.exception.DuplicateCustomerNameException;
import com.bsupply.productdashboard.factory.CustomerResponseFactory;
import com.bsupply.productdashboard.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    @CacheEvict(
            value = {"customers", "customerById"},
            allEntries = true)
    public void addCustomer(CustomerRequest customerRequest) {

        checkCustomerNameExist(customerRequest.name());
        log.info("Add customer with details {}", customerRequest);
        Customer customer = new Customer();
        customer.setName(customerRequest.name());
        customer.setDescription(customerRequest.description());
        customer.setLocation(customerRequest.location());
        customer.setEmail(customerRequest.email());
        customer.setPhoneNumber(customerRequest.phoneNumber());
        customerRepository.save(customer);
    }

    private void checkCustomerNameExist(String customerName) {

        if (customerRepository.existsByName(customerName)) {
            throw new DuplicateCustomerNameException(customerName);
        }
    }

    @Cacheable(value = "customerById", key = "customerId")
    public CustomerResponse getCustomerById(UUID customerId) {

        log.info("Fetch customer with id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDescription(),
                customer.getLocation(),
                customer.getEmail(),
                customer.getPhoneNumber());
    }

    @Cacheable(value = "customers")
    public PageResponseDto<CustomerResponse> getAllCustomers(Pageable pageable) {

        log.info("Fetch all customers");
        Page<CustomerResponse> customers = customerRepository.findAll(pageable)
                .map(c -> CustomerResponseFactory.getCustomerResponse(c));
        return PageResponseDto.wrapResponse(customers);
    }

    @CacheEvict(
            value = {"customers", "customerById"},
            allEntries = true)
    public CustomerResponse updateCustomer(UUID customerId, CustomerRequest customerRequest) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));

        log.info("Update customer with id: {}", customerId);
        customer.setName(customerRequest.name());
        customer.setDescription(customerRequest.description());
        customer.setPhoneNumber(customerRequest.phoneNumber());
        customer.setLocation(customerRequest.location());
        customer.setEmail(customerRequest.email());
        Customer saved = customerRepository.save(customer);
        return CustomerResponseFactory.getCustomerResponse(saved);
    }

    @CacheEvict(
            value = {"customers", "customerById"},
            allEntries = true)
    public void deleteCustomer(UUID customerId) {

        log.info("Attempt to delete customer with id: {}", customerId);
        customerRepository.deleteById(customerId);
        log.info("Customer with id: {} deleted", customerId);
    }
}
