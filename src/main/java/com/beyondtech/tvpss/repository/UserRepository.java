package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAddress(String emailAddress);
    List<User> findAll();
    Optional<User> findById(Long id); 
    void save(User user);
    boolean existsByEmailAddress(String emailAddress);
}
