package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/StudentAchievement")
public class StudentAchievementController {

	@GetMapping("")
	public String viewAllStudentAchievement(Model model) {
		model.addAttribute("pageTitle", "View Student Achievement");
		model.addAttribute("currentPageDirectory", "StudentAchievement");
		model.addAttribute("headerText", "Pencapaian Pelajar");
		model.addAttribute("content", "SchoolAdmin/studentAchievement/view-all-student-achievement");
		model.addAttribute("breadcrumbTitle1", "Pencapai Pelajar");
		model.addAttribute("breadcrumbTitle2", "Semua Pencapaian");

		return "layouts/admin-layouts";
	}

	@GetMapping("/addStudentAchievement")
	public String addStudentAchievement(Model model) {
		model.addAttribute("pageTitle", "View Student Achievement");
		model.addAttribute("currentPageDirectory", "StudentAchievement");
		model.addAttribute("headerText", "Pencapaian Pelajar");
		model.addAttribute("content", "SchoolAdmin/studentAchievement/add-student-achievement");
		model.addAttribute("breadcrumbTitle1", "Pencapai Pelajar");
		model.addAttribute("breadcrumbTitle2", "Semua Pencapaian");

		User currentUser = (User) model.getAttribute("currentUser");
		model.addAttribute("schoolCode", currentUser.getSchool().getCode());

		return "layouts/admin-layouts";
	}

	@GetMapping("/viewStudentAchievementDetail")
	public String viewStudentAchievementDetail(Model model) {
		model.addAttribute("pageTitle", "View Student Achievement Detail");
		model.addAttribute("currentPageDirectory", "StudentAchievement");
		model.addAttribute("headerText", "Pencapaian Pelajar");
		model.addAttribute("content", "SchoolAdmin/studentAchievement/view-student-achievement-detail");
		model.addAttribute("breadcrumbTitle1", "Pencapai Pelajar");
		model.addAttribute("breadcrumbTitle2", "KAE121");

		return "layouts/admin-layouts";
	}
}
