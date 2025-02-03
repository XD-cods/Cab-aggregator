package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.utility.mapper.MapboxMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class MapboxClient {

  private final RestTemplate restTemplate;

  private final MapboxMapper mapboxMapper;

  @Value("${mapbox.api.secret-key}")
  private String MAPBOX_ACCESS_TOKEN;

  public double[] geocodeAddress(String address) {

    URI url = UriComponentsBuilder
            .fromUriString("https://api.mapbox.com/search/geocode/v6/forward")
            .queryParam("q", address)
            .queryParam("access_token", MAPBOX_ACCESS_TOKEN)
            .buildAndExpand()
            .encode(StandardCharsets.UTF_8)
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "Mozilla/5.0");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    String responseBody = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

    return mapboxMapper.extractCoordinatesFromGeocodeResponse(responseBody);
  }

  public double calculateDistance(Address origin, Address destination) {
    String coordinates = origin.getLatitude() + "," + origin.getLongitude() + ";"
                         + destination.getLatitude() + "," + destination.getLongitude();
    URI url = UriComponentsBuilder
            .fromUriString("https://api.mapbox.com/directions/v5/mapbox/driving/{coordinates}")
            .queryParam("access_token", MAPBOX_ACCESS_TOKEN)
            .buildAndExpand(coordinates)
            .toUri();

    String responseBody = restTemplate.getForEntity(url, String.class).getBody();

    return mapboxMapper.extractDistanceFromDirectionsResponse(responseBody);
  }

}
