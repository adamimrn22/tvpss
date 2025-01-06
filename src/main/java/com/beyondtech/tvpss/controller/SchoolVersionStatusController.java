package com.beyondtech.tvpss.controller;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchoolVersionStatusController {

	@GetMapping("/SubmitTVPSSVersion")
	public String submitSchoolVersion(Model model) {
		model.addAttribute("pageTitle", "TVPSS Submission");
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "SubmitTVPSSVersion");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "SchoolAdmin/tvpssSubmission/submit-tvpss-version");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Submit Versi TVPSS");

		return "layouts/admin-layouts";
	}

	@GetMapping("/InformasiTVPSS")
	public String viewAllSchoolStatus(Model model) throws AccessDeniedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String role = (String) model.getAttribute("role");
		model.addAttribute("pageTitle", "Status TVPSS");
		if (Objects.equals(role, "ppdadmin")) {
			model.addAttribute("currentPage", "AdminPPDInformasiTVPSS");
			model.addAttribute("headerText", "Info Status TVPSS");
			model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/view-all-school");

		} else if (Objects.equals(role, "stateadmin")) {
			model.addAttribute("currentPage", "StateAdminInformasiTVPSS");
			model.addAttribute("headerText", "Info Status TVPSS");
			model.addAttribute("content", "StateAdmin/schoolVersionStatus/view-all-school");
		} else {
			throw new AccessDeniedException("Access Denied");
		}

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Semua Sekolah");

		return "layouts/admin-layouts";
	}

	@GetMapping("/InformasiTVPSS/SchoolValidate")
	public String viewValidateSchoolVersion(Model model) {
		model.addAttribute("pageTitle", "Validate School Version");
		model.addAttribute("role", "ppdadmin");
		model.addAttribute("currentPage", "AdminPPDInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/validate-school");
		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		return "layouts/admin-layouts";
	}

	@GetMapping("/InformasiTVPSS/SchoolDetails")
	public String viewSchoolDetailsStateAdmin(Model model) {
		model.addAttribute("pageTitle", "School Details");
		model.addAttribute("role", "stateadmin");
		model.addAttribute("currentPage", "StateAdminInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "StateAdmin/schoolVersionStatus/view-school-status-detail");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		return "layouts/admin-layouts";
	}
}
