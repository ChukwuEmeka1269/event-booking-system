package com.musala_soft.event_booking_system.models;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@Builder
public class MailMessage {
    private String subject;
    private String to;
    private String[] copy;
    private Map<String, Object> parameters;
    private String template;
    @Builder.Default
    private Collection<MailAttachment> attachments = new ArrayList<>();
}
