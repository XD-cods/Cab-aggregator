package com.vlad.kuzhyr.ratingservice.constant;

public class ControllerRouteConstant {

    public static final String BASE_RATING_URL = "/api/v1/ratings";
    public static final String GET_RATING_BY_ID_URL = BASE_RATING_URL + "/%d";
    public static final String GET_RATINGS_URL = BASE_RATING_URL;
    public static final String GET_AVERAGE_RATING_BY_PASSENGER_ID_URL = BASE_RATING_URL + "/passenger/%d";
    public static final String GET_AVERAGE_RATING_BY_DRIVER_ID_URL = BASE_RATING_URL + "/driver/%d";
    public static final String CREATE_RATING_URL = BASE_RATING_URL;
    public static final String UPDATE_RATING_URL = BASE_RATING_URL + "/%d";
}