package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.LogService;
import com.beyondtech.tvpss.service.SchoolVersionStatusService;
import com.beyondtech.tvpss.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

	@Autowired
	private UserManagementService userManagementService;

	@Autowired
	SchoolVersionStatusService schoolVersionStatusService;

	@Autowired
	private LogService logService;

	public DashboardController(UserManagementService userManagementService, LogService logService, SchoolVersionStatusService schoolVersionStatusService) {
		this.userManagementService = userManagementService;
		this.logService = logService;
		this.schoolVersionStatusService = schoolVersionStatusService;
	}

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
		User currentUser = (User) model.getAttribute("currentUser");

		switch (role) {
			case "ROLE_superadmin":
				model.addAttribute("content", "SuperAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "SuperAdmin");
				Map<String, int[]> loginCountsByRole = logService.getLoginCountsByHour();
				model.addAttribute("loginCountsByRole", loginCountsByRole);

				Long superAdminCount = userManagementService.getUserCountByRole("superadmin");
				Long stateAdminCount = userManagementService.getUserCountByRole("stateadmin");
				Long ppdAdminCount = userManagementService.getUserCountByRole("ppdadmin");
				Long schoolAdminCount = userManagementService.getUserCountByRole("schooladmin");

				model.addAttribute("superAdminCount", superAdminCount != null ? superAdminCount : 0);
				model.addAttribute("stateAdminCount", stateAdminCount != null ? stateAdminCount : 0);
				model.addAttribute("ppdAdminCount", ppdAdminCount != null ? ppdAdminCount : 0);
				model.addAttribute("schoolAdminCount", schoolAdminCount != null ? schoolAdminCount : 0);

 				break;
			case "ROLE_stateadmin":
				model.addAttribute("content", "StateAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "StateAdmin");

				List<Map<String, Object>> schoolDataList = schoolVersionStatusService.getAllSchool();
				model.addAttribute("schoolDataList", schoolDataList);

				break;
			case "ROLE_ppdadmin":

				model.addAttribute("content", "PpdAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "AdminPPD");
				List<Map<String, Object>> schools = schoolVersionStatusService.getAllSchoolsByDistrict(currentUser.getDistrict());
				model.addAttribute("schoolDataList", schools);

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
