package com.beyondtech.tvpss.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResetPasswordController {

	@GetMapping("/resetpassword")
	public String ResetPassword(Model model) {
		model.addAttribute("pageTitle", "ResetPassword");
		model.addAttribute("currentPage", "resetpassword");
		model.addAttribute("role", "ppdadmin");
		model.addAttribute("content", "reset-password");
		model.addAttribute("headerText", "Tukar Kata Laluan");

		return "layouts/admin-layouts";
	}
}
