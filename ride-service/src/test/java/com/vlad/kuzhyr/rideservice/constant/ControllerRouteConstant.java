package com.vlad.kuzhyr.rideservice.constant;

public class ControllerRouteConstant {

    public static final String BASE_RIDE_URL = "/api/v1/rides";
    public static final String GET_RIDE_BY_ID_URL = BASE_RIDE_URL + "/%d";
    public static final String GET_ALL_RIDES_URL = BASE_RIDE_URL;
    public static final String GET_ALL_RIDES_BY_DRIVER_ID_URL = BASE_RIDE_URL + "/driver/%d";
    public static final String GET_ALL_RIDES_BY_PASSENGER_ID_URL = BASE_RIDE_URL + "/passenger/%d";
    public static final String CREATE_RIDE_URL = BASE_RIDE_URL;
    public static final String UPDATE_RIDE_URL = BASE_RIDE_URL + "/%d";
    public static final String UPDATE_RIDE_STATUS_URL = BASE_RIDE_URL + "/%d";

}
