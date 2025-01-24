package com.vlad.kuzhyr.passengerservice.utility.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PassengerServiceConstant {

  public final String NOT_FOUND_MESSAGE = "passenger not found by id: %d";
  public final String CONFLICT_BY_EMAIL_OR_PHONE = "passenger exist by email: %s or phone: %s";
  public final String BAD_REQUEST_MESSAGE = "passenger not available by id: %d";

}
