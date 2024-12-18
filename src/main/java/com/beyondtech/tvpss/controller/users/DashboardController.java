package com.beyondtech.tvpss.controller.users;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	@GetMapping("/SuperAdmin")
	public String showsSADashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("role", "superadmin");
		model.addAttribute("currentPage", "superadmin");
		model.addAttribute("content", "SuperAdmin/dashboard");
		model.addAttribute("headerText", "Selamat Datang Pengguna");

		return "layouts/admin-layouts";
	}

	@GetMapping("/StateAdmin")
	public String showASTDashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("role", "stateadmin");
		model.addAttribute("currentPage", "StateAdmin");
		model.addAttribute("content", "StateAdmin/dashboard");
		model.addAttribute("headerText", "Selamat Datang Pengguna");

		return "layouts/admin-layouts";
	}

	@GetMapping("/AdminPPD")
	public String showPAashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("role", "ppdadmin");
		model.addAttribute("currentPage", "AdminPPD");
		model.addAttribute("content", "PpdAdmin/dashboard");
		model.addAttribute("headerText", "Selamat Datang Pengguna");

		return "layouts/admin-layouts";
	}

	@GetMapping("/SchoolAdmin")
	public String showsASDashboard(Model model) {
		model.addAttribute("pageTitle", "Dashboard");
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "SchoolAdmin");
		model.addAttribute("content", "SchoolAdmin/dashboard");
		model.addAttribute("headerText", "Selamat Datang Pengguna");
		return "layouts/admin-layouts";
	}
}
