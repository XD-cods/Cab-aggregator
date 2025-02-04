package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.DepartureAndDestinationAddressesSameException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final MapboxClient mapboxClient;

    private final PriceService priceService;

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

    public void validateDifferentAddresses(String departureAddress, String destinationAddress) {
        departureAddress = departureAddress.trim();
        destinationAddress = destinationAddress.trim();
        if (departureAddress.equalsIgnoreCase(destinationAddress)) {
            throw new DepartureAndDestinationAddressesSameException(
                ExceptionMessageConstant.DEPARTURE_AND_DESTINATION_ADDRESSES_SAME_MESSAGE
            );
        }
    }

    public void updateRideAddress(Ride ride, String newDepartureAddress, String newDestinationAddress) {
        Address departureAddress = findOrCreateAddress(newDepartureAddress);
        Address destinationAddress = findOrCreateAddress(newDestinationAddress);
        double distance = mapboxClient.calculateDistance(departureAddress, destinationAddress);
        BigDecimal price = priceService.calculatePrice(distance);

        ride.setDepartureAddress(departureAddress);
        ride.setDestinationAddress(destinationAddress);
        ride.setRideDistance(distance);
        ride.setRidePrice(price);
    }

}
