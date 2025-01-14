package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.Student;
import com.beyondtech.tvpss.response.StudentApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final RestTemplate restTemplate;
    private final String apiUrl = "http://localhost:8083/api/students"; // Modify this as per your actual API URL
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    public StudentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Student> getStudentsBySchoolCode(String schoolCode) {
        String getStudentsUrl = apiUrl + "/school/" + schoolCode; // Assuming your API endpoint supports this

        try {
            // Use StudentApiResponse as the wrapper class for deserialization
            ResponseEntity<StudentApiResponse> response = restTemplate.exchange(
                    getStudentsUrl,
                    HttpMethod.GET,
                    null, // No body for GET request
                    StudentApiResponse.class);

            // Check if the response is not null and contains students
            if (response.getBody() != null && response.getBody().isSuccess()) {
                return response.getBody().getData(); // Return the list of students from the 'data' field
            } else {
                logger.error("Failed to fetch students for school code: {}", schoolCode);
                return List.of(); // Return an empty list if no students found
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching students for school code: {}", schoolCode, e);
            return List.of(); // Return an empty list if an exception occurs
        }
    }

    // Existing method for fetching a student by email
    public Student getStudentInfo(String email) {
        String getStudentInfo = apiUrl + "/email/" + email;

        try {
            StudentApiResponse response = restTemplate.getForObject(getStudentInfo, StudentApiResponse.class);
            if (response != null && response.isSuccess()) {
                return response.getData().get(0); // Assuming only one student is returned for the email
            } else {
                logger.error("Failed to fetch student data for email: {}. Error message: {}", email, response != null ? response.getMessage() : "Unknown error");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
