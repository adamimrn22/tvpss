package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
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
    @Autowired
    private SchoolService schoolService;

//	public DashboardController(UserManagementService userManagementService, LogService logService, SchoolVersionStatusService schoolVersionStatusService, EquipmentManagementService equipmentManagementService, TvpssCrewService tvpssCrewService) {
//		this.userManagementService = userManagementService;
//		this.logService = logService;
//		this.schoolVersionStatusService = schoolVersionStatusService;
//		this.equipmentManagementService = equipmentManagementService;
//		this.tvpssCrewService = tvpssCrewService;
//	}

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

				Long versionZero = schoolVersionStatusService.countTvpssVersion(0L);
				Long versionOne = schoolVersionStatusService.countTvpssVersion(1L);
				Long versionTwo = schoolVersionStatusService.countTvpssVersion(2L);
				Long versionThree = schoolVersionStatusService.countTvpssVersion(3L);
				Long versionFour = schoolVersionStatusService.countTvpssVersion(4L);

				Map<String, Long> districtVersion0Counts = schoolVersionStatusService.countTvpssVersionsByVersion(0);
				Map<String, Long> districtVersion1Counts = schoolVersionStatusService.countTvpssVersionsByVersion(1);
				Map<String, Long> districtVersion2Counts = schoolVersionStatusService.countTvpssVersionsByVersion(2);
				Map<String, Long> districtVersion3Counts = schoolVersionStatusService.countTvpssVersionsByVersion(3);
				Map<String, Long> districtVersion4Counts = schoolVersionStatusService.countTvpssVersionsByVersion(4);

				long schoolInJohorCount = schoolService.countAllSchools();

				model.addAttribute("schoolInJohorCount", schoolInJohorCount);
				model.addAttribute("districtVersion0Counts", districtVersion0Counts);
				model.addAttribute("districtVersion1Counts", districtVersion1Counts);
				model.addAttribute("districtVersion2Counts", districtVersion2Counts);
				model.addAttribute("districtVersion3Counts", districtVersion3Counts);
				model.addAttribute("districtVersion4Counts", districtVersion4Counts);

				model.addAttribute("versionZero", versionZero != null ? versionZero : 0);
				model.addAttribute("versionOne", versionOne != null ? versionOne : 0);
				model.addAttribute("versionTwo", versionTwo != null ? versionTwo : 0);
				model.addAttribute("versionThree", versionThree != null ? versionThree : 0);
				model.addAttribute("versionFour", versionFour != null ? versionFour : 0);
				model.addAttribute("versionFour", versionFour != null ? versionFour : 0);

				break;
			case "ROLE_ppdadmin":

				model.addAttribute("content", "PpdAdmin/dashboard");
				model.addAttribute("currentPageDirectory", "AdminPPD");
				List<Map<String, Object>> schools = schoolVersionStatusService.getAllSchoolsByDistrict(currentUser.getDistrict());
				model.addAttribute("schoolDataList", schools);

				Long schoolVersion0Counts = schoolVersionStatusService.countTvpssVersionsByDistrictAndVersion(currentUser.getDistrict(), 0L);
				Long schoolVersion1Counts = schoolVersionStatusService.countTvpssVersionsByDistrictAndVersion(currentUser.getDistrict(), 1L);
				Long schoolVersion2Counts = schoolVersionStatusService.countTvpssVersionsByDistrictAndVersion(currentUser.getDistrict(), 2L);
				Long schoolVersion3Counts = schoolVersionStatusService.countTvpssVersionsByDistrictAndVersion(currentUser.getDistrict(), 3L);
				Long schoolVersion4Counts = schoolVersionStatusService.countTvpssVersionsByDistrictAndVersion(currentUser.getDistrict(), 4L);

				long schoolCountDistrict = schoolService.countSchoolByDistrict(currentUser.getDistrict());

				Long validatedSchool = schoolVersionStatusService.countTvpssVersionByDistrictAndStatus(currentUser.getDistrict(), TvpssStatus.SUDAH);
				Long pendingSchool = schoolVersionStatusService.countTvpssVersionByDistrictAndStatus(currentUser.getDistrict(), TvpssStatus.PENDING);

				model.addAttribute("validatedSchool", validatedSchool);
				model.addAttribute("pendingSchool", pendingSchool);
				model.addAttribute("schoolCountDistrict", schoolCountDistrict);
 				model.addAttribute("schoolVersion0Counts", schoolVersion0Counts);
				model.addAttribute("schoolVersion1Counts", schoolVersion1Counts);
				model.addAttribute("schoolVersion2Counts", schoolVersion2Counts);
				model.addAttribute("schoolVersion3Counts", schoolVersion3Counts);
				model.addAttribute("schoolVersion4Counts", schoolVersion4Counts);


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

				Map<String, Long> genderCounts = tvpssCrewService.countTvpssCrewByGender(currentUser.getSchool().getCode());
				model.addAttribute("genderCounts", genderCounts);

				List<Long> approvedCrewCounts = tvpssCrewService.getTvpssCrewCountsForPast5Years(currentUser.getSchool().getCode());

				int currentYear = Calendar.getInstance().get(Calendar.YEAR);
				List<Integer> years = List.of(
						currentYear - 5,
						currentYear - 4,
						currentYear - 3,
						currentYear - 2,
						currentYear - 1,
						currentYear
				);

				model.addAttribute("approvedCrewCounts", approvedCrewCounts);
				model.addAttribute("years", years);


				break;
			default:
				break;
		}
	}
}
