package org.booking.services.impl;

public class OccupiedPlaceException extends RuntimeException {

    public OccupiedPlaceException(String message) {
        super(message);
    }
}
