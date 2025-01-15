package com.beyondtech.tvpss.auth;

import com.beyondtech.tvpss.model.PasswordResetToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Repository
@Transactional
public class PasswordRepositoryImpl implements PasswordRepository {
    @Autowired
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(PasswordResetToken token) {
        getCurrentSession().persist(token);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return getCurrentSession()
                .createQuery("FROM PasswordResetToken WHERE token = :token", PasswordResetToken.class)
                .setParameter("token", token)
                .uniqueResult();
    }

    @Override
    public void delete(PasswordResetToken token) {
        getCurrentSession().remove(token);
    }

    public PasswordResetToken findByUserId(Long userId) {
        // Get the current time in Malaysia timezone
        ZonedDateTime nowInMalaysia = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));
        System.out.println("Checking token for userId: " + userId + " at time: " + nowInMalaysia);

        PasswordResetToken token = getCurrentSession()
                .createQuery("from PasswordResetToken where user.id = :userId and expiryDate > :now", PasswordResetToken.class)
                .setParameter("userId", userId)
                .setParameter("now", nowInMalaysia)
                .uniqueResult();

        if (token != null) {
            System.out.println("Found token for userId: " + userId);
        } else {
            System.out.println("No valid token found for userId: " + userId);
        }

        return token;
    }


}
