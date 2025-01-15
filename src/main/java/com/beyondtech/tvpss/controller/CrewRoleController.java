package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.TvpssPosition;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.TvpssPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
@RequestMapping("/CrewRole")
public class CrewRoleController {

    @Autowired
    private TvpssPositionService tvpssPositionService;


    @GetMapping("")
    public String showAllCrew(Model model){
        model.addAttribute("pageTitle", "Crew Management Roles");
        model.addAttribute("currentPageDirectory", "crewrole");
        model.addAttribute("headerText", "Pengurusan Jawatan Tvpss");
        model.addAttribute("content", "SchoolAdmin/crewRole/view-all-role");
        model.addAttribute("breadcrumbTitle1", "Pengurusan Jawatan Tvpss");
        model.addAttribute("breadcrumbTitle2", "Semua Jawatan");

        User currentUser = (User) model.getAttribute("currentUser");
        List<TvpssPosition> positions = tvpssPositionService.getAllCrewBySchoolCode(currentUser.getSchool().getCode());
        model.addAttribute("positions", positions);

        return "layouts/admin-layouts";
    }

    @PostMapping("/add")
    public String addCrewRole(@RequestParam("name") String name, Model model, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) model.getAttribute("currentUser");

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Nama jawatan perlu diletak");
            }

            TvpssPosition position = new TvpssPosition();
            position.setName(name);
            position.setSchoolCode(currentUser.getSchool().getCode());
            tvpssPositionService.addCrew(position);

            redirectAttributes.addFlashAttribute("successMessage", "Jawatan berjaya ditambah!");
            return "redirect:/CrewRole";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/CrewRole";
        }
    }

    @GetMapping("/ajax/{crewRoleId}")
    @ResponseBody
    public TvpssPosition getEquipmentById(@PathVariable Long crewRoleId) {
         return tvpssPositionService.getPositionDetail(crewRoleId);
    }

    @PostMapping("/update")
    public String updateEquipment(@RequestParam("crewRoleID") Long id,
                                  @RequestParam("crewRoleName") String name,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) model.getAttribute("currentUser");

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Equipment name is required.");
            }

            TvpssPosition position = tvpssPositionService.getPositionDetail(id);
            if (position != null) {
                position.setName(name);
                tvpssPositionService.updateCrew(position);
                redirectAttributes.addFlashAttribute("successMessage", "Jawatan berjaya dikemaskini!");
            }else {
                redirectAttributes.addFlashAttribute("errorMessage", "Jawatan Tidak dijumpai!");
            }

            return "redirect:/CrewRole";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Jawatan Tidak dijumpai!");
            return "redirect:/CrewRole";
        }
    }

    @PostMapping("/delete")
    public String deleteCrewRole(@RequestParam("crewRoleToDelete") Long id, RedirectAttributes redirectAttributes) {
        try {
            TvpssPosition position= tvpssPositionService.getPositionDetail(id);
            if (position != null) {
                 tvpssPositionService.deleteCrew(position);
                redirectAttributes.addFlashAttribute("successMessage", "Jawatan berjaya dibuang!");
            }else {
                redirectAttributes.addFlashAttribute("errorMessage", "Jawatan Tidak dijumpai!");
            }
            return "redirect:/CrewRole";
        }catch(Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/CrewRole";
        }
    }

}
