package com.musala_soft.event_booking_system.service;

import com.musala_soft.event_booking_system.models.MailMessage;

public interface NotificationService {
    void sendEmailNotification(MailMessage mailMessage);
}
