package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/GenerateCertificate")
public class GenerateCertificateController {

	@GetMapping("")
	public String viewAllCert(Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/view-all-generate-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		return "layouts/admin-layouts";
	}

	@GetMapping("/certGen")
	public String generateCert(Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/generate-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		return "layouts/admin-layouts";
	}

	@GetMapping("/viewGeneratedCert")
	public String viewGeneratedCertDetail(Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/view-generated-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		return "layouts/admin-layouts";
	}
}
