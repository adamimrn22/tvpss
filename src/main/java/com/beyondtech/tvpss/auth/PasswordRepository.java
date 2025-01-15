package com.beyondtech.tvpss.auth;


import com.beyondtech.tvpss.model.PasswordResetToken;

public interface PasswordRepository {
    void save(PasswordResetToken token);
    PasswordResetToken findByToken(String token);
    void delete(PasswordResetToken token);
    PasswordResetToken findByUserId(Long userId);
}
