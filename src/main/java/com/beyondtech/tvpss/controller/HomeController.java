package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.auth.PasswordChangeDto;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.UserManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }

        ZoneId systemTimeZone = ZoneId.systemDefault();

        // Get the current date and time in that time zone
        ZonedDateTime currentDateTime = ZonedDateTime.now(systemTimeZone);

        // Print the current time with time zone
        System.out.println("Current Date and Time in Time Zone: " + currentDateTime);

        return "login";
    }

    @GetMapping("/changepassword")
    public String ResetPasswordForm(Model model) {
        model.addAttribute("pageTitle", "Reset Password");
        model.addAttribute("currentPageDirectory", "changepassword");
        model.addAttribute("content", "update-password");
        model.addAttribute("headerText", "Tukar Kata Laluan");

        return "layouts/admin-layouts";
    }

    @PostMapping("/changepassword")
    public String resetPassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            Authentication authentication,
            HttpServletRequest request) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "Kata laluan baharu tidak sepadan");
            return "redirect:/changepassword";
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("message", "Kata laluan mestilah sekurang-kurangnya 6 aksara panjang");
            return "redirect:/changepassword";
        }

        try {
            String email = authentication.getName();
            System.out.println("emailll " + email);

            boolean success = userManagementService.changePassword(
                    email,
                    currentPassword,
                    newPassword
            );

            if (success) {
                 SecurityContextHolder.clearContext();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                redirectAttributes.addFlashAttribute("success", "Kata laluan berjaya ditukar");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("message", "Kata laluan semasa tidak betul");
                return "redirect:/changepassword";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Ralat menukar kata laluan: " + e.getMessage());
            return "redirect:/changepassword";
        }
    }

}
