package com.musala_soft.event_booking_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailAttachment {
    private String fileName;
    private byte[] content;
}
