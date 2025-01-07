package com.beyondtech.tvpss.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_school")
public class UserSchool {

//    ALTER TABLE user_school
//    DROP FOREIGN KEY FKmyjs8jnfhemn4vnfypdcla7nk;  -- Drop the current constraint
//
//    ALTER TABLE user_school
//    ADD CONSTRAINT FKmyjs8jnfhemn4vnfypdcla7nk
//    FOREIGN KEY (user_id)
//    REFERENCES user(id)
//    ON DELETE CASCADE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "school_code", nullable = false)
    private String schoolCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user; 
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }
}
