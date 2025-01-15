package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.TvpssPosition;
import com.beyondtech.tvpss.repository.TvpssPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TvpssPositionService {
    @Autowired
    private TvpssPositionRepository tvpssPositionRepository;

    public List<TvpssPosition> getAllCrewBySchoolCode(String schoolCode) {
        return tvpssPositionRepository.getAllCrewBySchoolCode(schoolCode);
    }

    public void addCrew(TvpssPosition tvpssPosition) {
        tvpssPositionRepository.addCrew(tvpssPosition);
    }

    public void updateCrew(TvpssPosition position) {
        tvpssPositionRepository.updateCrew(position);
    }

    public void deleteCrew(TvpssPosition position) {
        tvpssPositionRepository.deleteCrew(position);
    }


    public TvpssPosition getPositionDetail(Long id) {
        return tvpssPositionRepository.getPositionDetail(id);
    }
}
