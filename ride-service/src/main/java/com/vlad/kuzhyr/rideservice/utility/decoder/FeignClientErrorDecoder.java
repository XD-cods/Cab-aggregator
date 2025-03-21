package com.vlad.kuzhyr.rideservice.utility.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vlad.kuzhyr.rideservice.exception.CustomFeignException;
import com.vlad.kuzhyr.rideservice.exception.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new ErrorDecoder.Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return errorDecoder.decode(methodKey, response);
        }
        ErrorResponse errorResponse = objectMapper
            .registerModule(new JavaTimeModule())
            .readValue(readResponseBody(response), ErrorResponse.class);

        return new CustomFeignException(
            errorResponse,
            response.status()
        );
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            @Cleanup InputStreamReader inputStreamReader =
                new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
        return "";
    }
}