package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssStatus;
import com.beyondtech.tvpss.model.TvpssVersion;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TvpssVersionRepositoryImpl implements TvpssVersionRepository {

    @Autowired
    private SessionFactory sessionFactory;

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

//    @Override
//    public TvpssVersion saveOrUpdate(TvpssVersion tvpssVersion) {
//        Session session = getCurrentSession();
//
//        TvpssVersion existingVersion = session.createQuery("FROM TvpssVersion WHERE schoolCode = :schoolCode", TvpssVersion.class)
//                .setParameter("schoolCode", tvpssVersion.getSchoolCode())
//                .uniqueResult();
//
//        if (existingVersion != null) {
//            tvpssVersion.setId(existingVersion.getId());
//            session.merge(tvpssVersion);
//        } else {
//            session.persist(tvpssVersion);
//        }
//
//        return tvpssVersion;
//    }

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

}
