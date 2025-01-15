package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import com.beyondtech.tvpss.service.SchoolService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class TvpssVersionRepositoryImpl implements TvpssVersionRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    SchoolService schoolService;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public TvpssVersion saveOrUpdate(TvpssVersion tvpssVersion) {
        Session session = getCurrentSession();

        TvpssVersion existingVersion = session.createQuery("FROM TvpssVersion WHERE schoolCode = :schoolCode", TvpssVersion.class)
                .setParameter("schoolCode", tvpssVersion.getSchoolCode())
                .uniqueResult();

        if (existingVersion != null) {
            // Set the ID for the update
            tvpssVersion.setId(existingVersion.getId());

            // If the new version has no logo path, preserve the existing one
            if (tvpssVersion.getLogoPath() == null) {
                tvpssVersion.setLogoPath(existingVersion.getLogoPath());
            }

            session.merge(tvpssVersion);
        } else {
            session.persist(tvpssVersion);
        }

        return tvpssVersion;
    }

    @Override
    public TvpssVersion findBySchoolCode(String schoolCode) {
        Session session = getCurrentSession();
        return session.createQuery("FROM TvpssVersion WHERE schoolCode = :schoolCode", TvpssVersion.class)
                .setParameter("schoolCode", schoolCode)
                .uniqueResult();
    }

    @Override
    public void updateTvpssVersion(String schoolCode, int version, TvpssStatus currentStatus) {
        Session session = getCurrentSession();

        int updatedEntities = session.createQuery(
                        "UPDATE TvpssVersion SET tvpssVersion = :version, tvpssCurrentStatus = :currentStatus  WHERE schoolCode = :schoolCode")
                .setParameter("version", version)
                .setParameter("currentStatus", currentStatus)
                .setParameter("schoolCode", schoolCode)
                .executeUpdate();

        if (updatedEntities == 0) {
            throw new EntityNotFoundException("No TVPSS version found for school code: " + schoolCode);
        }
    }

    @Override
    public Integer getTvpssVersion(String schoolCode){
        Session session = getCurrentSession();

        return (Integer) session.createQuery("SELECT tvpssVersion FROM TvpssVersion WHERE schoolCode = :schoolCode ")
                .setParameter("schoolCode", schoolCode)
                .uniqueResult();
    }

    @Override
    public Long countTvpssVersions(Long version) {
        Session session = getCurrentSession();

        return (Long) session.createQuery("SELECT COUNT(s) FROM TvpssVersion s WHERE s.tvpssVersion = :version")
                .setParameter("version", version)
                .uniqueResult();
    }

    @Override
    public Map<String, Long> countTvpssVersionsByVersion(int version) {
         // Get the district-school mapping from the external API
        Map<String, List<String>> districtSchoolMap = schoolService.getDistrictSchoolMap();

        // Create a map to hold the count for each district
        Map<String, Long> districtCounts = new HashMap<>();

        // Loop through each district and count the TVPSS versions for the given version
        for (Map.Entry<String, List<String>> entry : districtSchoolMap.entrySet()) {
            String district = entry.getKey();
            List<String> schoolCodesInDistrict = entry.getValue();

            if (schoolCodesInDistrict.isEmpty()) {
                districtCounts.put(district, 0L); // No schools in this district
                continue;
            }

            // Create the Hibernate query to count the TVPSS versions for the given district and version
            Session session = getCurrentSession();
            Long count = (Long) session.createQuery(
                            "SELECT COUNT(t) " +
                                    "FROM TvpssVersion t " +
                                    "WHERE t.schoolCode IN :schoolCodes AND t.tvpssVersion = :version")
                    .setParameter("schoolCodes", schoolCodesInDistrict)
                    .setParameter("version", version)
                    .uniqueResult();

            districtCounts.put(district, count != null ? count : 0L);
        }

        return districtCounts; // Return the counts for all districts
    }


    @Override
    public Long countTvpssVersionsByDistrictAndVersion(String district, int version) {
         Map<String, List<String>> districtSchoolMap = schoolService.getDistrictSchoolMap();

        List<String> schoolCodesInDistrict = districtSchoolMap.getOrDefault(district, List.of());

        if (schoolCodesInDistrict.isEmpty()) {
            return 0L;
        }

        Session session = getCurrentSession();

        Long count = (Long) session.createQuery(
                        "SELECT COUNT(t) " +
                                "FROM TvpssVersion t " +
                                "WHERE t.schoolCode IN :schoolCodes AND t.tvpssVersion = :version")
                .setParameter("schoolCodes", schoolCodesInDistrict)
                .setParameter("version", version)
                .uniqueResult();

        return count != null ? count : 0L;
    }
}
