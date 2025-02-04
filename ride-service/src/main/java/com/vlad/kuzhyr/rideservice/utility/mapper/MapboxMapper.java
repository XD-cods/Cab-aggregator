package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.rideservice.exception.AddressNotValidException;
import com.vlad.kuzhyr.rideservice.exception.DistanceExtractionException;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapboxMapper {

    private final ObjectMapper objectMapper;

    public double[] extractCoordinatesFromGeocodeResponse(String responseBody) {
        JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode features = jsonNode.path("features");

        if (!features.isEmpty()) {
            JsonNode geometry = features.get(0).path("geometry");
            JsonNode coordinates = geometry.path("coordinates");

            if (coordinates.isArray() && coordinates.size() == 2) {
                return new double[] {coordinates.get(0).asDouble(), coordinates.get(1).asDouble()};
            }
        }

        throw new AddressNotValidException(
            ExceptionMessageConstant.ADDRESS_NOT_VALID_MESSAGE
        );
    }

    public double extractDistanceFromDirectionsResponse(String responseBody) {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode routes = jsonNode.path("routes");

        if (!routes.isEmpty()) {
            return routes.get(0).path("distance").asDouble();
        }

        throw new DistanceExtractionException(
            ExceptionMessageConstant.DISTANCE_EXTRACTION_FAILED_MESSAGE
        );
    }

}
