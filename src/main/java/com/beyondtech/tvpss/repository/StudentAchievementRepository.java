package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.StudentAchievement;

import java.util.List;
import java.util.Optional;

public interface StudentAchievementRepository {
    Optional<StudentAchievement> findById(Long id);
    List<StudentAchievement> findAll();
    List<StudentAchievement> findAllBySchoolId(String schoolCode);
    List<Achievement> findAchievementBySchoolCode(String schoolCode);
    Achievement achievementById(Long id);
    List<StudentAchievement> findStudentAchievementsByAchievementId(Long id);
    void saveAchievement(Achievement achievement);
    void save(StudentAchievement studentAchievement);
    void delete(Achievement achievement);
}