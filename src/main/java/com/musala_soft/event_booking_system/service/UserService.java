package com.musala_soft.event_booking_system.service;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.openapi.model.User;
import org.apache.coyote.BadRequestException;

public interface UserService {

    AppUser createUser(User user) throws BadRequestException;
}
