package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.utility.mapper.MapboxMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class MapboxClient {

  private final RestTemplate restTemplate;
  private final MapboxMapper mapboxMapper;

  @Value("${mapbox.api.secret-key}")
  private String MAPBOX_ACCESS_TOKEN;

  public double[] geocodeAddress(String address) throws Exception {
    URI url = UriComponentsBuilder
            .fromUriString("https://api.mapbox.com/geocoding/v5/mapbox.places/{address}.json")
            .queryParam("access_token", MAPBOX_ACCESS_TOKEN)
            .buildAndExpand(address.replace(" ", "%20"))
            .toUri();

    String responseBody = restTemplate.getForEntity(url, String.class).getBody();

    return mapboxMapper.extractCoordinatesFromGeocodeResponse(responseBody);
  }

  public double calculateDistance(double[] origin, double[] destination) throws Exception {
    String coordinates = origin[0] + "," + origin[1] + ";" + destination[0] + "," + destination[1];
    URI url = UriComponentsBuilder
            .fromUriString("https://api.mapbox.com/directions/v5/mapbox/driving/{coordinates}")
            .queryParam("access_token", MAPBOX_ACCESS_TOKEN)
            .buildAndExpand(coordinates)
            .toUri();

    String responseBody = restTemplate.getForEntity(url, String.class).getBody();

    return mapboxMapper.extractDistanceFromDirectionsResponse(responseBody);
  }

}
