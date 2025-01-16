package com.beyondtech.tvpss.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "student_achievement")
public class StudentAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String studentIdentificationNumber;
    private Date studentDateAchievement;
    private String studentTypeAchievement;
    private String achievementInformation;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievementTable;

    public StudentAchievement() {}

    public StudentAchievement(Long id, String studentName, String studentIdentificationNumber, Date studentDateAchievement, String studentTypeAchievement, String achievementInformation, Achievement achievementTable) {
        this.id = id;
        this.studentName = studentName;
        this.studentIdentificationNumber = studentIdentificationNumber;
        this.studentDateAchievement = studentDateAchievement;
        this.studentTypeAchievement = studentTypeAchievement;
        this.achievementInformation = achievementInformation;
        this.achievementTable = achievementTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentIdentificationNumber() {
        return studentIdentificationNumber;
    }

    public void setStudentIdentificationNumber(String studentIdentificationNumber) {
        this.studentIdentificationNumber = studentIdentificationNumber;
    }

    public Date getStudentDateAchievement() {
        return studentDateAchievement;
    }

    public void setStudentDateAchievement(Date studentDateAchievement) {
        this.studentDateAchievement = studentDateAchievement;
    }

    public String getStudentTypeAchievement() {
        return studentTypeAchievement;
    }

    public void setStudentTypeAchievement(String studentTypeAchievement) {
        this.studentTypeAchievement = studentTypeAchievement;
    }

    public String getAchievementInformation() {
        return achievementInformation;
    }

    public void setAchievementInformation(String achievementInformation) {
        this.achievementInformation = achievementInformation;
    }

    public Achievement getAchievementTable() {
        return achievementTable;
    }

    public void setAchievementTable(Achievement achievementTable) {
        this.achievementTable = achievementTable;
    }
}
