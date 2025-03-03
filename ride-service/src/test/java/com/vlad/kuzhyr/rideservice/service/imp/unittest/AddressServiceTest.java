package com.vlad.kuzhyr.rideservice.service.imp.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.exception.DepartureAndDestinationAddressesSameException;
import com.vlad.kuzhyr.rideservice.exception.NewAddressAndCurrentAddressSameException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.service.impl.AddressService;
import com.vlad.kuzhyr.rideservice.service.impl.cache.AddressCacheService;
import com.vlad.kuzhyr.rideservice.utility.calculator.PriceCalculator;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressCacheService addressCacheService;

    @Mock
    private MapboxClient mapboxClient;

    @Mock
    private PriceCalculator priceCalculator;

    @InjectMocks
    private AddressService addressService;

    @Test
    void validateDepartureAndDestinationDifferentAddresses_shouldThrowExceptionWhenAddressesAreSame() {
        String departureAddress = UnitTestDataProvider.address1().getAddressName();

        DepartureAndDestinationAddressesSameException exception =
            assertThrows(DepartureAndDestinationAddressesSameException.class,
                () -> addressService.validateDepartureAndDestinationDifferentAddresses(departureAddress,
                    departureAddress));

        assertEquals(ExceptionMessageConstant.DEPARTURE_AND_DESTINATION_ADDRESSES_SAME_MESSAGE, exception.getMessage());

        verifyNoInteractions(priceCalculator);
        verifyNoInteractions(mapboxClient);
        verifyNoInteractions(addressCacheService);
    }

    @Test
    void validateDifferentAddresses_shouldNotThrowExceptionWhenAddressesAreDepartureAndDestinationDifferent() {
        String departureAddress = UnitTestDataProvider.address1().getAddressName();
        String destinationAddress = UnitTestDataProvider.address2().getAddressName();

        assertDoesNotThrow(() -> addressService.validateDepartureAndDestinationDifferentAddresses(departureAddress,
            destinationAddress));

        verifyNoInteractions(priceCalculator);
        verifyNoInteractions(mapboxClient);
        verifyNoInteractions(addressCacheService);
    }

    @Test
    void updateRideAddress_shouldThrowNewAddressAndCurrentAddressSameException() {
        Ride ride = UnitTestDataProvider.ride();
        Address departure = ride.getDepartureAddress();
        Address destination = ride.getDestinationAddress();
        String departureAddress = ride.getDepartureAddress().getAddressName();
        String destinationAddress = ride.getDestinationAddress().getAddressName();

        when(addressCacheService.findOrCreateAddress(departureAddress)).thenReturn(departure);
        when(addressCacheService.findOrCreateAddress(destinationAddress)).thenReturn(destination);

        NewAddressAndCurrentAddressSameException exception = assertThrows(
            NewAddressAndCurrentAddressSameException.class,
            () -> addressService.updateRideAddress(ride, departureAddress, destinationAddress)
        );

        assertEquals(ExceptionMessageConstant.NEW_ADDRESS_AND_CURRENT_ADDRESS_SAME, exception.getMessage());

        verify(addressCacheService, times(2)).findOrCreateAddress(any());
        verify(priceCalculator, never()).calculatePrice(anyDouble());
        verify(mapboxClient, never()).calculateDistance(any(Address.class), any(Address.class));
    }

    @Test
    void updateRideAddress_shouldUpdateRideAddress() {
        Ride ride = UnitTestDataProvider.ride();
        Address departure = UnitTestDataProvider.newAddress1();
        Address destination = UnitTestDataProvider.newAddress2();
        String departureAddress = departure.getAddressName();
        String destinationAddress = destination.getAddressName();

        when(mapboxClient.calculateDistance(any(Address.class), any(Address.class))).thenReturn(ride.getRideDistance());
        when(priceCalculator.calculatePrice(ride.getRideDistance())).thenReturn(ride.getRidePrice());
        when(addressCacheService.findOrCreateAddress(departureAddress)).thenReturn(departure);
        when(addressCacheService.findOrCreateAddress(destinationAddress)).thenReturn(destination);

        addressService.updateRideAddress(ride, departureAddress, destinationAddress);

        assertEquals(departure, ride.getDepartureAddress());
        assertEquals(destination, ride.getDestinationAddress());

        verify(addressCacheService, times(2)).findOrCreateAddress(any());
        verify(priceCalculator).calculatePrice(anyDouble());
        verify(mapboxClient).calculateDistance(any(Address.class), any(Address.class));
    }
}
