package com.musala_soft.event_booking_system.service.impl;

import com.musala_soft.event_booking_system.email.EmailService;
import com.musala_soft.event_booking_system.models.MailMessage;
import com.musala_soft.event_booking_system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotification implements NotificationService {
    private final EmailService emailService;
    @Override
    public void sendEmailNotification(MailMessage mailMessage) {
        emailService.sendMail(mailMessage);
    }
}
