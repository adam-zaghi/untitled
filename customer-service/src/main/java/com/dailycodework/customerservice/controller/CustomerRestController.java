package com.dailycodework.customerservice.controller;

import com.dailycodework.customerservice.dto.CreateCustomerRequest;
import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.entities.Customer;

import com.dailycodework.customerservice.repository.AddressRepository;
import com.dailycodework.customerservice.service.AddressService;
import com.dailycodework.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/customers")
public class CustomerRestController {
    final private CustomerService customerService;
    final private AddressService addressService;
    @GetMapping
    ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    @GetMapping("/{id}")
    ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer savedCustomer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer
    ) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/addresses")
    public ResponseEntity<Address> addAddressToCustomer(
            @PathVariable Long id,
            @RequestBody Address address
    ) {
        Address savedAddress = customerService.addAddressToCustomer(id, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Address>> getCustomerAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerAddresses(id));
    }
    @GetMapping("/{id}/default-address")
    public ResponseEntity<Address> getDefaultAddress(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getDefaultAddress(id));
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomers() {
        return ResponseEntity.ok(customerService.getCustomerCount());
    }
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Customer> getCustomerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getCustomerByUserId(userId));
    }

}
