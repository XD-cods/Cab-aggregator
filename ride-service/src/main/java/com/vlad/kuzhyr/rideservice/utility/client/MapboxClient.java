package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.utility.constant.ArrayIndexConstant;
import com.vlad.kuzhyr.rideservice.utility.constant.MapBoxConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.MapboxMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapboxClient {

    private final RestTemplate restTemplate;
    private final MapboxMapper mapboxMapper;

    @Value("${mapbox.api.secret-key}")
    private String mapboxAccessToken;

    @Cacheable(value = "geocode", key = "#address.trim().toLowerCase()")
    public double[] geocodeAddress(String address) {
        log.debug("geocodeAddress: Entering method. Address: {}", address);

        URI url = UriComponentsBuilder
            .fromUriString(MapBoxConstant.GEOCODE_URL)
            .queryParam("q", address)
            .queryParam("access_token", mapboxAccessToken)
            .buildAndExpand()
            .encode(StandardCharsets.UTF_8)
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String responseBody = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        double[] coordinates = mapboxMapper.extractCoordinatesFromGeocodeResponse(responseBody);

        log.debug("geocodeAddress: Geocoded address. Address: {}, Coordinates: [{}, {}]", address,
            coordinates[ArrayIndexConstant.LONGITUDE_INDEX],
            coordinates[ArrayIndexConstant.LATITUDE_INDEX]);
        return coordinates;
    }

    @Cacheable(value = "distance", key = "#origin.addressName.trim().toLowerCase() +" +
                                         " '_' + #destination.addressName.trim().toLowerCase()")
    public double calculateDistance(Address origin, Address destination) {
        log.debug("calculateDistance: Entering method. Origin: {}, Destination: {}",
            origin.getAddressName(), destination.getAddressName());

        String coordinates = origin.getLongitude() + "," + origin.getLatitude() + ";" +
                             destination.getLongitude() + "," + destination.getLatitude();

        URI url = UriComponentsBuilder
            .fromUriString(MapBoxConstant.ROUTE_URL)
            .queryParam("access_token", mapboxAccessToken)
            .buildAndExpand(coordinates)
            .toUri();

        String responseBody = restTemplate.getForEntity(url, String.class).getBody();
        double distance = mapboxMapper.extractDistanceFromDirectionsResponse(responseBody);

        log.debug(
            "calculateDistance: Calculated distance. Origin: {}, Destination: {}, Distance: {} meters",
            origin.getAddressName(), destination.getAddressName(), distance);
        return distance;
    }
}
