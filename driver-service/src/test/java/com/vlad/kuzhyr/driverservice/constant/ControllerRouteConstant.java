package com.vlad.kuzhyr.driverservice.constant;

public class ControllerRouteConstant {

    public static final String CAR_API_BASE_URL = "/api/v1/cars";
    public static final String GET_ALL_CAR_URL = CAR_API_BASE_URL;
    public static final String GET_CAR_BY_ID_URL = CAR_API_BASE_URL + "/%d";
    public static final String CREATE_CAR_URL = CAR_API_BASE_URL;
    public static final String UPDATE_CAR_URL = CAR_API_BASE_URL + "/%d";
    public static final String DELETE_CAR_URL = CAR_API_BASE_URL + "/%d";

    public static final String DRIVERS_BASE_URL = "/api/v1/drivers";
    public static final String GET_DRIVER_BY_ID_URL = DRIVERS_BASE_URL + "/%d";
    public static final String GET_ALL_DRIVERS_URL = DRIVERS_BASE_URL;
    public static final String CREATE_DRIVER_URL = DRIVERS_BASE_URL;
    public static final String UPDATE_DRIVER_URL = DRIVERS_BASE_URL + "/%d";
    public static final String UPDATE_DRIVER_CARS_URL = DRIVERS_BASE_URL + "/%d";
    public static final String DELETE_DRIVER_URL = DRIVERS_BASE_URL + "/%d";

}
