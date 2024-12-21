package com.beyondtech.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/EquipmentManagement")
public class ManageEquipmentController {
	@GetMapping("")
	public String viewAllEquipment(Model model) {
		model.addAttribute("pageTitle", "Equipment Management");
		model.addAttribute("role", "schooladmin");
		model.addAttribute("currentPage", "EquipmentManagement");
		model.addAttribute("headerText", "Pengurusan Barang");
		model.addAttribute("content", "SchoolAdmin/manageEquipment/view-all-equipment");
		model.addAttribute("breadcrumbTitle1", "Pengurusan Barang");
		model.addAttribute("breadcrumbTitle2", "Semua Barang");

		return "layouts/admin-layouts";
	}

}
