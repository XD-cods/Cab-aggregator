package com.vlad.kuzhyr.driverservice.utility.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegularExpressionConstant {

  public final String PASSENGER_PHONE_REGEX = "/^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$/";

}
