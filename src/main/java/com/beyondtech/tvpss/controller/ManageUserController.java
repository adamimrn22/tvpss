package com.beyondtech.tvpss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/SuperAdmin")
public class ManageUserController {

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/Pengguna")
	public String showAllUser(Model model) {
		Locale locale = LocaleContextHolder.getLocale();

		// Fetch the localized breadcrumb texts from the properties files
		String breadcrumbUserManagement = messageSource.getMessage("breadcrumb.userManagement", null, locale);
		String breadcrumbAllUsers = messageSource.getMessage("breadcrumb.allUsers", null, locale);


		model.addAttribute("pageTitle", "View User");
		model.addAttribute("role", "superadmin");
		model.addAttribute("currentPage", "SuperAdminPengguna");
		model.addAttribute("content", "SuperAdmin/UsersManagement/view-all-user");

		model.addAttribute("headerText", breadcrumbAllUsers);
		model.addAttribute("breadcrumbTitle1", breadcrumbUserManagement);
		model.addAttribute("breadcrumbTitle2", breadcrumbAllUsers);
		return "layouts/admin-layouts";
	}
}
