package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAddress(String emailAddress);
    void save(User user);
}
