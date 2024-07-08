package com.musala_soft.event_booking_system.exceptions;

public class AlreadyBookedEvenException extends RuntimeException {
    public AlreadyBookedEvenException(String message) {
        super(message);
    }
}
