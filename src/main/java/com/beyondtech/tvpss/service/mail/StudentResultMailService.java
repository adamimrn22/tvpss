package com.beyondtech.tvpss.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class StudentResultMailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendStudentResult(String studentEmail) throws MessagingException {

        Context context = new Context();

        String htmlContent = templateEngine.process("mail/studentResultMail", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admintvpss@moe.gov.my");
        helper.setTo(studentEmail);
        helper.setSubject("Aplikasi Permohonan TVPSS Anda");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
