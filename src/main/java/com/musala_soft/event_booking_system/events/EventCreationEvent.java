package com.musala_soft.event_booking_system.events;


import com.musala_soft.event_booking_system.models.Event;
import org.springframework.context.ApplicationEvent;



public class EventCreationEvent extends ApplicationEvent {
    private final Event event;

    public EventCreationEvent(Event event) {
        super(event);
        this.event = event;
    }

    public Event getEvent(){
        return  event;
    }
}
