package com.beyondtech.tvpss.model;

import jakarta.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private ZonedDateTime expiryDate;

    private static final int EXPIRATION_HOURS = 1;

    // Define a ZoneId for Malaysia to avoid redundant code
    private static final ZoneId MALAYSIA_TIMEZONE = ZoneId.of("Asia/Kuala_Lumpur");

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    private ZonedDateTime calculateExpiryDate() {
        // Set the expiry date to 1 hour ahead in Malaysia's timezone
        return ZonedDateTime.now(MALAYSIA_TIMEZONE).plusHours(EXPIRATION_HOURS);
    }

    public boolean isExpired() {
        ZonedDateTime nowInMalaysia = ZonedDateTime.now(MALAYSIA_TIMEZONE);
        System.out.println("Current time: " + nowInMalaysia);
        System.out.println("Expiry time: " + expiryDate);
        return nowInMalaysia.isAfter(expiryDate);
    }


    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public ZonedDateTime getExpiryDate() {
        return expiryDate;
    }
}
