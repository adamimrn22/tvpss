package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchoolInformationController {

	@GetMapping("/SchoolInformation")
	public String viewSchoolInformation(Model model) {
		model.addAttribute("pageTitle", "School Information");
		model.addAttribute("currentPageDirectory", "SchoolInformation");
		model.addAttribute("headerText", "Informasi Sekolah");
		model.addAttribute("content", "SchoolAdmin/school-information");
		model.addAttribute("breadcrumbTitle1", "Pengurusan Sekolah");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		return "layouts/admin-layouts";
	}
}
