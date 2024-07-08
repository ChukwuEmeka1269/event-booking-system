package com.musala_soft.event_booking_system.utils;

import com.musala_soft.event_booking_system.exceptions.UserNotFoundException;

import com.musala_soft.event_booking_system.models.AppUser;

import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtil {
    public static AppUser getCurrentlyLoggedInUser() {
        try {
            return (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            throw new UserNotFoundException("User has not logged in");
        }
    }


}
