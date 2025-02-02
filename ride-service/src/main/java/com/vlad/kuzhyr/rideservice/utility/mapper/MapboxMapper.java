package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.rideservice.exception.AddressNotValid;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapboxMapper {

  private final ObjectMapper objectMapper;

  public double[] extractCoordinatesFromGeocodeResponse(String responseBody) throws Exception {
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    JsonNode features = jsonNode.path("features");

    if (!features.isEmpty()) {
      JsonNode center = features.get(0).path("center");
      return new double[]{center.get(0).asDouble(), center.get(1).asDouble()};
    }

    throw new AddressNotValid(
            ExceptionMessageConstant.ADDRESS_NOT_VALID_MESSAGE
    );
  }

  public double extractDistanceFromDirectionsResponse(String responseBody) throws Exception {
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    JsonNode routes = jsonNode.path("routes");

    if (!routes.isEmpty()) {
      return routes.get(0).path("distance").asDouble();
    }

    throw new RuntimeException("Не удалось извлечь расстояние из ответа.");
  }

}
