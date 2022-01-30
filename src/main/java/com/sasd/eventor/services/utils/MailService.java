package com.sasd.eventor.services.utils;

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

    public void sendEmail(String receiverEmail, String subject, String text) {
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
