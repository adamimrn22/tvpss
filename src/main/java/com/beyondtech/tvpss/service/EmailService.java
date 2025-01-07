package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendPasswordEmail(User user, String password) throws MessagingException {
        // Create the email context with variables
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("password", password);

        // Process the template
        String htmlContent = templateEngine.process("mail/userSuccessEmail", context);

        // Create the email message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(user.getEmailAddress());
        helper.setSubject("Your Account Password");
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }
}