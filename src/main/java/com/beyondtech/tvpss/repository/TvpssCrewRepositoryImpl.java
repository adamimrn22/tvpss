package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssCrew;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TvpssCrewRepositoryImpl implements TvpssCrewRepository {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void updateStatus(TvpssCrew crew) {
        Session session = getCurrentSession();
        if (crew != null) {
            if (crew.getRejectCause() != null) {
                session.createQuery("UPDATE TvpssCrew SET status = :status, rejectCause = :message WHERE id=:id")
                        .setParameter("status", crew.getStatus())
                        .setParameter("message", crew.getRejectCause())
                        .setParameter("id", crew.getId())
                        .executeUpdate();
            } else {
                session.createQuery("UPDATE TvpssCrew SET status = :status WHERE id=:id")
                        .setParameter("status", crew.getStatus())
                        .setParameter("id", crew.getId())
                        .executeUpdate();
            }
        }
    }


    @Override
    public void deleteApplication(TvpssCrew crew) {

    }

    @Override
    public TvpssCrew getCrewById(long id) {
        Session session = getCurrentSession();
        return session.createQuery("FROM TvpssCrew t WHERE t.id = :id", TvpssCrew.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    @Override
    public List<TvpssCrew> getAllAplicationBySchool(String schoolCode, ApplicationStatus status) {
        Session session = getCurrentSession();

        return session.createQuery("FROM TvpssCrew t WHERE t.schoolCode = :schoolCode AND t.status = :status", TvpssCrew.class)
                .setParameter("schoolCode", schoolCode)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public Long countApplicationBySchool(String schoolCode, ApplicationStatus status) {
        Session session = getCurrentSession();

        return session.createQuery("SELECT COUNT(t) FROM TvpssCrew t WHERE t.schoolCode =: schoolCode AND status = :status")
                .setParameter("schoolCode", schoolCode)
                .setParameter("status", status)
                .getResultCount();
    }

}
