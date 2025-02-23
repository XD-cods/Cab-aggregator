package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.DepartureAndDestinationAddressesSameException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.service.impl.cache.AddressCacheService;
import com.vlad.kuzhyr.rideservice.utility.calculator.PriceCalculator;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressCacheService addressCacheService;
    private final MapboxClient mapboxClient;
    private final PriceCalculator priceCalculator;

    public void validateDifferentAddresses(String departureAddress, String destinationAddress) {
        log.debug("Address service. Validating different addresses. Departure: {}, Destination: {}", departureAddress,
            destinationAddress);
        departureAddress = departureAddress.trim();
        destinationAddress = destinationAddress.trim();
        if (departureAddress.equalsIgnoreCase(destinationAddress)) {
            log.error(
                "Address service. Departure and destination addresses are the same. Departure: {}, Destination: {}",
                departureAddress, destinationAddress);
            throw new DepartureAndDestinationAddressesSameException(
                ExceptionMessageConstant.DEPARTURE_AND_DESTINATION_ADDRESSES_SAME_MESSAGE
            );
        }
        log.info("Address service. Addresses validated successfully. Departure: {}, Destination: {}", departureAddress,
            destinationAddress);
    }

    public void updateRideAddress(Ride ride, String newDepartureAddress, String newDestinationAddress) {
        log.debug("Address service. Updating ride address. Ride id: {}, New Departure: {}, New Destination: {}",
            ride.getId(), newDepartureAddress, newDestinationAddress);
        Address departureAddress = addressCacheService.findOrCreateAddress(newDepartureAddress);
        Address destinationAddress = addressCacheService.findOrCreateAddress(newDestinationAddress);
        double distance = mapboxClient.calculateDistance(departureAddress, destinationAddress);
        BigDecimal price = priceCalculator.calculatePrice(distance);

        ride.setDepartureAddress(departureAddress);
        ride.setDestinationAddress(destinationAddress);
        ride.setRideDistance(distance);
        ride.setRidePrice(price);
        log.info("Address service. Updated ride address. Ride id: {}, New Departure: {}, New Destination: {}",
            ride.getId(), newDepartureAddress, newDestinationAddress);
    }
}