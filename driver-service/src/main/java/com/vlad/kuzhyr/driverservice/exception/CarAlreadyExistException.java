package com.vlad.kuzhyr.driverservice.exception;

public class CarAlreadyExistException extends RuntimeException {

    public CarAlreadyExistException(String message) {
        super(message);
    }

}
