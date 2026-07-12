package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.entities.Customer;


import java.util.List;


public interface CustomerService {
    Customer createCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    Address addAddressToCustomer(Long customerId, Address address);

    List<Address> getCustomerAddresses(Long customerId);

    Address getDefaultAddress(Long customerId);



}
