package com.dailycodework.orderservice.feign;

import com.dailycodework.orderservice.entitites.Address;
import com.dailycodework.orderservice.entitites.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.web.PagedModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//Client rest qui va me donner les methode pour commu avec customer
//openfeign appelle automatiquement discoery service pour recup l'adress avec le nom
@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/api/customers/{id}")
    Customer getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/api/customers")
    PagedModel<Customer> getCustomers();

    @GetMapping("/api/customers/{id}/address")
    List<Address> getCustomerAddresses(@PathVariable("id") Long id);
    @GetMapping("/api/address/{id}")
    Address getAddressById(@PathVariable("id") Long id);
}
