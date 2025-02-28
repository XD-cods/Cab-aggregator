package com.vlad.kuzhyr.passengerservice.constant;

public class ControllerRouteConstant {

    public static final String BASE_PASSENGER_URL = "/api/v1/passengers";
    public static final String GET_PASSENGER_BY_ID_URL = BASE_PASSENGER_URL + "/%d";
    public static final String GET_PASSENGERS_URL = BASE_PASSENGER_URL;
    public static final String CREATE_PASSENGER_URL = BASE_PASSENGER_URL;
    public static final String UPDATE_PASSENGER_URL = BASE_PASSENGER_URL + "/%d";
    public static final String DELETE_PASSENGER_URL = BASE_PASSENGER_URL + "/%d";

}
