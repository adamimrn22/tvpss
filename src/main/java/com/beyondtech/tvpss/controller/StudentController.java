package com.beyondtech.tvpss.controller;

import com.beyondtech.tvpss.model.Student;
import com.beyondtech.tvpss.response.StudentApiResponse;
import com.beyondtech.tvpss.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/StudentAchievement/addStudentAchievement")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/by-school/{schoolCode}")
    @ResponseBody
    public ResponseEntity<StudentApiResponse> getStudentsBySchoolCode(@PathVariable String schoolCode) {
        // Fetch students based on the schoolCode
        List<Student> students = studentService.getStudentsBySchoolCode(schoolCode);

        System.out.println("the code" + schoolCode);
        System.out.println("the student" + students);

        // Prepare the response
        StudentApiResponse response = new StudentApiResponse();
        response.setSuccess(true);  // Indicate success
        response.setMessage("Data fetched successfully");  // Set the success message
        response.setData(students);  // Set the list of students

        // Return the response wrapped in ResponseEntity (Spring MVC will serialize it to JSON)
        return ResponseEntity.ok(response);
    }
}
