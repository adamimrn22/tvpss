package com.beyondtech.tvpss.service.mail;

import com.beyondtech.tvpss.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ResetPasswordMailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendPasswordResetEmail(String userEmail, String token, String applicationUrl) throws MessagingException {

        String resetLink = applicationUrl + "/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String htmlContent = templateEngine.process("mail/resetPasswordMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(userEmail);
        helper.setSubject("Tetap Semula Kata Laluan");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
