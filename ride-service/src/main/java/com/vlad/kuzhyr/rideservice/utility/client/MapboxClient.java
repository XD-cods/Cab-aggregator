package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.utility.constant.ArrayIndexConstant;
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

    @Value("${mapbox.api.routes.geocode}")
    private String mapboxGeocodeUrl;

    @Cacheable(value = "geocode", key = "#address.trim().toLowerCase()")
    public double[] geocodeAddress(String address) {
        log.debug("geocodeAddress: Entering method. Address: {}", address);

        URI url = UriComponentsBuilder
            .fromUriString(mapboxGeocodeUrl)
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

}
