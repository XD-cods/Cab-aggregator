package com.vlad.kuzhyr.passengerservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

  public static final String PASSENGER_NOT_FOUND_MESSAGE = "passenger not found by id: %d";
  public static final String PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE = "passenger exist by email: %s";
  public static final String PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE = "passenger exist by phone: %s";

}
