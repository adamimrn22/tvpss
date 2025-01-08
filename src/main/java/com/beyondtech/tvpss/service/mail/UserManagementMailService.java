package com.beyondtech.tvpss.service.mail;

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
public class UserManagementMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public UserManagementMailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendPasswordMail(User user, String password) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("password", password);
        context.setVariable("email", user.getEmailAddress());

        String htmlContent = templateEngine.process("mail/userSuccessMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(user.getEmailAddress());
        helper.setSubject("Your Account Password");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Async
    public void deleteUserMail(User user) throws MessagingException {
        Context context = new Context();

        context.setVariable("name", user.getName());
        context.setVariable("email", user.getEmailAddress());


        String htmlContent = templateEngine.process("mail/userDeletedMail", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(user.getEmailAddress());
        helper.setSubject("Account Berjaya Dipadam");
        helper.setText(htmlContent, true); // true indicates HTML content

        mailSender.send(message);
    }


}