package com.musala_soft.event_booking_system.events;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import org.springframework.context.ApplicationEvent;

public class BookingEvent extends ApplicationEvent {

    private AppUser user;
    private Event event;

    public BookingEvent(Object source, AppUser user, Event event) {
        super(source);
        this.user = user;
        this.event = event;
    }

    public AppUser getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }
}
