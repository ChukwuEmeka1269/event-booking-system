package com.musala_soft.event_booking_system.event_listeners;

import com.musala_soft.event_booking_system.events.BookingEvent;
import com.musala_soft.event_booking_system.events.CancellationEvent;
import com.musala_soft.event_booking_system.events.EventCreationEvent;
import com.musala_soft.event_booking_system.events.UserCreationEvent;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.MailMessage;
import com.musala_soft.event_booking_system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    private final String[] CC = {};

    @EventListener
    public void handleUserCreationEvent(UserCreationEvent event) {
        var user = event.getUser();
        log.info("USER CREATION EVENT:::::::::: about to send email notification to user");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getName());
        MailMessage message = MailMessage.builder().to(user.getEmail()).subject(USER_CREATION_SUBJECT).attachments(null).template(TEMPLATE_WELCOME).copy(CC).parameters(parameters).build();
        notificationService.sendEmailNotification(message);
    }

    @EventListener
    public void handleEventCreationEvent(EventCreationEvent event) {
        var createdEvent = event.getEvent();
        var principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("EVENT CREATION :::::::::: about to send email notification to user {}", principal.getName());
        buildMailMessage(createdEvent, principal, USER_EVENT_CREATION_SUBJECT, TEMPLATE_CREATE_EVENT);

    }

    @EventListener
    public void handleEventBookingEvent(BookingEvent event) {
        var createdEvent = event.getEvent();
        var user = event.getUser();
        log.info("EVENT BOOKING :::::::::: about to send email notification to user {}", user.getName());
        buildMailMessage(createdEvent, user, USER_EVENT_BOOKING_SUBJECT, TEMPLATE_BOOK_EVENT);
    }

    @EventListener
    public void handleCancellationEvent(CancellationEvent event) {
        var createdEvent = event.getEvent();
        var user = event.getUser();
        log.info("EVENT CANCELLATION :::::::::: about to send email notification to user {}", user.getName());
        buildMailMessage(createdEvent, user, USER_EVENT_CANCELLATION_SUBJECT, TEMPLATE_CANCEL_EVENT);
    }


    private void buildMailMessage(Event createdEvent, AppUser principal, String userEventCreationSubject, String templateCreateEvent) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", principal.getName());
        parameters.put("event", createdEvent);

        MailMessage message = MailMessage.builder().to(principal.getEmail()).subject(userEventCreationSubject).attachments(null).template(templateCreateEvent).copy(CC).parameters(parameters).build();

        notificationService.sendEmailNotification(message);
    }
}
