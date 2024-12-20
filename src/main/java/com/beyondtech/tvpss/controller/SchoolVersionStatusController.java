package com.beyondtech.tvpss.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchoolVersionStatusController {

	@GetMapping("/InformasiTVPSS")
	public String viewAllSchoolStatus(Model model) throws AccessDeniedException {
		String role = "ppdadmin";
		model.addAttribute("pageTitle", "Status TVPSS");
		if (role == "ppdadmin") {
			model.addAttribute("role", "ppdadmin");
			model.addAttribute("currentPage", "AdminPPDInformasiTVPSS");
			model.addAttribute("headerText", "Info Status TVPSS");
			model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/view-all-school");
		} else if (role == "stateadmin") {
			model.addAttribute("role", "stateadmin");
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

		model.addAttribute("role", "stateadmin");
		model.addAttribute("currentPage", "StateAdminInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "StateAdmin/schoolVersionStatus/view-school-status-detail");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		return "layouts/admin-layouts";
	}
}
