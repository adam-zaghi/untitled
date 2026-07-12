package com.dailycodework.customerservice.repository;

import com.dailycodework.customerservice.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByCustomerId(Long customerId);
}
