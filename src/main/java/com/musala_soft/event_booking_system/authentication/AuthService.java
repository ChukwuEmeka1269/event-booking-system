package com.musala_soft.event_booking_system.authentication;

import com.musala_soft.event_booking_system.openapi.model.Credentials;

public interface AuthService {
    String authenticate(Credentials credentials);
}
