package org.booking.controllers;

public class NoSuchEventException extends RuntimeException {

    public NoSuchEventException(String message) {
        super(message);
    }
}
