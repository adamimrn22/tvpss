package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssCrew;

import java.util.List;

public interface TvpssCrewRepository {
    void updateStatus(TvpssCrew crew);
    void deleteApplication(TvpssCrew crew);
    TvpssCrew getCrewById(long id);
    List<TvpssCrew> getAllAplicationBySchool(String schoolCode, ApplicationStatus status);
}
