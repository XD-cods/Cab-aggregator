package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.DepartureAndDestinationAddressesSameException;
import com.vlad.kuzhyr.rideservice.exception.NewAddressAndCurrentAddressSameException;
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
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressCacheService addressCacheService;
    private final MapboxClient mapboxClient;
    private final PriceCalculator priceCalculator;

    public void validateDepartureAndDestinationDifferentAddresses(String departureAddress, String destinationAddress) {
        log.debug("validateDepartureAndDestinationDifferentAddresses: Entering method. Departure: {}, Destination: {}",
            departureAddress,
            destinationAddress);

        departureAddress = departureAddress.trim();
        destinationAddress = destinationAddress.trim();

        if (departureAddress.equalsIgnoreCase(destinationAddress)) {
            log.error(
                "validateDepartureAndDestinationDifferentAddresses: " +
                "Departure and destination addresses are the same. " +
                "Departure: {}, " +
                "Destination: {}",
                departureAddress, destinationAddress);
            throw new DepartureAndDestinationAddressesSameException(
                ExceptionMessageConstant.DEPARTURE_AND_DESTINATION_ADDRESSES_SAME_MESSAGE
            );
        }

        log.debug("validateDifferentAddresses: Addresses validated successfully. Departure: {}, Destination: {}",
            departureAddress, destinationAddress);
    }

    public void updateRideAddress(Ride ride, String newDepartureAddress, String newDestinationAddress) {
        log.debug("updateRideAddress: Entering method. Ride id: {}, New Departure: {}, New Destination: {}",
            ride.getId(), newDepartureAddress, newDestinationAddress);

        Address departureAddress = addressCacheService.findOrCreateAddress(newDepartureAddress);
        Address destinationAddress = addressCacheService.findOrCreateAddress(newDestinationAddress);

        validateCurrentAddressAndNewAddressDifferent(ride.getDepartureAddress().getAddressName(),
            newDepartureAddress.trim());
        validateCurrentAddressAndNewAddressDifferent(ride.getDestinationAddress().getAddressName(),
            newDestinationAddress.trim());

        double distance = mapboxClient.calculateDistance(departureAddress, destinationAddress);
        BigDecimal price = priceCalculator.calculatePrice(distance);

        ride.setDepartureAddress(departureAddress);
        ride.setDestinationAddress(destinationAddress);
        ride.setRideDistance(distance);
        ride.setRidePrice(price);

        log.debug("updateRideAddress: Ride address updated. Ride id: {}, New Departure: {}, New Destination: {}",
            ride.getId(), newDepartureAddress, newDestinationAddress);
    }

    public void validateCurrentAddressAndNewAddressDifferent(String currentAddress, String newAddress) {
        log.debug("validateCurrentAddressAndNewAddressDifferent: Entering method. Current address: {}, new address: {}",
            currentAddress,
            newAddress);

        if (currentAddress.equalsIgnoreCase(newAddress)) {

            log.error(
                "validateDepartureAndDestinationDifferentAddresses: " +
                "Current address and new address are the same. " +
                "Current address: {}, " +
                "new address: {}",
                currentAddress,
                newAddress);

            throw new NewAddressAndCurrentAddressSameException(
                ExceptionMessageConstant.NEW_ADDRESS_AND_CURRENT_ADDRESS_SAME
            );
        }

        log.debug(
            "validateDepartureAndDestinationDifferentAddresses: " +
            "Departure and destination addresses are the different. " +
            "Current address: {}, " +
            "new address: {}",
            currentAddress,
            newAddress);

    }
}
