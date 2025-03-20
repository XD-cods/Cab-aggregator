package com.vlad.kuzhyr.rideservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeignClientException extends RuntimeException {

    private final ErrorResponse errorResponse;

    private final int httpStatus;

}
