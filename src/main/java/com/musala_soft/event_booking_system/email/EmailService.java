package com.musala_soft.event_booking_system.email;

import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.MailMessage;

public interface EmailService {
    void sendMail(MailMessage prop);
    void sendMailForUpComingEvents(AppUser user, Event event);
}
