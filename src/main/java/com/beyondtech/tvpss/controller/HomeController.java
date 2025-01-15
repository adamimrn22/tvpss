package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/resetpassword")
    public String ResetPassword(Model model) {
        model.addAttribute("pageTitle", "ResetPassword");
        model.addAttribute("currentPageDirectory", "resetpassword");
        model.addAttribute("content", "reset-password");
        model.addAttribute("headerText", "Tukar Kata Laluan");

        return "layouts/admin-layouts";
    }

//    @PostMapping("/resetpassword")
//    public String resetPassword(
//            @RequestParam("currentPassword") String currentPassword,
//            @RequestParam("newPassword") String newPassword,
//            @RequestParam("confirmPassword") String confirmPassword,
//            Model model,
//            HttpServletRequest request,
//            HttpServletResponse response,
//            RedirectAttributes redirectAttributes) {
//
//        // Get the current logged-in user
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User currentUser = userService.findByUsername(username);
//
//        // Validate current password
//        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
//            model.addAttribute("errorMessage", "Current password is incorrect");
//            return "layouts/admin-layouts";
//        }
//
//        // Validate if new password matches confirm password
//        if (!newPassword.equals(confirmPassword)) {
//            model.addAttribute("errorMessage", "New password and confirm password do not match");
//            return "layouts/admin-layouts";
//        }
//
//        // Save the new password
//        String hashedPassword = passwordEncoder.encode(newPassword);
//        currentUser.set (hashedPassword);
//        userService.save(currentUser);
//
//        // Logout the user after password reset
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//
//        // Add a success message to redirect attributes
//        redirectAttributes.addFlashAttribute("resetSuccess", "Your password has been reset successfully. Please log in again.");
//
//        // Redirect to the login page
//        return "redirect:/login"; // Redirect to login page
//    }
}
