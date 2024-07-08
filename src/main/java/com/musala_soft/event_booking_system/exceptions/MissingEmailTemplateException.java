package com.musala_soft.event_booking_system.exceptions;

public class MissingEmailTemplateException extends RuntimeException{
    public MissingEmailTemplateException(String message) {
        super(message);
    }
}
