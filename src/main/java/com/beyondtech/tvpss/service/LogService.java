package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.Log;
import com.beyondtech.tvpss.model.Role;
import com.beyondtech.tvpss.model.User;
import com.beyondtech.tvpss.repository.LogRepository;
import com.beyondtech.tvpss.repository.RoleRepository;
import com.beyondtech.tvpss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;

    @Transactional
    public void logLogin(String username) {
        User user = userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = user.getRole();
        Log log = new Log();
        log.setUser(user);
        log.setRole(role);
        log.setLoginTime(LocalDateTime.now());

        logRepository.save(log);
    }

    public Map<String, int[]> getLoginCountsByHour() {

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<Log> logs = logRepository.findAllByLoginTimeAfter(oneDayAgo);

        Map<String, int[]> roleLoginCounts = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getRole().getRolename(), // Group by role
                        Collectors.collectingAndThen(Collectors.toList(), logsByRole -> {
                            int[] hourlyCounts = new int[24];
                            for (Log log : logsByRole) {
                                int hour = log.getLoginTime().getHour();
                                hourlyCounts[hour]++;
                            }
                            return hourlyCounts;
                        })
                ));
        return roleLoginCounts;
    }
}

