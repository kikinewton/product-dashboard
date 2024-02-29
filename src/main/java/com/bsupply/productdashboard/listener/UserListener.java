package com.bsupply.productdashboard.listener;

import com.bsupply.productdashboard.entity.User;
import com.bsupply.productdashboard.service.EmailService;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Slf4j
@RequiredArgsConstructor
public class UserListener {

  private final SpringTemplateEngine templateEngine;
  private final EmailService emailService;

  @Value("${config.templateMail}")
  String newEmployeeEmail;

  @PostPersist
  public void sendUserSignupEmail(User user) {

    log.info("Send email to new user");
    String title = "Welcome " + user.getLastName();
    String message =
        "You now have access to the product dashboard software. Kindly contact your admin for your default credentials";
    String emailContent = composeEmail(title, message, newEmployeeEmail);
    try {
      emailService.sendMail(user.getEmail(), "SIGNUP", emailContent);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private String composeEmail(String title, String message, String template) {
    Context context = new Context();
    context.setVariable("title", title);
    context.setVariable("message", message);
    return templateEngine.process(template, context);
  }
}
