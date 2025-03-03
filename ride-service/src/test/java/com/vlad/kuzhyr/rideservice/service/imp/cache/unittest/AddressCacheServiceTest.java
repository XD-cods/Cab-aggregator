package com.vlad.kuzhyr.rideservice.service.imp.cache.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.service.impl.cache.AddressCacheService;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ArrayIndexConstant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressCacheServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MapboxClient mapboxClient;

    @InjectMocks
    private AddressCacheService addressCacheService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findOrCreateAddress_shouldReturnExistingAddress() {
        Address existingAddress = UnitTestDataProvider.address1();
        String addressName = existingAddress.getAddressName();

        when(addressRepository.findByAddressName(addressName)).thenReturn(Optional.of(existingAddress));

        Address result = addressCacheService.findOrCreateAddress(addressName);

        assertEquals(existingAddress, result);

        verify(addressRepository).findByAddressName(addressName);
        verifyNoInteractions(mapboxClient);
    }

    @Test
    void findOrCreateAddress_shouldCreateNewAddress() {
        Address existingAddress = UnitTestDataProvider.address1();
        String addressName = existingAddress.getAddressName();
        double[] coordinates = {existingAddress.getLongitude(), existingAddress.getLatitude()};

        when(addressRepository.findByAddressName(addressName)).thenReturn(Optional.empty());
        when(mapboxClient.geocodeAddress(addressName)).thenReturn(coordinates);
        when(addressRepository.save(any(Address.class))).thenReturn(existingAddress);

        Address result = addressCacheService.findOrCreateAddress(addressName);

        assertEquals(addressName, result.getAddressName());
        assertEquals(coordinates[ArrayIndexConstant.LATITUDE_INDEX], result.getLatitude());
        assertEquals(coordinates[ArrayIndexConstant.LONGITUDE_INDEX], result.getLongitude());

        verify(addressRepository).save(any(Address.class));
    }
}