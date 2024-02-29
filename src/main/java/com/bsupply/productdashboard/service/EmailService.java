package com.bsupply.productdashboard.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${config.defaultSendingEmail}")
    private String DEFAULT_SENDING_EMAIL;

    private final JavaMailSender mailSender;

    @Async
    public void sendMail(@Email String to, String subject, String content) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setText(content, Boolean.TRUE);
            helper.setFrom(DEFAULT_SENDING_EMAIL);
            helper.setSubject(subject);

            log.info("Sending mail to {} on subject {}", to, subject);
            mailSender.send(message);

        } catch (MessagingException e) {

            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}