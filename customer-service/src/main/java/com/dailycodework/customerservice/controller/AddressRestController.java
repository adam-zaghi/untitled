package com.dailycodework.customerservice.controller;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.repository.AddressRepository;
import com.dailycodework.customerservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressRestController {
    final private AddressService addressService;
    @GetMapping
    public ResponseEntity<List<Address>> getAll(){
        return ResponseEntity.ok(addressService.getAll());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable Long id){
        return ResponseEntity.ok(addressService.getAddressById(id));
    }
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address){
        return ResponseEntity.ok(addressService.createAddress(address));
    }
    @PutMapping("{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address){
        return ResponseEntity.ok(addressService.updateAddress(id,address));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Long id){
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
