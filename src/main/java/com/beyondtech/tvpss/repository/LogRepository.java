package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Log;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository {
    void save(Log log); // Custom save method
    List<Log> findAllByLoginTimeAfter(LocalDateTime loginTime);

}
