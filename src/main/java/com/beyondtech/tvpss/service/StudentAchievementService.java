package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.StudentAchievement;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.repository.StudentAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentAchievementService {

    private final StudentAchievementRepository studentAchievementRepository;

    @Autowired
    private SchoolService schoolService;


    @Autowired
    public StudentAchievementService(StudentAchievementRepository studentAchievementRepository) {
        this.studentAchievementRepository = studentAchievementRepository;
    }

    public List<Achievement> findAchievementBySchoolCode(String schoolCode) {
        return studentAchievementRepository.findAchievementBySchoolCode(schoolCode);
    }

    public List<StudentAchievement> findStudentAchievementByAchievementId(Long id) {
        return studentAchievementRepository.findStudentAchievementsByAchievementId(id);
    }

    public Optional<StudentAchievement> findByIdAchievement(Long id) {
        return studentAchievementRepository.findById(id);
    }

    public List<StudentAchievement> findAll() {
        return studentAchievementRepository.findAll();
    }

    public List<StudentAchievement> findAllBySchoolId(String schoolCode) {
        return studentAchievementRepository.findAllBySchoolId(schoolCode);
    }

    public Achievement findAchievementById(Long id) {
        return studentAchievementRepository.achievementById(id);
    }

    public void saveAchievement(Achievement achievement) {
        studentAchievementRepository.saveAchievement(achievement);
    }

    public void save(StudentAchievement studentAchievement) {
         studentAchievementRepository.save(studentAchievement);
    }

    public void delete(Achievement achievement) {
        studentAchievementRepository.delete(achievement);
    }


    public List<Map<String, Object>> getAllSchool() {
        // Get all schools from the external API
        List<School> schools = schoolService.getAllSchools();
        System.out.println("All schools: " + schools);

        List<Map<String, Object>> schoolDataList = new ArrayList<>();

        // Loop through all schools
        for (School school : schools) {
            // Get all achievements for this school
            List<Achievement> achievements = studentAchievementRepository.achievementBySchoolCode(school.getCode());

            // Check if the school has any associated achievements
            if (achievements != null && !achievements.isEmpty()) {
                for (Achievement achievement : achievements) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("school", school);
                    map.put("achievement", achievement);
                    schoolDataList.add(map);
                }
            }
        }

        return schoolDataList;
    }


}

