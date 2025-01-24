package com.vlad.kuzhyr.passengerservice.utility.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PassengerServiceConstant {

  public final String NOT_FOUND_MESSAGE = "passenger not found by id: %d";
  public final String CONFLICT_BY_ID_MESSAGE = "passenger exist by email: %s";
  public final String BAD_REQUEST_MESSAGE = "passenger not available by id: %d";

}
