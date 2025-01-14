package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.Student;
import com.beyondtech.tvpss.model.TvpssCrew;
import com.beyondtech.tvpss.repository.TvpssCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TvpssCrewService {
    @Autowired
    TvpssCrewRepository tvpssCrewRepository;

    private final StudentService studentService;

    public TvpssCrewService(TvpssCrewRepository tvpssCrewRepository, StudentService studentService) {
        this.tvpssCrewRepository = tvpssCrewRepository;
        this.studentService = studentService;
    }

    public List<TvpssCrew> getAllApplicationBySchoolAndStatus(String schoolCode, ApplicationStatus status) {
        return tvpssCrewRepository.getAllAplicationBySchool(schoolCode, status);
    }

    public TvpssCrew getApplicationById(Long id) {
        return tvpssCrewRepository.getCrewById(id);
    }

    public void updateApplication(TvpssCrew tvpssCrew) {
        tvpssCrewRepository.updateStatus(tvpssCrew);
    }

    public Long countTvpssCrewBySchoolAndStatus(String schoolCode, ApplicationStatus status) {
        return tvpssCrewRepository.countApplicationBySchool(schoolCode, status);
    }

    public List<Long> getTvpssCrewCountsForPast5Years() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Get the current year
        int startYear = currentYear - 5;  // 5 years ago
        List<Long> counts = new ArrayList<>();

        // Loop through the last 5 years and get the count for each year
        for (int year = startYear; year <= currentYear; year++) {
            long count = tvpssCrewRepository.countTvpssCrewByYearRangeAndStatus(year, ApplicationStatus.APPROVED);
            counts.add(count);
        }

        return counts;
    }


    public Map<String, Long> countTvpssCrewByGender(String schoolCode) {
        // Fetch all students from the external API for the given school code
        List<Student> students = studentService.getStudentsBySchoolCode(schoolCode);

        if (students.isEmpty()) {
            throw new IllegalArgumentException("No students found for school code: " + schoolCode);
        }

        // Extract identificationNumbers of students
        List<String> identificationNumbers = students.stream()
                .map(Student::getIdentificationNumber)
                .collect(Collectors.toList());

        // Fetch TvpssCrew entries based on identificationNumbers
        List<TvpssCrew> tvpssCrews = tvpssCrewRepository.findApprovedByIdentificationNumbers(identificationNumbers);

        // Create a map of identificationNumber -> gender
        Map<String, String> identificationNumberToGender = students.stream()
                .collect(Collectors.toMap(Student::getIdentificationNumber, Student::getGender));

        // Create a map to count tvpssCrew entries by gender
        Map<String, Long> genderCounts = new HashMap<>();

        // Count the occurrences based on gender
        for (TvpssCrew tvpssCrew : tvpssCrews) {
            String gender = identificationNumberToGender.get(tvpssCrew.getIdentificationNumber());
            genderCounts.put(gender, genderCounts.getOrDefault(gender, 0L) + 1);
        }

        return genderCounts;
    }
}
