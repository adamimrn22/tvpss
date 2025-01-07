package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.SchoolNameAndCode;
import com.beyondtech.tvpss.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/SuperAdmin/schools/district/{district}")
    public List<SchoolNameAndCode> getSchoolsByDistrict(@PathVariable("district") String district) {
        return schoolService.getSchoolNamesAndCodesByDistrict(district);
    }

 
}
