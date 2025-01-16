package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.AchievementStatus;
import com.beyondtech.tvpss.model.StudentAchievement;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.StudentAchievementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/StudentAchievement")
public class StudentAchievementController {

    @Autowired
    StudentAchievementService studentAchievementService;

	@GetMapping("")
	public String viewAllStudentAchievement(Model model) {
		model.addAttribute("pageTitle", "View Student Achievement");
		model.addAttribute("currentPageDirectory", "StudentAchievement");
		model.addAttribute("headerText", "Pencapaian Pelajar");
		model.addAttribute("content", "SchoolAdmin/studentAchievement/view-all-student-achievement");
		model.addAttribute("breadcrumbTitle1", "Pencapai Pelajar");
		model.addAttribute("breadcrumbTitle2", "Semua Pencapaian");
		User currentUser = (User) model.getAttribute("currentUser");

		List<Achievement> achievements = studentAchievementService.findAchievementBySchoolCode(currentUser.getSchool().getCode());

		System.out.println("test " + achievements);

		model.addAttribute("achievements", achievements);

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

	@PostMapping("/addStudentAchievement/save")
	public String saveAchievements(@ModelAttribute("achievementsStudent") String achievementsJson, Model model) throws JsonProcessingException {
		// Step 1: Deserialize the JSON string into a list of StudentAchievement objects
		ObjectMapper objectMapper = new ObjectMapper();
		List<StudentAchievement> studentAchievements = objectMapper.readValue(achievementsJson, new TypeReference<List<StudentAchievement>>() {});

		// Step 2: Create and persist the Achievement entity first
		User currentUser = (User) model.getAttribute("currentUser");
		Achievement achievement = new Achievement();
		LocalDate localDate = LocalDate.now(); // Get the current date without time
		Date sqlDate = Date.valueOf(localDate);
		achievement.setDateGenerated(sqlDate);
		achievement.setSchoolCode(currentUser.getSchool().getCode());
		achievement.setStatus("PENDING");

		// Persist the Achievement entity (save to DB)
		studentAchievementService.saveAchievement(achievement); // Assuming you have a service method to save Achievement

		// Step 3: Iterate over StudentAchievement entities and associate them with the persisted Achievement
		for (StudentAchievement studentAchievement : studentAchievements) {
			// Create new StudentAchievement object and set its fields
			StudentAchievement studentAchievement1 = new StudentAchievement();
			studentAchievement1.setStudentName(studentAchievement.getStudentName());
			studentAchievement1.setStudentIdentificationNumber(studentAchievement.getStudentIdentificationNumber());
			studentAchievement1.setAchievementInformation(studentAchievement.getAchievementInformation());
			studentAchievement1.setStudentTypeAchievement(studentAchievement.getStudentTypeAchievement());
			studentAchievement1.setStudentDateAchievement(studentAchievement.getStudentDateAchievement());
			studentAchievement1.setAchievementInformation(studentAchievement.getAchievementInformation());

			// Set the previously persisted Achievement entity
			studentAchievement1.setAchievementTable(achievement);

			// Persist the StudentAchievement entity (save to DB)
			studentAchievementService.save(studentAchievement1); // Assuming you have a service method to save StudentAchievement
		}

		// Step 4: Redirect to the list of student achievements
		return "redirect:/StudentAchievement";
	}


	@GetMapping("/viewStudentAchievementDetail/{id}")
	public String viewStudentAchievementDetail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("pageTitle", "View Student Achievement Detail");
		model.addAttribute("currentPageDirectory", "StudentAchievement");
		model.addAttribute("headerText", "Pencapaian Pelajar");
		model.addAttribute("content", "SchoolAdmin/studentAchievement/view-student-achievement-detail");
		model.addAttribute("breadcrumbTitle1", "Pencapai Pelajar");
		model.addAttribute("breadcrumbTitle2", "KAE121");

		List<StudentAchievement> studentAchievements = studentAchievementService.findStudentAchievementByAchievementId(id);
		model.addAttribute("studentAchievements", studentAchievements);

		return "layouts/admin-layouts";
	}

	@PostMapping("/delete")
	public String deleteAchievement(@RequestParam("achievementIDToDelete") Long achievementId, Model model) {
		Achievement achievement = studentAchievementService.findAchievementById(achievementId);
		if (achievement != null) {
			studentAchievementService.delete(achievement);
		}
		return "redirect:/StudentAchievement";
	}

}
