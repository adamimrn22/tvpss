package com.beyondtech.tvpss.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.SchoolVersionStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		User currentUser = (User) model.getAttribute("currentUser");

		Map<String,Object> schoolData = schoolVersionStatusService.getSchoolVersionWithSchoolData(currentUser.getSchool().getCode());
		model.addAttribute("currentUser", currentUser);

		model.addAttribute("schoolData", schoolData);
		return "layouts/admin-layouts";
	}

	@PostMapping("/SubmitTVPSSVersion")
	public String submitSchoolVersion(
			@RequestParam("youtubeLink") String youtubeLink,
			@RequestParam("collabAgency1") String collabAgency1,
			@RequestParam("emailAgency1") String emailAgency1,
			@RequestParam("miniStudio") String miniStudio,
			@RequestParam("collabAgency2") String collabAgency2,
			@RequestParam("emailAgency2") String emailAgency2,
			@RequestParam("recordingEquipment") String recordingEquipment,
			@RequestParam("technologyUsage") String technologyUsage,
			@RequestParam("logo") MultipartFile logo, Model model,
			RedirectAttributes redirectAttributes){

		try {

			if(!(recordingEquipment.equals("yes") || recordingEquipment.equals("no")) || !(miniStudio.equals("yes")
				|| miniStudio.equals("no")) || !(technologyUsage.equals("yes") || technologyUsage.equals("no"))) {

				redirectAttributes.addFlashAttribute("error", "Sila pilih Status ada atau tiada");
				return "redirect:/SubmitTVPSSVersion";
			}



			User currentUser = (User) model.getAttribute("currentUser");
			TvpssVersion tvpssVersion = new TvpssVersion();

			if (currentUser != null) {
				tvpssVersion.setPic(currentUser);
				tvpssVersion.setSchoolCode(currentUser.getSchool().getCode());
			}

			tvpssVersion.setYoutubeLink(youtubeLink);
			tvpssVersion.setCollabAgency1(collabAgency1);
			tvpssVersion.setEmailAgency1(emailAgency1);
			tvpssVersion.setCollabAgency2(collabAgency2);
			tvpssVersion.setEmailAgency2(emailAgency2);
			tvpssVersion.setMiniStudio(miniStudio);
			tvpssVersion.setRecordingEquipment(recordingEquipment);
			tvpssVersion.setTechnologyUsage(technologyUsage);

            schoolVersionStatusService.submitTvpssVersion(tvpssVersion, logo);

			redirectAttributes.addFlashAttribute("success", "Data TVPSS Anda Berjaya Dihantar");
			return "redirect:/SubmitTVPSSVersion";

		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
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
			List<Map<String, Object>> schools = schoolVersionStatusService.getAllSchoolsByDistrict(currentUser.getDistrict());
			model.addAttribute("schoolDataList", schools);

			model.addAttribute("currentPageDirectory", "AdminPPDInformasiTVPSS");
			model.addAttribute("headerText", "Info Status TVPSS");
			model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/view-all-school");

		} else if (Objects.equals(role, "stateadmin")) {

			List<Map<String, Object>> schoolDataList = schoolVersionStatusService.getAllSchool();
			model.addAttribute("schoolDataList", schoolDataList);

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

	@GetMapping("/InformasiTVPSS/SchoolValidate/{schoolCode}")
	public String viewValidateSchoolVersion(@PathVariable("schoolCode") String schoolCode, Model model) {
		model.addAttribute("pageTitle", "Validate School Version");
		model.addAttribute("role", "ppdadmin");
		model.addAttribute("currentPageDirectory", "AdminPPDInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");
		model.addAttribute("content", "PpdAdmin/SchoolVersionStatus/validate-school");
		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		Map<String,Object> schoolData = schoolVersionStatusService.getSchoolVersionWithSchoolData(schoolCode);


		TvpssVersion version = (TvpssVersion) schoolData.get("version");

		if (version != null) {
			System.out.println("dataaaa " + version.getTvpssVersion());
		} else {
			System.out.println("Version data is not available");
		}

		model.addAttribute("schoolData", schoolData);

		return "layouts/admin-layouts";
	}

	@PostMapping("/InformasiTVPSS/validate")
	public String updateTvpssStatus(@RequestParam("schoolCode") String schoolCode,@RequestParam("versionStatus") int version, RedirectAttributes redirectAttributes) {
		schoolVersionStatusService.updateTvpssVersion(schoolCode, version);

		redirectAttributes.addFlashAttribute("success", "Status Bagi Kod Sekolah " + schoolCode + " berjaya dikemaskini dengan versi " + version);
		return "redirect:/InformasiTVPSS";
	}

	@GetMapping("/InformasiTVPSS/{schoolCode}")
	public String viewSchoolDetailsStateAdmin(@PathVariable("schoolCode") String schoolCode,Model model) {
		model.addAttribute("pageTitle", "School Details");
		model.addAttribute("role", "stateadmin");
		model.addAttribute("currentPageDirectory", "StateAdminInformasiTVPSS");
		model.addAttribute("headerText", "Info Status TVPSS");

		model.addAttribute("breadcrumbTitle1", "Pengurusan TVPSS");
		model.addAttribute("breadcrumbTitle2", "Butiran Sekolah");

		Map<String,Object> schoolData = schoolVersionStatusService.getSchoolVersionWithSchoolData(schoolCode);
		model.addAttribute("schoolData", schoolData);

		model.addAttribute("content", "StateAdmin/schoolVersionStatus/view-school-status-detail");
		return "layouts/admin-layouts";
	}
}
