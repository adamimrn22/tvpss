package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.UserSchool;
import com.beyondtech.tvpss.repository.UserSchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSchoolService {

    @Autowired
    private UserSchoolRepository userSchoolRepository;

    public Optional<UserSchool> getUserSchoolByUserId(Long userId) {
        return userSchoolRepository.findByUserId(userId);
    }

    public void saveUserSchool(UserSchool userSchool) {
        userSchoolRepository.save(userSchool);
    }
}
