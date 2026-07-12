package com.dailycodework.customerservice.service;

import com.dailycodework.customerservice.entities.Address;
import com.dailycodework.customerservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImp implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(Address address) {

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

        /*
         * Si la nouvelle adresse est définie comme adresse par défaut,
         * on enlève le statut par défaut des autres adresses du même client.
         */
        if (address.getIsDefault() != null) {

            if (Boolean.TRUE.equals(address.getIsDefault())) {

                if (oldAddress.getCustomer() == null) {
                    throw new RuntimeException("Cannot set default address without customer");
                }

                Long customerId = oldAddress.getCustomer().getId();

                unsetOtherDefaultAddresses(customerId, oldAddress.getId());

                oldAddress.setIsDefault(true);

            } else {
                oldAddress.setIsDefault(false);
            }
        }

        return addressRepository.save(oldAddress);
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