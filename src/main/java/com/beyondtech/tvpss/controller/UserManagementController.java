package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.exception.UserException;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.mail.UserManagementMailService;
import com.beyondtech.tvpss.service.UserManagementService;
import com.beyondtech.tvpss.utils.PageResponse;
import com.beyondtech.tvpss.utils.PasswordUtil;
import jakarta.mail.MessagingException;
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
public class UserManagementController {

	private static final Logger log = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserManagementService userManagementService;

	public UserManagementController(UserManagementService userManagementService, MessageSource messageSource) {
		this.userManagementService = userManagementService;
		this.messageSource = messageSource;
	}

	@GetMapping("")
	public String showAllUser(Model model,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(defaultValue = "10") int size) {
		Locale locale = LocaleContextHolder.getLocale();
		String breadcrumbUserManagement = messageSource.getMessage("breadcrumb.userManagement", null, locale);
		String breadcrumbAllUsers = messageSource.getMessage("breadcrumb.allUsers", null, locale);

		model.addAttribute("pageTitle", "View User");
		model.addAttribute("currentPageDirectory", "SuperAdminPengguna");
		model.addAttribute("content", "SuperAdmin/UsersManagement/view-all-user");

		model.addAttribute("headerText", breadcrumbAllUsers);
		model.addAttribute("breadcrumbTitle1", breadcrumbUserManagement);
		model.addAttribute("breadcrumbTitle2", breadcrumbAllUsers);

		List<User> users = userManagementService.getAllUsers();
		model.addAttribute("users", users);

		return "layouts/admin-layouts";
	}

	@PostMapping("/addUser")
	public String addUser(
			@RequestParam("fullName") String fullName,
			@RequestParam("email") String email,
			@RequestParam("roleName") String roleName,
			@RequestParam("addUserDistrict") String district,
			@RequestParam("addUserSchoolCode") String schoolCode,
			RedirectAttributes redirectAttributes) {

		String password = PasswordUtil.generateRandomPassword(12, true);
		try {
			userManagementService.addNewUser(fullName, email, password, district, roleName, schoolCode);
			redirectAttributes.addFlashAttribute("success", "User berjaya ditambah!");
			return "redirect:/SuperAdmin/Pengguna";
		} catch (UserException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/SuperAdmin/Pengguna";
		} catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


	@GetMapping("/ajax/{id}")
	@ResponseBody
	public Map<String, Object> getUserById(@PathVariable("id") Long id) {
		return userManagementService.getUserWithSchoolDetails(id);
	}

	@PostMapping("/edit")
	public String editUser(@RequestParam("userId") Long id,
			             @RequestParam("editFullName") String fullName,
						 @RequestParam("editEmail") String email,
						 RedirectAttributes redirectAttributes) {

		try{
			userManagementService.editUser(id, fullName, email);
			redirectAttributes.addFlashAttribute("success", "User berjaya dikemaskini!");
		}catch (UserException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/SuperAdmin/Pengguna";
	}

	@PostMapping("/delete")
	public String deleteUser(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
		try {
			userManagementService.deleteUser(userId);
			redirectAttributes.addFlashAttribute("success", "User berjaya dipadam!");
		} catch (UserException | MessagingException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}

		return "redirect:/SuperAdmin/Pengguna";
	}

}
