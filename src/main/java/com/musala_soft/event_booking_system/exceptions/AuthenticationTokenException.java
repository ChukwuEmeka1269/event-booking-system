package com.musala_soft.event_booking_system.exceptions;


public class AuthenticationTokenException extends RuntimeException {
    public AuthenticationTokenException(String message) {
        super(message);
    }

    public AuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
