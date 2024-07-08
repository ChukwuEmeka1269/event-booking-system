package com.musala_soft.event_booking_system.service.impl;

import com.musala_soft.event_booking_system.email.EmailService;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.NotificationLog;
import com.musala_soft.event_booking_system.repository.NotificationLogRepository;
import com.musala_soft.event_booking_system.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeriodicNotificationService{
    private final EventService eventService;
    private final EmailService emailService;
    private final NotificationLogRepository notificationLogRepository;

    @Scheduled(cron = "${app.periodic.email}")
    public void sendEmailNotification() {
        List<Event> upcomingEvents = eventService.fetchUpcomingEvents();
       for(Event event : upcomingEvents){
           List<AppUser> bookedUsers = event.getBookedUsers();
           for(AppUser userThatBookedEvent : bookedUsers){
               emailService.sendMailForUpComingEvents(userThatBookedEvent, event);
               logNotification(event);
           }
           log.info("LOGGING NOTIFICATION FOR ::::::::::::::::::::::   {}", event);
       }

    }
    private void logNotification(Event event) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setEvent(event);
        notificationLog.setNotificationTime(LocalDateTime.now());
        NotificationLog savedLog = notificationLogRepository.save(notificationLog);

        log.info("NOTIFICATION LOG SAVED............ with id = {}", savedLog.getId());

    }


}
