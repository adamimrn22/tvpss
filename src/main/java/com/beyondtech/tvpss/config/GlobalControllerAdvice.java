package com.beyondtech.tvpss.config;

import com.beyondtech.tvpss.model.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.model.UserSchool;
import com.beyondtech.tvpss.repository.UserRepository;
import com.beyondtech.tvpss.repository.UserSchoolRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Component
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository; // Repository to fetch user details
    @Autowired
    private UserSchoolRepository userSchoolRepository; // Repository to fetch school details

    @ModelAttribute
    public void addAuthenticatedUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.core.userdetails.User springSecurityUser) {
                String username = springSecurityUser.getUsername();

                User user = userRepository.findByEmailAddress(username).orElse(null);

                if (user != null) {
                    model.addAttribute("currentUser", user);

                    Role userRole = user.getRole();
                    if (userRole != null) {
                        model.addAttribute("roleName", userRole.getName());
                        model.addAttribute("role", userRole.getRolename());
                    }

                    userSchoolRepository.findByUserId(user.getId()).ifPresent(userSchool -> model.addAttribute("schoolCode", userSchool.getSchoolCode()));
                }
            }
        }
    }
}
