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
		// Get the current authentication object
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String roleModel = authentication.getAuthorities().iterator().next().getAuthority(); // Get the role
			model.addAttribute("headerText", "Selamat Datang");
			model.addAttribute("pageTitle", " Dashboard");

			// Call method to get content based on role
			setRoleBasedContent(model, roleModel);
		}

		return "layouts/admin-layouts"; // Return the same layout for all roles
	}

	private void setRoleBasedContent(Model model, String role) {
		// Method to determine content based on role
		switch (role) {
			case "ROLE_superadmin":
				model.addAttribute("content", "SuperAdmin/dashboard");
				model.addAttribute("currentPage", "SuperAdmin");
				break;
			case "ROLE_stateadmin":
				model.addAttribute("content", "StateAdmin/dashboard");
				model.addAttribute("currentPage", "StateAdmin");
				break;
			case "ROLE_ppdadmin":
				model.addAttribute("content", "PpdAdmin/dashboard");
				model.addAttribute("currentPage", "AdminPPD");
				break;
			case "ROLE_schooladmin":
				model.addAttribute("content", "SchoolAdmin/dashboard");
				model.addAttribute("currentPage", "SchoolAdmin");
				break;
			default:
				break;
		}
	}
}
