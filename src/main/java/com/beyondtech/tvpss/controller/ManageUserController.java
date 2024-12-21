package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/SuperAdmin")
public class ManageUserController {

	@GetMapping("/Pengguna")
	public String showAllUser(Model model) {
		model.addAttribute("pageTitle", "View User");
		model.addAttribute("role", "superadmin");
		model.addAttribute("currentPage", "SuperAdminPengguna");
		model.addAttribute("content", "SuperAdmin/UsersManagement/view-all-user");

		model.addAttribute("headerText", "Semua Pengguna");
		model.addAttribute("breadcrumbTitle1", "Pengurusan Pengguna");
		model.addAttribute("breadcrumbTitle2", "Semua Pengguna");

		return "layouts/admin-layouts";
	}
}
