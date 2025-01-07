package com.beyondtech.tvpss.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
