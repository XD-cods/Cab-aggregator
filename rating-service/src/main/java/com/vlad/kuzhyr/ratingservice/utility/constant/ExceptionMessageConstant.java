package com.vlad.kuzhyr.ratingservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

    public static final String RATING_NOT_FOUND_MESSAGE = "rating not found by id: %d";

    public static final String RATINGS_NOT_FOUND_BY_PASSENGER_ID_MESSAGE = "ratings not found by passenger id: %d";

    public static final String RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE = "ratings not founded by driver id: %d";

    public static final String RATING_ALREADY_EXISTS_MESSAGE = "rating already exists by %s and by ride id: %d";

    public static final String RATED_BY_CAN_NOT_NULL_MESSAGE = "rated by can't be null";

    public static final String RIDE_INFO_NOT_FOUND_MESSAGE = "ride not found by id: %d or ride not completed";

}
