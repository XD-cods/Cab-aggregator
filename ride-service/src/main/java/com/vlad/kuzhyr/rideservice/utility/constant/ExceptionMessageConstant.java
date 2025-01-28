package com.vlad.kuzhyr.rideservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

  public static final String RIDE_NOT_FOUND_MESSAGE = "ride not found by id: %d";
  public static final String RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE = "rides not found by driver id: %d";
  public static final String RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE = "ride not found by passenger id: %d";

}
