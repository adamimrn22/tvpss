package com.beyondtech.tvpss.service.mail;

import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.UserManagementService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
public class TvpssVersionMailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserManagementService userManagementService;

    @Async
    public void sendTvpssSubmitted(String userEmail) throws MessagingException {

        Context context = new Context();

        String htmlContent = templateEngine.process("mail/tvpssVersionSubmitMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(userEmail);
        helper.setSubject("PERMOHONAN KEMASKINI VERSI TVPSS");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Async
    public void sendNewTvpssVersionSubmitted(String district) throws MessagingException {

        List<User> users = userManagementService.getPpdAdminByDistrict(district);

        Context context = new Context();
        String htmlContent = templateEngine.process("mail/newTvpssVersionSubmittedMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setSubject("PERMOHONAN KEMASKINI VERSI TVPSS");
        helper.setText(htmlContent, true);

        for (User user : users) {
            helper.setTo(user.getEmailAddress());
            mailSender.send(message);
        }
    }

    @Async
    public void sendUpdatedTvpssVersion(String userEmail) throws MessagingException {

        Context context = new Context();
        String htmlContent = templateEngine.process("mail/validatedTvpssVersionMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(userEmail);
        helper.setSubject("PERMOHONAN KEMASKINI VERSI TVPSS");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
