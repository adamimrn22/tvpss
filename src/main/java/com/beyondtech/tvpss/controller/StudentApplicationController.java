package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/StudentApplication")
public class StudentApplicationController {

	@GetMapping("")
	public String viewStudentApplication(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-pending");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Pending");

		return "layouts/admin-layouts";
	}

	@GetMapping("/validate")
	public String validateStudentApplication(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Maklumat Pemohon");
		model.addAttribute("content", "SchoolAdmin/studentApplication/validate-student-application");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Maklumat Pemohon");
		model.addAttribute("breadcrumbTitle3", "Semua Permohonan Pending");
		model.addAttribute("breadcrumbLink1", "/StudentApplication");
		return "layouts/admin-layouts";
	}

	@GetMapping("/Approved")
	public String viewStudentApplicationApprove(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-approved");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Lulus");

		return "layouts/admin-layouts";
	}

	@GetMapping("/Approved/Detail")
	public String viewStudentApplicationApproveDetail(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-student-application-approve-detail");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbLink1", "/StudentApplication/Approved");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Lulus");
		model.addAttribute("breadcrumbTitle3", "Ahmad");

		return "layouts/admin-layouts";
	}

	@GetMapping("/Rejected")
	public String viewStudentApplicationRejected(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-rejected");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Ditolak");

		return "layouts/admin-layouts";
	}

	@GetMapping("/Rejected/Detail")
	public String viewStudentApplicationRejectedDetail(Model model) {
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-student-application-rejected-detail");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbLink1", "/StudentApplication/Rejected");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Ditolak");
		model.addAttribute("breadcrumbTitle3", "Ahmad");

		return "layouts/admin-layouts";
	}

}
