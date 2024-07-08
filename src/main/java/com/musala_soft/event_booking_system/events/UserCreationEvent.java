package com.musala_soft.event_booking_system.events;

import com.musala_soft.event_booking_system.models.AppUser;
import org.springframework.context.ApplicationEvent;

public class UserCreationEvent extends ApplicationEvent {
    private final AppUser appUser;

    public UserCreationEvent(Object source, AppUser appUser) {
        super(source);
        this.appUser = appUser;
    }

    public AppUser getUser(){
        return appUser;
    }
}
