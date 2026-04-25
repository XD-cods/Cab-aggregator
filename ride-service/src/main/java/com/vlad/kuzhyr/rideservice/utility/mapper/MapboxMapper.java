package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.rideservice.exception.AddressNotValidException;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapboxMapper {

    private final ObjectMapper objectMapper;

    public double[] extractCoordinatesFromGeocodeResponse(String responseBody) {
        log.debug("extractCoordinatesFromGeocodeResponse: Entering method. Response body: {}", responseBody);

        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.error("extractCoordinatesFromGeocodeResponse: Failed to parse JSON response. Error: {}",
                e.getMessage());
            throw new RuntimeException(e);
        }

        JsonNode features = jsonNode.path("features");

        if (!features.isEmpty()) {
            JsonNode geometry = features.get(0).path("geometry");
            JsonNode coordinates = geometry.path("coordinates");

            if (coordinates.isArray() && coordinates.size() == 2) {
                double[] result = new double[] {coordinates.get(0).asDouble(), coordinates.get(1).asDouble()};
                log.debug("extractCoordinatesFromGeocodeResponse: Extracted coordinates. Longitude: {}, Latitude: {}",
                    result[0], result[1]);
                return result;
            }
        }

        log.error("extractCoordinatesFromGeocodeResponse: Address is not valid or coordinates could not be extracted.");
        throw new AddressNotValidException(
            ExceptionMessageConstant.ADDRESS_NOT_VALID_MESSAGE
        );
    }

}
