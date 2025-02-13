package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressCacheService {

    private final AddressRepository addressRepository;

    private final MapboxClient mapboxClient;

    @Cacheable(value = "addresses", key = "#addressName.trim().toLowerCase()")
    public Address findOrCreateAddress(String addressName) {
        String finalAddressName = addressName.trim();

        return addressRepository.findByAddressName(finalAddressName)
            .orElseGet(() -> createNewAddress(finalAddressName));
    }

    private Address createNewAddress(String addressName) {
        double[] coordinates = mapboxClient.geocodeAddress(addressName);
        Address newAddress = Address.builder()
            .addressName(addressName)
            .latitude(coordinates[0])
            .longitude(coordinates[1])
            .build();

        return addressRepository.save(newAddress);
    }

}
