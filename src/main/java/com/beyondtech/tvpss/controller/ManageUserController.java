package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.exception.UserException;
import com.beyondtech.tvpss.facade.UserManagementFacade;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.UserManagementService;
import com.beyondtech.tvpss.utils.PageResponse;
import com.beyondtech.tvpss.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
	public String showAllUser(Model model,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(defaultValue = "10") int size) {
		Locale locale = LocaleContextHolder.getLocale();
		String breadcrumbUserManagement = messageSource.getMessage("breadcrumb.userManagement", null, locale);
		String breadcrumbAllUsers = messageSource.getMessage("breadcrumb.allUsers", null, locale);

		PageResponse<User> userPage = userManagementService.getAllUsersPageable(page, size);

		int totalPages = userPage.getTotalPages();
		int currentPage = userPage.getCurrentPage();
		int startPage = Math.max(1, currentPage - 2);
		int endPage = Math.min(startPage + 4, totalPages);
		if (endPage - startPage < 4 && startPage > 1) {
			startPage = Math.max(1, endPage - 4);
		}

		model.addAttribute("pageTitle", "View User");
		model.addAttribute("currentPageDirectory", "SuperAdminPengguna");
		model.addAttribute("content", "SuperAdmin/UsersManagement/view-all-user");

		model.addAttribute("headerText", breadcrumbAllUsers);
		model.addAttribute("breadcrumbTitle1", breadcrumbUserManagement);
		model.addAttribute("breadcrumbTitle2", breadcrumbAllUsers);

		List<User> users = userManagementService.getAllUsers();
		model.addAttribute("users", users);

		model.addAttribute("users", userPage.getContent());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", userPage.getTotalElements());
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("pageSize", size);

		return "layouts/admin-layouts";
	}

	@PostMapping("/addUser")
	public String addUser(
			@RequestParam("fullName") String fullName,
			@RequestParam("email") String email,
			@RequestParam("userType") String userType,
			@RequestParam("addUserDistrict") String district,
			@RequestParam("addUserSchoolCode") String schoolcode,
			RedirectAttributes redirectAttributes) {

		String password = PasswordUtil.generateRandomPassword(12, true);
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

	@PostMapping("/delete")
	public String deleteUser(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
		System.out.println(userId);
		try {
			userManagementService.deleteUser(userId);
			redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
		} catch (UserException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}

		return "redirect:/SuperAdmin/Pengguna";
	}

}
