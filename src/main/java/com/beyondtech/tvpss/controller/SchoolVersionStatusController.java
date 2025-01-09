package com.beyondtech.tvpss.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.SchoolService;
import com.beyondtech.tvpss.service.SchoolVersionStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SchoolVersionStatusController {

	@Autowired
	private SchoolVersionStatusService schoolVersionStatusService;

	@GetMapping("/SubmitTVPSSVersion")
	public String getSchoolVersion(Model model) {
		model.addAttribute("pageTitle", "TVPSS Submission");
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPageDirectory", "SubmitTVPSSVersion");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "SchoolAdmin/tvpssSubmission/submit-tvpss-version");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Submit Versi TVPSS");

		return "layouts/admin-layouts";
	}

	@PostMapping("/SubmitTVPSSVersion")
	public String submitSchoolVersion(
			@RequestParam("schoolcode") String schoolCode,
			@RequestParam("youtubeLink") String youtubeLink,
			@RequestParam("collabAgency1") String collabAgency1,
			@RequestParam("emailAgency1") String emailAgency1,
			@RequestParam("collabAgency2") String collabAgency2,
			@RequestParam("emailAgency2") String emailAgency2,
			@RequestParam("recordingEquipment") String recordingEquipment,
			@RequestParam("technologyUsage") String technologyUsage,
			@RequestParam("logo") MultipartFile logo,
			RedirectAttributes redirectAttributes){

		try {
			TvpssVersion tvpssVersion = new TvpssVersion();
			tvpssVersion.setSchoolCode(schoolCode);
			tvpssVersion.setYoutubeLink(youtubeLink);
			tvpssVersion.setCollabAgency1(collabAgency1);
			tvpssVersion.setEmailAgency1(emailAgency1);
			tvpssVersion.setCollabAgency2(collabAgency2);
			tvpssVersion.setEmailAgency2(emailAgency2);
			tvpssVersion.setRecordingEquipment(recordingEquipment);
			tvpssVersion.setTechnologyUsage(technologyUsage);

			if (recordingEquipment == null || recordingEquipment.trim().isEmpty() ||
					technologyUsage == null || technologyUsage.trim().isEmpty()) {
				redirectAttributes.addFlashAttribute("errorMessage");
				return "redirect:/SubmitTVPSSVersion";
			}

			schoolVersionStatusService.submitTvpssVersion(tvpssVersion, logo);

			redirectAttributes.addFlashAttribute("success", "success");
			return "redirect:/SubmitTVPSSVersion";

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error");
			return "redirect:/SubmitTVPSSVersion";
		}
	}


	@GetMapping("/InformasiTVPSS")
	public String viewAllSchoolStatus(Model model) throws AccessDeniedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String role = (String) model.getAttribute("role");
		User currentUser = (User) model.getAttribute("currentUser");
		model.addAttribute("pageTitle", "Status TVPSS");

		if (Objects.equals(role, "ppdadmin")) {

            List<School> schools = schoolVersionStatusService.getAllSchools(currentUser.getDistrict());
			model.addAttribute("schools", schools);

			for (School school : schools) {
				System.out.println(school);
			}

			model.addAttribute("currentPageDirectory", "AdminPPDInformasiTVPSS");
			model.addAttribute("headerText", "Info Status TVPSS");
			model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/view-all-school");

		} else if (Objects.equals(role, "stateadmin")) {
			model.addAttribute("currentPageDirectory", "StateAdminInformasiTVPSS");
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
		model.addAttribute("currentPageDirectory", "AdminPPDInformasiTVPSS");
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
		model.addAttribute("currentPageDirectory", "StateAdminInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "StateAdmin/schoolVersionStatus/view-school-status-detail");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		return "layouts/admin-layouts";
	}
}
