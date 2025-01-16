package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.Student;
import com.beyondtech.tvpss.model.StudentAchievement;
import com.beyondtech.tvpss.service.SchoolService;
import com.beyondtech.tvpss.service.SchoolVersionStatusService;
import com.beyondtech.tvpss.service.StudentAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/GenerateCertificate")
public class GenerateCertificateController {

	@Autowired
	StudentAchievementService studentAchievementService;

	@Autowired
	SchoolVersionStatusService schoolVersionStatusService;

	@GetMapping("")
	public String viewAllCert(Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/view-all-generate-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		List<Map<String, Object>> schoolData = studentAchievementService.getAllSchool();
		System.out.println("test data " + schoolData);
		model.addAttribute("schoolData", schoolData);
		return "layouts/admin-layouts";
	}

	@GetMapping("/certGen/{id}/{schoolCode}")
	public String generateCertificate(@PathVariable("id") Long id, @PathVariable("schoolCode") String schoolCode, Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/generate-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		Map<String,Object> schoolData = schoolVersionStatusService.getSchoolVersionWithSchoolData(schoolCode);
		model.addAttribute("schoolData", schoolData);

		List<StudentAchievement> studentAchievements = studentAchievementService.findStudentAchievementByAchievementId(id);
		model.addAttribute("studentAchievements", studentAchievements);


		return "layouts/admin-layouts";
	}

	@GetMapping("/viewGeneratedCert/{id}")
	public String viewGeneratedCertDetail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("pageTitle", "Generate Certificate");
		model.addAttribute("currentPageDirectory", "GenerateCertificate");
		model.addAttribute("headerText", "Jana Sijil Pelajar");
		model.addAttribute("content", "StateAdmin/generateCertificate/view-generated-certificate");
		model.addAttribute("breadcrumbTitle1", "Jana Sijil Pelajar");
		model.addAttribute("breadcrumbTitle2", "KEA0212");

		List<StudentAchievement> studentAchievements = studentAchievementService.findStudentAchievementByAchievementId(id);
		System.out.println("test data " + studentAchievements);
		model.addAttribute("studentAchievements", studentAchievements);
		return "layouts/admin-layouts";
	}
}
