package com.vlad.kuzhyr.rideservice.service.impl.cache;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ArrayIndexConstant;
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
        log.debug("findOrCreateAddress: Entering method. Address name: {}", addressName);

        String finalAddressName = addressName.trim();

        Address address = addressRepository.findByAddressName(finalAddressName)
            .orElseGet(() -> createNewAddress(finalAddressName));

        log.debug("findOrCreateAddress: Address found or created. Address name: {}", addressName);
        return address;
    }

    private Address createNewAddress(String addressName) {
        log.debug("createNewAddress: Entering method. Address name: {}", addressName);

        double[] coordinates = mapboxClient.geocodeAddress(addressName);
        Address newAddress = Address.builder()
            .addressName(addressName)
            .latitude(coordinates[ArrayIndexConstant.LATITUDE_INDEX])
            .longitude(coordinates[ArrayIndexConstant.LONGITUDE_INDEX])
            .build();

        Address savedAddress = addressRepository.save(newAddress);

        log.debug("createNewAddress: New address created. Address name: {}", addressName);
        return savedAddress;
    }
}
