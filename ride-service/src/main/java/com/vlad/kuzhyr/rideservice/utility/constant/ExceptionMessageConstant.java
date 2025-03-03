package com.vlad.kuzhyr.rideservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

    public static final String RIDE_NOT_FOUND_MESSAGE = "ride not found by id: %d";

    public static final String RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE = "rides not found by driver id: %d";

    public static final String RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE = "ride not found by passenger id: %d";

    public static final String ADDRESS_NOT_VALID_MESSAGE = "address could not be validated from the geocode response.";

    public static final String DISTANCE_EXTRACTION_FAILED_MESSAGE =
        "failed to extract distance from the directions response.";

    public static final String NOT_VALID_STATUS_TRANSITION = "invalid status transition from %s to %s";

    public static final String RIDE_CAN_NOT_UPDATABLE_MESSAGE = "ride cannot be updated in status: %s";

    public static final String DEPARTURE_AND_DESTINATION_ADDRESSES_SAME_MESSAGE =
        "departure and destination addresses can't same";

    public static final String DRIVER_BUSY_MESSAGE = "driver busy by driver id: %d";

    public static final String PASSENGER_BUSY_MESSAGE = "passenger busy by passenger id: %d";

    public static final String DRIVER_HAS_NO_CAR = "driver with ID %d has no car assigned.";

    public static final String NEW_ADDRESS_AND_CURRENT_ADDRESS_SAME =
        "new address and current address will be different";
}
