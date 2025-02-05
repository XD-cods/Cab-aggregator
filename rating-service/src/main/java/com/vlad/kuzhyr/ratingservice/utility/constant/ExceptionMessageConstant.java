package com.vlad.kuzhyr.ratingservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

    public static final String RATING_NOT_FOUND_MESSAGE = "rating not found by id: %d";

    public static final String RATINGS_NOT_FOUNDED_BY_PASSENGER_ID_MESSAGE = "ratings not founded by passenger id: %d";

    public static final String RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE = "ratings not founded by driver id: %d";

    public static final String RATING_ALREADY_EXISTS_MESSAGE = "rating already exists by %s by ride id: %d";

}
