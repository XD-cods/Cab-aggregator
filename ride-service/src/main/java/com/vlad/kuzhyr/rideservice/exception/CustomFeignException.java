package com.vlad.kuzhyr.rideservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomFeignException extends RuntimeException {

    private final ErrorResponse errorResponse;

    private final int httpStatus;

}
