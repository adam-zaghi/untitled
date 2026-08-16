package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.repository.AddressRepository;
import com.dailycodework.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImp implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(Address address,Long userId) {

        if (Boolean.TRUE.equals(address.getIsDefault()) && address.getCustomer() != null) {
            Long customerId = address.getCustomer().getId();
            unsetOtherDefaultAddresses(customerId, null);
        }

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address address) {

        Address oldAddress = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        oldAddress.setStreet(address.getStreet());
        oldAddress.setCity(address.getCity());
        oldAddress.setCountry(address.getCountry());
        oldAddress.setPostalCode(address.getPostalCode());
        oldAddress.setLongitude(address.getLongitude());
        oldAddress.setLatitude(address.getLatitude());


        return addressRepository.save(oldAddress);
    }
    @Override
    @Transactional
    public Address setDefaultAddress(Long addressId) {
        Address selectedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId));

        if (selectedAddress.getCustomer() == null) {
            throw new RuntimeException("Address is not linked to a customer");
        }

        Long customerId = selectedAddress.getCustomer().getId();

        List<Address> customerAddresses = addressRepository.findByCustomerId(customerId);

        for (Address address : customerAddresses) {
            address.setIsDefault(false);
        }

        selectedAddress.setIsDefault(true);

        addressRepository.saveAll(customerAddresses);

        return selectedAddress;
    }
    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        addressRepository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
    }

    private void unsetOtherDefaultAddresses(Long customerId, Long currentAddressId) {

        List<Address> addresses = addressRepository.findByCustomerId(customerId);

        addresses.forEach(address -> {
            boolean isNotCurrentAddress =
                    currentAddressId == null || !address.getId().equals(currentAddressId);

            if (isNotCurrentAddress && Boolean.TRUE.equals(address.getIsDefault())) {
                address.setIsDefault(false);
            }
        });

        addressRepository.saveAll(addresses);
    }
}