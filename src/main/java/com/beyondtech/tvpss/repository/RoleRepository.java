package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Role;

public interface RoleRepository {
    Role findByRolename(String rolename);
    void save(Role role);
}