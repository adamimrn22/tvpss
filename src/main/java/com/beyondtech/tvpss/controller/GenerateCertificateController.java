package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/GenerateCertifcate")
public class GenerateCertificateController {
	@GetMapping("/certGen")
	public String viewAllEquipment(Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("role", "stateadmin");
		model.addAttribute("currentPage", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/generate-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		return "layouts/admin-layouts";
	}
}
