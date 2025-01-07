package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.exception.UserException;
import com.beyondtech.tvpss.facade.UserManagementFacade;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.UserManagementService;
import com.beyondtech.tvpss.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/SuperAdmin/Pengguna")
public class ManageUserController {

	private static final Logger log = LoggerFactory.getLogger(ManageUserController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserManagementService userManagementService;
	@Autowired
	private final UserManagementFacade userManagementFacade;

	public ManageUserController(UserManagementService userManagementService, UserManagementFacade userManagementFacade, MessageSource messageSource) {
		this.userManagementService = userManagementService;
		this.userManagementFacade = userManagementFacade;
		this.messageSource = messageSource;
	}

	@GetMapping("")
	public String showAllUser(Model model) {
		log.debug("User is authenticated: {}", SecurityContextHolder.getContext().getAuthentication());
		Locale locale = LocaleContextHolder.getLocale();
		String breadcrumbUserManagement = messageSource.getMessage("breadcrumb.userManagement", null, locale);
		String breadcrumbAllUsers = messageSource.getMessage("breadcrumb.allUsers", null, locale);

		model.addAttribute("pageTitle", "View User");
		model.addAttribute("currentPage", "SuperAdminPengguna");
		model.addAttribute("content", "SuperAdmin/UsersManagement/view-all-user");

		model.addAttribute("headerText", breadcrumbAllUsers);
		model.addAttribute("breadcrumbTitle1", breadcrumbUserManagement);
		model.addAttribute("breadcrumbTitle2", breadcrumbAllUsers);

		List<User> users = userManagementService.getAllUsers();
		model.addAttribute("users", users);

		System.out.println("Users" + users);
		return "layouts/admin-layouts";
	}

//	@GetMapping("/addUser")
//	public void formAddUser(Model model) {
//
//	}

	@PostMapping("/addUser")
	public String addUser(
			@RequestParam("fullName") String fullName,
			@RequestParam("email") String email,
			@RequestParam("userType") String userType,
			@RequestParam("addUserDistrict") String district,
			@RequestParam("addUserSchoolCode") String schoolcode,
			RedirectAttributes redirectAttributes) {

//		String password = PasswordUtil.generateRandomPassword(12, true);
//		System.out.println("Generated password: " + password);
		String password = "test";

		try {
			userManagementFacade.addUser(fullName, email, password, district, userType, schoolcode);

			redirectAttributes.addFlashAttribute("success", "User added successfully!");
		} catch (UserException ex) {
			
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/SuperAdmin/Pengguna";
	}


	@GetMapping("/ajax/{id}")
	@ResponseBody
	public Map<String, Object> getUserById(@PathVariable("id") Long id) {
		return userManagementService.getUserWithSchoolDetails(id);
	}
}
