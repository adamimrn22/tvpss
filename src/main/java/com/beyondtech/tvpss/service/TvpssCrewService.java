package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssCrew;
import com.beyondtech.tvpss.repository.TvpssCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvpssCrewService {
    @Autowired
    TvpssCrewRepository tvpssCrewRepository;

    public TvpssCrewService(TvpssCrewRepository tvpssCrewRepository) {
        this.tvpssCrewRepository = tvpssCrewRepository;
    }

    public List<TvpssCrew> getAllApplicationBySchoolAndStatus(String schoolCode, ApplicationStatus status) {
        return tvpssCrewRepository.getAllAplicationBySchool(schoolCode, status);
    }

    public TvpssCrew getApplicationById(Long id) {
        return tvpssCrewRepository.getCrewById(id);
    }

    public void updateApplication(TvpssCrew tvpssCrew) {
        tvpssCrewRepository.updateStatus(tvpssCrew);
    }

    public Long countTvpssCrewBySchoolAndStatus(String schoolCode, ApplicationStatus status) {
        return tvpssCrewRepository.countApplicationBySchool(schoolCode, status);
    }
}
