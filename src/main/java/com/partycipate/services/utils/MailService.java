package com.partycipate.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderUsername;
    @Value("${mail.sending.enabled}")
    private Boolean sendingEnabled;
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    public void sendEmail(String receiverEmail, String subject, String text) {
        logger.info("Sending email to '{}', subject of message is '{}'", receiverEmail, subject);
        if (!sendingEnabled) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderUsername);
        message.setTo(receiverEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
