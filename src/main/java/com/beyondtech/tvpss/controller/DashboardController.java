package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.*;
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

	@Autowired
	EquipmentManagementService equipmentManagementService;

	@Autowired
	TvpssCrewService tvpssCrewService;

	public DashboardController(UserManagementService userManagementService, LogService logService, SchoolVersionStatusService schoolVersionStatusService, EquipmentManagementService equipmentManagementService, TvpssCrewService tvpssCrewService) {
		this.userManagementService = userManagementService;
		this.logService = logService;
		this.schoolVersionStatusService = schoolVersionStatusService;
		this.equipmentManagementService = equipmentManagementService;
		this.tvpssCrewService = tvpssCrewService;
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

				Long crewCount = tvpssCrewService.countTvpssCrewBySchoolAndStatus(currentUser.getSchool().getCode(), ApplicationStatus.APPROVED);
				Long equipmentCount = equipmentManagementService.countAllEquipments(currentUser.getSchool().getCode());
				Integer currentTvpssVersion = schoolVersionStatusService.getTvpssVersion(currentUser.getSchool().getCode());

				model.addAttribute("equipmentCount", equipmentCount != null ? equipmentCount : 0);
				model.addAttribute("crewCount", crewCount != null ? crewCount : 0);
				model.addAttribute("currentTvpssVersion", currentTvpssVersion != null ? currentTvpssVersion : 0);

				break;
			default:
				break;
		}
	}
}
