package com.beyondtech.tvpss.auth;

import com.beyondtech.tvpss.model.PasswordResetToken;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.UserManagementService;
import com.beyondtech.tvpss.service.mail.ResetPasswordMailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {
    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ResetPasswordMailService resetPasswordMailService;

    @Autowired
    private UserManagementService userDao;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String userEmail,
                                        HttpServletRequest request,
                                        Model model) throws MessagingException {
        Optional<User> user = userDao.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            model.addAttribute("error", "Email not found");
            return "forgot-password";
        }

        // Check if the user already has a valid reset token
        PasswordResetToken existingToken = passwordResetService.findByUserId(user.get().getId());
        if (existingToken != null) {
            if (existingToken.isExpired()) {
                passwordResetService.deleteToken(existingToken);
            } else {
                model.addAttribute("error","Anda telah meminta tetapan semula kata laluan. Sila semak e-mel anda atau cuba lagi kemudian.");
                return "forgot-password";
            }
        }

        String token = UUID.randomUUID().toString();
        passwordResetService.createPasswordResetTokenForUser(user, token);

        String applicationUrl = "http://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();

        resetPasswordMailService.sendPasswordResetEmail(userEmail, token, applicationUrl);

        model.addAttribute("success", "E-mel tetapan semula kata laluan dihantar");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        String result = passwordResetService.validatePasswordResetToken(token);
        if (result != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       Model model) {
        String result = passwordResetService.validatePasswordResetToken(token);
        if (result != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        PasswordResetToken resetToken = passwordResetService.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }
        User user = resetToken.getUser();
        userDao.resetPassword(user, passwordEncoder.encode(newPassword));
        passwordResetService.deleteToken(resetToken);

        model.addAttribute("success", "Kata laluan telah berjaya ditetapkan semula");
        return "login";
    }
}