package com.vlad.kuzhyr.passengerservice.utility.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {

  public final String PASSENGER_NOT_FOUND_MESSAGE = "passenger not found by id: %d";
  public final String PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE = "passenger exist by email: %s";
  public final String PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE = "passenger exist by phone: %s";

}
