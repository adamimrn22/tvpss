package com.beyondtech.tvpss.facade;

import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.service.EmailService;
import com.beyondtech.tvpss.service.UserManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementFacade {

    @Autowired
    private final UserManagementService userManagementService;
    private final EmailService emailService;

    public UserManagementFacade(UserManagementService userManagementService, EmailService emailService) {
        this.userManagementService = userManagementService;
        this.emailService = emailService;
    }

    public void addUser(String name, String email, String password, String district, String role, String schoolCode) {
        try {
            userManagementService.addNewUser(name, email, password, district, role, schoolCode);
            emailService.sendPasswordEmail(new User(name, email), password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
