package com.bsupply.productdashboard.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=56804",
        "spring.mail.username=user",
        "spring.mail.password=admin",
        "properties.mail.smtp.auth=true",
        "properties.mail.smtp.starttls.enable=true"
})
class EmailServiceTest {

    @Container
    static GenericContainer greenMailGenericContainer = new GenericContainer<>(DockerImageName.parse("greenmail/standalone:latest"))
            .waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=user:admin")
            .withExposedPorts(3025);

    @DynamicPropertySource
    static void configureMailHost(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", greenMailGenericContainer::getHost);
        registry.add("spring.mail.port", greenMailGenericContainer::getFirstMappedPort);
    }

    @Autowired
    EmailService emailService;

    @Autowired
    JavaMailSender javaMailSender;

    @RegisterExtension
    static GreenMailExtension testSmtp = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);


    @Test
    public void testSendEmail() throws Exception {

        String to = "recipient@example.com";
        String subject = "Test Subject";
        String content = "<html><body><h1>Hello, world!</h1></body></html>";

        emailService.sendMail(to, subject, content);

        // Wait for the email to be sent (adjust timeout as needed)
        testSmtp.waitForIncomingEmail(3000, 1);

        MimeMessage[] receivedMessages = testSmtp.getReceivedMessages();
        System.out.println("receivedMessages = " + receivedMessages.length);
        assertThat(receivedMessages).isNotEmpty();

        // Validate the content of the first received email
        MimeMessage receivedMessage = receivedMessages[0];
        assertThat(receivedMessage.getSubject()).isEqualTo(subject);
        assertThat((String) receivedMessage.getContent()).contains("Hello, world!");
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        // Create MimeMessage
        jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo("recipient@example.com");
        helper.setSubject("Test Email");
        helper.setText("<html><body><h1>Hello!</h1><p>This is a test email.</p></body></html>", true);

        // Send the email
        javaMailSender.send(mimeMessage);

        // Check received email
        MimeMessage[] receivedMessages = testSmtp.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Test Email", receivedMessages[0].getSubject());
        assertEquals("recipient@example.com", receivedMessages[0].getAllRecipients()[0].toString());
        // You can also check the HTML content of the received email
        String content = (String) receivedMessages[0].getContent();
        assertEquals("<html><body><h1>Hello!</h1><p>This is a test email.</p></body></html>", content);
    }

}