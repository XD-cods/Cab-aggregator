package com.vlad.kuzhyr.driverservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

  public static final String DRIVER_NOT_FOUND_MESSAGE = "driver not found by id: %d";
  public static final String DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE = "driver exist by phone: %s";
  public static final String DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE = "driver exist by email: %s";

  public static final String CAR_NOT_FOUND_MESSAGE = "car not found by id: %d";
  public static final String CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE = "car exist by car number: %s";

}
