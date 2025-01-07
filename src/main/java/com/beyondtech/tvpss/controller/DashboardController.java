package com.beyondtech.tvpss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/")
	public String showDashboard(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String roleModel = authentication.getAuthorities().iterator().next().getAuthority(); // Get the role
			model.addAttribute("headerText", "Selamat Datang");
			model.addAttribute("pageTitle", " Dashboard");
			setRoleBasedContent(model, roleModel);
		}

		return "layouts/admin-layouts";
	}

	private void setRoleBasedContent(Model model, String role) {
		switch (role) {
			case "ROLE_superadmin":
				model.addAttribute("content", "SuperAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "SuperAdmin");
				break;
			case "ROLE_stateadmin":
				model.addAttribute("content", "StateAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "StateAdmin");
				break;
			case "ROLE_ppdadmin":
				model.addAttribute("content", "PpdAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "AdminPPD");
				break;
			case "ROLE_schooladmin":
				model.addAttribute("content", "SchoolAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "SchoolAdmin");
				break;
			default:
				break;
		}
	}
}
