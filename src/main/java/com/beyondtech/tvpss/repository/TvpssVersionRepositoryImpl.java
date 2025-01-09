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
    public TvpssVersion save(TvpssVersion tvpssVersion) {
        getCurrentSession().persist(tvpssVersion);
        return tvpssVersion;
    }
}