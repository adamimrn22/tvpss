package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.service.TestMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mail")
public class TestMailController {

    @Autowired
    private TestMailService testMailService;

    public TestMailController(TestMailService testMailService) {
        this.testMailService = testMailService;
    }

    @GetMapping("/form")
    public String showEmailForm() {
        return "/mail/testsendemailform";
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam("to") String to,
                            @RequestParam("subject") String subject,
                            @RequestParam("body") String body,
                            Model model) {
        try {
            testMailService.sendMail(to, subject, body);
            model.addAttribute("message", "Email sent successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed to send email: " + e.getMessage());
        }

        return "redirect:/mail/form";  // Return to the same page with the message
    }
}