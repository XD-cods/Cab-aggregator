package com.vlad.kuzhyr.rideservice.utility.client.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.mapper.MapboxMapper;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class MapboxClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MapboxMapper mapboxMapper;

    @InjectMocks
    private MapboxClient mapboxClient;

    @Test
    void geocodeAddress_shouldReturnCoordinates() {
        String address = "123 Main St, CityA";
        double[] expectedCoordinates = {40.7128, -74.0060};

        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        when(mapboxMapper.extractCoordinatesFromGeocodeResponse(anyString())).thenReturn(expectedCoordinates);

        double[] result = mapboxClient.geocodeAddress(address);

        assertArrayEquals(expectedCoordinates, result);

        verify(mapboxMapper).extractCoordinatesFromGeocodeResponse(anyString());
        verify(restTemplate).exchange(any(), eq(HttpMethod.GET), any(), eq(String.class));
    }

    @Test
    void calculateDistance_shouldReturnDistance() {
        Address origin = UnitTestDataProvider.address1();
        Address destination = UnitTestDataProvider.address2();
        double expectedDistance = 100.0;

        when(restTemplate.getForEntity(any(), eq(String.class)))
            .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));
        when(mapboxMapper.extractDistanceFromDirectionsResponse(anyString())).thenReturn(expectedDistance);

        double result = mapboxClient.calculateDistance(origin, destination);

        assertEquals(expectedDistance, result);

        verify(restTemplate).getForEntity(any(), eq(String.class));
        verify(mapboxMapper).extractDistanceFromDirectionsResponse(anyString());
    }
}
