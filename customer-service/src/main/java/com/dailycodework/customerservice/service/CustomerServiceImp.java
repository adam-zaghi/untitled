package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.dto.CreateCustomerRequest;
import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.entities.Customer;
import com.dailycodework.customerservice.entities.User;
import com.dailycodework.customerservice.feign.AuthRestClient;
import com.dailycodework.customerservice.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthRestClient  authRestClient;

    @Override

    public Customer createCustomer(CreateCustomerRequest request) {

        if (request.getUserEmail() == null || request.getUserEmail().isBlank()) {
            throw new RuntimeException("User email is required");
        }

        User user = authRestClient.getUserByEmail(request.getUserEmail());

        if (user == null || user.getId() == null) {
            throw new RuntimeException("User not found with email: " + request.getUserEmail());
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("User account is disabled");
        }

        if (customerRepository.existsByUserId(user.getId())) {
            throw new RuntimeException("Customer profile already exists for user: " + user.getEmail());
        }

        if (!"USER".equals(user.getRole()) && !"CUSTOMER".equals(user.getRole())) {
            throw new RuntimeException("Only USER or CUSTOMER accounts can become customers");
        }

        Customer customer = Customer.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (request.getDefaultAddress() != null) {
            Address address = Address.builder()
                    .street(request.getDefaultAddress().getStreet())
                    .city(request.getDefaultAddress().getCity())
                    .state(request.getDefaultAddress().getState())
                    .postalCode(request.getDefaultAddress().getPostalCode())
                    .country(request.getDefaultAddress().getCountry())
                    .isDefault(true)
                    .latitude(request.getDefaultAddress().getLatitude())
                    .longitude(request.getDefaultAddress().getLongitude())
                    .customer(customer)
                    .build();

            customer.getAddresses().add(address);
        }

        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findWithAddressesById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    @Override
    public Address addAddressToCustomer(Long customerId, Address address) {
        Customer customer = getCustomerById(customerId);

        address.setCustomer(customer);

        if (Boolean.TRUE.equals(address.getIsDefault())) {//si l'address est par defaut
            customer.getAddresses().forEach(a -> a.setIsDefault(false));
        }//on enleve le status par defaut aux ancien avant l'ajout

        customer.getAddresses().add(address);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);

        return address;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getCustomerAddresses(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return customer.getAddresses();
    }

    @Override
    @Transactional(readOnly = true)
    public Address getDefaultAddress(Long customerId) {
        Customer customer = getCustomerById(customerId);

        return customer.getAddresses()
                .stream()
                .filter(address -> Boolean.TRUE.equals(address.getIsDefault()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No default address found for customer id: " + customerId));
    }



    @Override
    public Long getCustomerCount() {
        return customerRepository.count();
    }

    @Override
    public Customer getCustomerByUserId(Long id) {
        return customerRepository.findByUserId(id);
    }
}