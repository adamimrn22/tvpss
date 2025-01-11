package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;

public interface TvpssVersionRepository {
    TvpssVersion saveOrUpdate(TvpssVersion tvpssVersion);
    TvpssVersion findBySchoolCode(String schoolCode);
    void updateTvpssVersion(String schoolCode, int version, TvpssStatus tvpssStatus);
}