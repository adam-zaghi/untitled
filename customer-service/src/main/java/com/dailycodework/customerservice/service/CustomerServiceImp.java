package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.entities.Customer;
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

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

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
        existingCustomer.setEmail(updatedCustomer.getEmail());
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
}