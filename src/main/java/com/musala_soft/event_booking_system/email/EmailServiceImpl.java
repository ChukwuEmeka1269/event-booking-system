package com.musala_soft.event_booking_system.email;

import com.musala_soft.event_booking_system.exceptions.MissingEmailTemplateException;
import com.musala_soft.event_booking_system.models.AppUser;
import com.musala_soft.event_booking_system.models.Event;
import com.musala_soft.event_booking_system.models.MailAttachment;
import com.musala_soft.event_booking_system.models.MailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

import static com.musala_soft.event_booking_system.constants.APP_CONSTANT.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${mail.from}")
    private String mailFrom;
    @Value("${mail.name}")
    private String mailName;
    @Value("${event.booking.frontend.domain}")
    private String frontendDomain;
    public final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;


    @Override
    public void sendMail(MailMessage prop) {
        Context context = new Context();
        context.setVariables(prop.getParameters());
        context.setVariable("frontEndUrl", frontendDomain);

        Executors.newCachedThreadPool()
                .submit(() -> {
                    defaultMail(prop, context);
                });
    }

    @Override
    public void sendMailForUpComingEvents(AppUser user, Event event) {
        log.info("UPCOMING EVENT NOTIFICATION:::::::::: about to send email notification to user");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", user);
        parameters.put("event", event);

        final String[] CC = {};
        MailMessage message = MailMessage.builder()
                .to(user.getEmail())
                .copy(CC)
                .subject(USER_UPCOMING_EVENT_SUBJECT)
                .template(TEMPLATE_UPCOMING_EVENT)
                .parameters(parameters)
                .attachments(null)
                .build();

        Context context = new Context();
        context.setVariables(message.getParameters());
        context.setVariable("frontEndUrl", frontendDomain);

        Executors.newCachedThreadPool()
                .submit(() -> {
                    defaultMail(message, context);
                });

    }


    private void defaultMail(MailMessage message, Context context){
        final String content = templateEngine.process(message.getTemplate(), context);

        log.info("Sending mail to -> {}", message.getTo());
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, CollectionUtils.isNotEmpty(message.getAttachments()));
            messageHelper.setFrom(mailFrom, mailName);
            messageHelper.setTo(message.getTo());
            if(ArrayUtils.isNotEmpty(message.getCopy())) messageHelper.setCc(message.getCopy());
            messageHelper.setSubject(message.getSubject());
            messageHelper.setText(content, true);

            if(Objects.nonNull(message.getAttachments())) {
                for (MailAttachment attachment : message.getAttachments()) {
                    String fileName = StringUtils.isEmpty(attachment.getFileName()) ? "notification file.pdf" : attachment.getFileName();
                    ByteArrayResource resource = new ByteArrayResource(attachment.getContent());
                    messageHelper.addAttachment(fileName, resource);
                }
            }

        };

        try {
            emailSender.send(messagePreparator);
            log.info("Mail successfully sent");
        } catch (MailException e) {
            log.error("Error sending mail", e);
            throw new MissingEmailTemplateException("Error sending mail due to  : " + e.getCause().getMessage());
        }
    }
}
