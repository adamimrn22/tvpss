package com.beyondtech.tvpss.auth;

import com.beyondtech.tvpss.model.PasswordResetToken;
import com.beyondtech.tvpss.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetService {

    private User user;

    @Autowired
    private PasswordRepository passTokenDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createPasswordResetTokenForUser(Optional<User> user, String token) {
        ZonedDateTime expiryDate = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")).plusHours(1); // Set expiry to 1 hour
        PasswordResetToken myToken = new PasswordResetToken(token, user.get());
        passTokenDao.save(myToken);
    }


    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = passTokenDao.findByToken(token);

        if (passToken == null) {
            return "invalidToken";
        }

        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime nowInMalaysia = ZonedDateTime.now(systemTimeZone);

        // Check if the token has expired
        if (passToken.getExpiryDate().isBefore(nowInMalaysia)) {
            passTokenDao.delete(passToken);
            return "expired";
        }

        return null;
    }
    public PasswordResetToken findByToken(String token) {
        return passTokenDao.findByToken(token);
    }

    public void deleteToken(PasswordResetToken token) {
        passTokenDao.delete(token);
    }

    public PasswordResetToken findByUserId(Long userId) {
        return passTokenDao.findByUserId(userId);
    }
}