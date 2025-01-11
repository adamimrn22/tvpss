package com.beyondtech.tvpss.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "school_tvpss")
public class TvpssVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolCode;

    private String youtubeLink;

    private String collabAgency1;

    private String emailAgency1;

    private String collabAgency2;

    private String emailAgency2;

    @Enumerated(EnumType.STRING) // This ensures the enum is stored as a String in the database
    private TvpssStatus tvpssCurrentStatus = TvpssStatus.PENDING;

    private String miniStudio;

    private String recordingEquipment;

    private String technologyUsage;

    private String logoPath;

    private int tvpssVersion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pic", referencedColumnName = "id", nullable = false)
    private User pic;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getter and Setter methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getCollabAgency1() {
        return collabAgency1;
    }

    public void setCollabAgency1(String collabAgency1) {
        this.collabAgency1 = collabAgency1;
    }

    public String getEmailAgency1() {
        return emailAgency1;
    }

    public void setEmailAgency1(String emailAgency1) {
        this.emailAgency1 = emailAgency1;
    }

    public String getCollabAgency2() {
        return collabAgency2;
    }

    public void setCollabAgency2(String collabAgency2) {
        this.collabAgency2 = collabAgency2;
    }

    public String getEmailAgency2() {
        return emailAgency2;
    }

    public void setEmailAgency2(String emailAgency2) {
        this.emailAgency2 = emailAgency2;
    }

    public TvpssStatus getTvpssCurrentStatus() {
        return tvpssCurrentStatus;
    }

    public void setTvpssCurrentStatus(TvpssStatus tvpssCurrentStatus) {
        this.tvpssCurrentStatus = tvpssCurrentStatus;
    }

    public String getRecordingEquipment() {
        return recordingEquipment;
    }

    public void setRecordingEquipment(String recordingEquipment) {
        this.recordingEquipment = recordingEquipment;
    }

    public String getTechnologyUsage() {
        return technologyUsage;
    }

    public void setTechnologyUsage(String technologyUsage) {
        this.technologyUsage = technologyUsage;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTvpssVersion() {
        return tvpssVersion;
    }

    public void setTvpssVersion(int tvpssVersion) {
        this.tvpssVersion = tvpssVersion;
    }

    public String getMiniStudio() {
        return miniStudio;
    }

    public void setMiniStudio(String miniStudio) {
        this.miniStudio = miniStudio;
    }

    public User getPic() {
        return pic;
    }

    public void setPic(User user) {
        this.pic = user;
    }
}