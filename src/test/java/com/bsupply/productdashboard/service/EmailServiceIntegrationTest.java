package com.bsupply.productdashboard.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testSendMail() throws Exception {
        // Mocking the mail sending process
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage(null, null));

        // Define email parameters
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // Send the email
        emailService.sendMail(to, subject, content);

        // Verify that the JavaMailSender's send method was called with a MimeMessage
        verify(mailSender).send(any(MimeMessage.class));
    }
}
