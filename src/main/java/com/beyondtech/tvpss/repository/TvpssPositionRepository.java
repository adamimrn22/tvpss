package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssPosition;

import java.util.List;

public interface TvpssPositionRepository {
    List<TvpssPosition> getAllCrewBySchoolCode(String schoolCode);
    void addCrew(TvpssPosition crew);
    void updateCrew(TvpssPosition position);
    void deleteCrew(TvpssPosition position);
    TvpssPosition getPositionDetail(Long id);
}
