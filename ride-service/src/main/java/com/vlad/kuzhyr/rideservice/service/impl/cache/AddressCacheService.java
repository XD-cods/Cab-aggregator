package com.vlad.kuzhyr.rideservice.service.impl.cache;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressCacheService {

    private final AddressRepository addressRepository;
    private final MapboxClient mapboxClient;

    @Cacheable(value = "addresses", key = "#addressName.trim().toLowerCase()")
    @Transactional
    public Address findOrCreateAddress(String addressName) {
        log.debug("Address cache service. Finding or creating address. Address name: {}", addressName);
        String finalAddressName = addressName.trim();

        Address address = addressRepository.findByAddressName(finalAddressName)
            .orElseGet(() -> createNewAddress(finalAddressName));
        log.info("Address cache service. Found or created address. Address name: {}", addressName);
        return address;
    }

    private Address createNewAddress(String addressName) {
        log.debug("Address cache service. Creating new address. Address name: {}", addressName);
        double[] coordinates = mapboxClient.geocodeAddress(addressName);
        Address newAddress = Address.builder()
            .addressName(addressName)
            .latitude(coordinates[1])
            .longitude(coordinates[0])
            .build();

        Address savedAddress = addressRepository.save(newAddress);
        log.info("Address cache service. Created new address. Address name: {}", addressName);
        return savedAddress;
    }

}
