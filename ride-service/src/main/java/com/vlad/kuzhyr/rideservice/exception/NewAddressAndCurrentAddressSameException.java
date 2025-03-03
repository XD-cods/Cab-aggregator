package com.vlad.kuzhyr.rideservice.exception;

public class NewAddressAndCurrentAddressSameException extends RuntimeException {
    public NewAddressAndCurrentAddressSameException(String message) {
        super(message);
    }
}
