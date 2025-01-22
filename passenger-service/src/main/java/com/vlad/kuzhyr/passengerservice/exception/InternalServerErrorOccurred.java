package com.vlad.kuzhyr.passengerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorOccurred extends RuntimeException {
  public InternalServerErrorOccurred(String message) {
    super(message);
  }
}
