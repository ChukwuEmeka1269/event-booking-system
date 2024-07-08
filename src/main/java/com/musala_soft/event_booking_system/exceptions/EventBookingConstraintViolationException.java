package com.musala_soft.event_booking_system.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Data
public class EventBookingConstraintViolationException<T> {

    private String name;
    private List<Map<String, Object>> violations;
    private HttpStatus status;

    public EventBookingConstraintViolationException(String name, List<Map<String, Object>> violations, HttpStatus status) {
        this.name = name;
        this.violations = violations;
        this.status = status;
    }
}
