package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.StudentAchievement;
import com.beyondtech.tvpss.repository.StudentAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentAchievementService {

    private final StudentAchievementRepository studentAchievementRepository;

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
}

