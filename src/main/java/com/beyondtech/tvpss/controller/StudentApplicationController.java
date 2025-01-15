package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssCrew;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.TvpssCrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/StudentApplication")
public class StudentApplicationController {

	@Autowired
	TvpssCrewService tvpssCrewService;

//	pending
	@GetMapping("")
	public String viewStudentApplication(Model model) {
		model.addAttribute("pageTitle", "View Student Application");
		model.addAttribute("currentPageDirectory", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-pending");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Pending");

		User currentUser = (User) model.getAttribute("currentUser");
		List<TvpssCrew> tvpssCrews = tvpssCrewService.getAllApplicationBySchoolAndStatus(currentUser.getSchool().getCode(), ApplicationStatus.PENDING);
		model.addAttribute("tvpssCrews", tvpssCrews);

		return "layouts/admin-layouts";
	}

//	validate
	@GetMapping("/validate/{id}")
	public String validateStudentApplication(@PathVariable("id") Long id, Model model) {
		model.addAttribute("pageTitle", "Validate Student Application");
		model.addAttribute("currentPageDirectory", "StudentApplication");
		model.addAttribute("headerText", "Maklumat Pemohon");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Maklumat Pemohon");
		model.addAttribute("breadcrumbTitle3", "Semua Permohonan Pending");
		model.addAttribute("breadcrumbLink1", "/StudentApplication");

		TvpssCrew crew = tvpssCrewService.getApplicationById(id);
		model.addAttribute("crew", crew);


		model.addAttribute("content", "SchoolAdmin/studentApplication/validate-student-application");


		return "layouts/admin-layouts";
	}

//	update
	@PostMapping("/updateStatus")
	public String updateStudentApplicationStatus(@RequestParam("id") Long id,
												 @RequestParam(value = "rejectCause", required = false) String rejectCause,
												 @RequestParam(value = "ApplicationStatusSelect", required = false) String status,
												 RedirectAttributes redirectAttributes) {
		try {

			if(status == null || status.isEmpty()) {
				throw new IllegalArgumentException("Please select status");
			}

			TvpssCrew crew = tvpssCrewService.getApplicationById(id);

			if(status.equals(ApplicationStatus.REJECTED.toString())) {
				crew.setStatus(ApplicationStatus.REJECTED);
				crew.setRejectCause(rejectCause);
				tvpssCrewService.updateApplication(crew);
				redirectAttributes.addFlashAttribute("success", "Status Dikemaskini");
				return "redirect:/StudentApplication/Rejected";
			}else if (status.equals(ApplicationStatus.APPROVED.toString())) {
				crew.setStatus(ApplicationStatus.APPROVED);
				tvpssCrewService.updateApplication(crew);
				redirectAttributes.addFlashAttribute("success", "Status Dikemaskini ");
				return "redirect:/StudentApplication/Approved";
			}else {
				throw new IllegalArgumentException("Please enter valid status");
			}

		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/StudentApplication/validate/" + id;
		}
	}

// Approved
	@GetMapping("/Approved")
	public String viewStudentApplicationApprove(Model model) {
		model.addAttribute("pageTitle", "Student Approved");
		model.addAttribute("currentPageDirectory", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-approved");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Lulus");

		User currentUser = (User) model.getAttribute("currentUser");
		List<TvpssCrew> tvpssCrews = tvpssCrewService.getAllApplicationBySchoolAndStatus(currentUser.getSchool().getCode(), ApplicationStatus.APPROVED);
		model.addAttribute("tvpssCrews", tvpssCrews);

		return "layouts/admin-layouts";
	}

// Rejected
	@GetMapping("/Rejected")
	public String viewStudentApplicationRejected(Model model) {
		model.addAttribute("pageTitle", "Student Rejected");
		model.addAttribute("currentPageDirectory", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-all-student-rejected");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");
		model.addAttribute("breadcrumbTitle2", "Semua Permohonan Ditolak");

		User currentUser = (User) model.getAttribute("currentUser");
		List<TvpssCrew> tvpssCrews = tvpssCrewService.getAllApplicationBySchoolAndStatus(currentUser.getSchool().getCode(), ApplicationStatus.REJECTED);
		model.addAttribute("tvpssCrews", tvpssCrews);

		return "layouts/admin-layouts";
	}

//	Detail
	@GetMapping("/Detail/{id}")
	public String viewStudentApplicationApproveDetail(@PathVariable("id") Long id, Model  model) {
		TvpssCrew crew = tvpssCrewService.getApplicationById(id);

		if (crew.getStatus() == ApplicationStatus.APPROVED) {
			model.addAttribute("pageTitle", "Student Approved  Details");
			model.addAttribute("breadcrumbLink1", "/StudentApplication/Approved");
			model.addAttribute("breadcrumbTitle2", "Semua Permohonan Lulus");
		}

		if (crew.getStatus() == ApplicationStatus.REJECTED) {
			model.addAttribute("pageTitle", "Student Rejected  Details");
			model.addAttribute("breadcrumbLink1", "/StudentApplication/Rejected");
			model.addAttribute("breadcrumbTitle2", "Semua Permohonan Ditolak");
		}

		model.addAttribute("breadcrumbTitle3", crew.getName());

		model.addAttribute("currentPageDirectory", "StudentApplication");
		model.addAttribute("headerText", "Permohonan Krew");
		model.addAttribute("content", "SchoolAdmin/studentApplication/view-student-application-detail");
		model.addAttribute("breadcrumbTitle1", "Permohonan Krew");

		model.addAttribute("crew", crew);

		return "layouts/admin-layouts";
	}
}
