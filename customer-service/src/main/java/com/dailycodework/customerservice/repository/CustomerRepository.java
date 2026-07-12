package com.dailycodework.customerservice.repository;


import com.dailycodework.customerservice.entities.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @EntityGraph(attributePaths = "addresses")
    Optional<Customer> findWithAddressesById(Long id);

    @EntityGraph(attributePaths = "addresses")
    List<Customer> findAll();
}

