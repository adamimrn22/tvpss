package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.TvpssPosition;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TvpssPositionRepositoryImpl implements TvpssPositionRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TvpssPosition> getAllCrewBySchoolCode(String schoolCode) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM TvpssPosition WHERE schoolCode = :schoolCode", TvpssPosition.class)
                .setParameter("schoolCode", schoolCode)
                .getResultList();
    }

    @Override
    public void addCrew(TvpssPosition position) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(position);
    }


    @Override
    public void updateCrew(TvpssPosition position) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(position);
    }

    @Override
    public void deleteCrew(TvpssPosition position) {
        Session session = sessionFactory.getCurrentSession();
        TvpssPosition existingPosition = session.get(TvpssPosition.class, position.getId());
        if (existingPosition != null) {
            session.delete(existingPosition);
        }
    }

    @Override
    public TvpssPosition getPositionDetail(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(TvpssPosition.class, id);
    }
}
