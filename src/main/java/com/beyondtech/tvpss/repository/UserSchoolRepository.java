package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.UserSchool;

import java.util.Optional;

public interface UserSchoolRepository {

    Optional<UserSchool> findByUserId(Long userId);
    void save(UserSchool userSchool);
}
