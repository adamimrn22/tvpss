package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.exception.UserException;
import com.beyondtech.tvpss.model.Role;
import com.beyondtech.tvpss.model.School;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.model.UserSchool;
import com.beyondtech.tvpss.repository.RoleRepository;
import com.beyondtech.tvpss.repository.UserRepository;
import com.beyondtech.tvpss.repository.UserSchoolRepository;
import com.beyondtech.tvpss.utils.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserSchoolRepository userSchoolRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String getSchoolCodeForUser(Long userId) {
        return userSchoolRepository.findByUserId(userId)
                .map(UserSchool::getSchoolCode)
                .orElse(null);
    }


    @Transactional
    public void addNewUser(String name, String email, String password, String district, String roleName, String schoolCode) {

        if (userRepository.existsByEmailAddress(email)) {
            throw UserException.alreadyExists();
        }

        if (name == null || name.isEmpty()) {
            throw UserException.fieldNull("Name");
        }
        if (email == null || email.isEmpty()) {
            throw UserException.fieldNull("Email");
        }

        if (email == null || email.isEmpty()) {
            throw UserException.fieldNull("Email");
        }
        Role role = roleRepository.findByRolename(roleName);

        if (role == null) {
            throw UserException.roleNotFound(roleName);
        }

        if(district == null || district.isEmpty()) {
            district = null;
        }

        User user = new User();
        user.setName(name);
        user.setEmailAddress(email);
        user.setDistrict(district);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        if(user.getRole().getRolename().equals("schooladmin")){
            UserSchool userSchool = new UserSchool();
            userSchool.setSchoolCode(schoolCode);
            userSchool.setUser(user);
            userSchoolRepository.save(userSchool);
        }
    }

    public Map<String, Object> getUserWithSchoolDetails(Long id) {
        User user = userRepository.findById(id).orElse(null);
        String schoolCode = userSchoolRepository.findByUserId(id)
                .map(UserSchool::getSchoolCode)
                .orElse(null);

        System.out.println("school code " + schoolCode);
        System.out.println("user data test" + user.getRole().getRolename());

        if (user.getRole().getRolename().equals("schooladmin") && schoolCode != null) {
            School school = schoolService.getSchoolByCode(schoolCode);
            System.out.println("school data " + school);
            if (school != null) {
                user.setSchool(school);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("schoolCode", schoolCode);
        return result;
    }

    public PageResponse<User> getAllUsersPageable(int page, int size) {
        List<User> users = userRepository.findAllPaginated(page, size);
        long total = userRepository.getTotalCount();
        return new PageResponse<>(users, page, size, total);
    }

    @Transactional
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {

            userRepository.delete(user.get());
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }

}
