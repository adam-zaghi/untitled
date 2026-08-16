package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.entities.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAll();

    Address createAddress(Address address,Long userId);

    Address updateAddress(Long id,Address address);

    void deleteAddress(Long id);

    Address getAddressById(Long id);
    Address setDefaultAddress(Long addressId);
}
