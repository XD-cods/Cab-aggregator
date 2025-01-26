package com.vlad.kuzhyr.driverservice.utility.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {

  public final String DRIVER_NOT_FOUND = "driver not found by id: %d";
  public final String DRIVER_CONFLICT_BY_PHONE = "driver exist by phone: %s";
  public final String DRIVER_CONFLICT_BY_EMAIL = "driver exist by email: %s";

  public final String CAR_NOT_FOUND = "car not found by id: %d";
  public final String CAR_CONFLICT_BY_PHONE = "car exist by phone: %s";
  public final String CAR_CONFLICT_BY_EMAIL = "car exist by email: %s";

}
