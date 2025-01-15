package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;

import java.util.Map;

public interface TvpssVersionRepository {
    TvpssVersion saveOrUpdate(TvpssVersion tvpssVersion);
    TvpssVersion findBySchoolCode(String schoolCode);
    void updateTvpssVersion(String schoolCode, int version, TvpssStatus tvpssStatus);

    Integer getTvpssVersion(String schoolCode);

    Long countTvpssVersions(Long version);

    Long countTvpssVersionsByDistrictAndVersion(String district, Long version);

    Long countTvpssVersionByDistrictAndStatus(String district, TvpssStatus status);

    Map<String, Long> countTvpssVersionsByVersion(int version);
}