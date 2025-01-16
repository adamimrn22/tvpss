package com.beyondtech.tvpss.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "achievement_table")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateGenerated;

    private String status;

    private String schoolCode;

    @OneToMany(mappedBy = "achievementTable", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<StudentAchievement> studentAchievements;

    public Achievement() {}

    public Achievement(Date dateGenerated, String status, String schoolCode) {
        this.dateGenerated = dateGenerated;
        this.status = status;
        this.schoolCode = schoolCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(Date dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public List<StudentAchievement> getStudentAchievements() {
        return studentAchievements;
    }

    public void setStudentAchievements(List<StudentAchievement> studentAchievements) {
        this.studentAchievements = studentAchievements;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", dateGenerated=" + dateGenerated +
                ", status='" + status + '\'' +
                ", schoolCode='" + schoolCode + '\'' +
                '}';
    }
}
