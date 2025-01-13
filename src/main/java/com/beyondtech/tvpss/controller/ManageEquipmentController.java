package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.Equipment;
import com.beyondtech.tvpss.model.EquipmentStatus;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.EquipmentManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/EquipmentManagement")
public class ManageEquipmentController {

	@Autowired
	EquipmentManagementService equipmentManagementService;

	public ManageEquipmentController(EquipmentManagementService equipmentManagementService) {
		this.equipmentManagementService = equipmentManagementService;
	}

	@GetMapping("")
	public String viewAllEquipment(Model model) {
		model.addAttribute("pageTitle", "Equipment Management");
		model.addAttribute("currentPageDirectory", "EquipmentManagement");
		model.addAttribute("headerText", "Pengurusan Barang");
		model.addAttribute("content", "SchoolAdmin/manageEquipment/view-all-equipment");
		model.addAttribute("breadcrumbTitle1", "Pengurusan Barang");
		model.addAttribute("breadcrumbTitle2", "Semua Barang");

		User currentUser = (User) model.getAttribute("currentUser");

		List<Equipment> equipments = equipmentManagementService.getAllEquipmentsBySchoolCode(currentUser.getSchool().getCode());

		model.addAttribute("equipments", equipments);


		return "layouts/admin-layouts";
	}

	@PostMapping("/add")
	public String addEquipment(@RequestParam("equipmentName") String name,
							   @RequestParam("equipmentType") String type,
							   @RequestParam("location") String location,
							   @RequestParam("dateAdded") Date date,
							   @RequestParam("equipmentStatus") EquipmentStatus status,
							   Model model,
							   RedirectAttributes redirectAttributes
	) {

		try {
			User currentUser = (User) model.getAttribute("currentUser");

			if (name == null || name.isEmpty()) {
				throw new IllegalArgumentException("Equipment name is required.");
			}
			if (type == null || type.isEmpty()) {
				throw new IllegalArgumentException("Equipment type is required.");
			}

			if (location == null || location.isEmpty()) {
				throw new IllegalArgumentException("Location is required.");
			}

			if (date == null) {
				throw new IllegalArgumentException("Date is required.");
			}

			if (status == null) {
				throw new IllegalArgumentException("Status is required.");
			}

			Equipment equipment = new Equipment();
			equipment.setEquipmentName(name);
			equipment.setEquipmentType(type);
			equipment.setLocation(location);
			equipment.setDateAdded(date);
			equipment.setStatus(status);
			equipment.setSchoolCode(currentUser.getSchool().getCode());
			equipmentManagementService.saveOrUpdateEquipment(equipment);

			redirectAttributes.addFlashAttribute("successMessage", "Equipment added successfully!");
			return "redirect:/EquipmentManagement";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/EquipmentManagement";
		}
	}

	@GetMapping("/ajax/{equipmentId}")
	@ResponseBody
	public Map<Long, Equipment> getEquipmentById(@PathVariable Long equipmentId) {
		System.out.println("Received equipmentId: " + equipmentId); // Debug log
        return equipmentManagementService.getEquipmentById(equipmentId);
	}


	@PostMapping("/delete")
	public String deleteEquipment(@RequestParam("equipmentIDToDelete") Long id, RedirectAttributes redirectAttributes) {
		try {
			Map<Long, Equipment> equipmentMap = equipmentManagementService.getEquipmentById(id);
			if (!equipmentMap.isEmpty()) {
				Equipment equipment = equipmentMap.get(id);
				equipmentManagementService.deleteEquipment(equipment);
				redirectAttributes.addFlashAttribute("successMessage", "Equipment deleted successfully!");
			}else {
				redirectAttributes.addFlashAttribute("errorMessage", "Equipment does not exist!");
			}
			return "redirect:/EquipmentManagement";
		}catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/EquipmentManagement";
		}
	}
}
