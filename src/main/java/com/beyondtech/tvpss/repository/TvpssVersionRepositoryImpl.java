package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssVersion;
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
            tvpssVersion.setId(existingVersion.getId());
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
}
