package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.ApplicationStatus;
import com.beyondtech.tvpss.model.TvpssCrew;
import com.beyondtech.tvpss.model.Student;
import com.beyondtech.tvpss.model.TvpssCrew;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Override
    public List<TvpssCrew> findApprovedByIdentificationNumbers(List<String> identificationNumbers) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM TvpssCrew tc WHERE tc.identificationNumber IN :identificationNumbers AND tc.status = :status";
            Query<TvpssCrew> query = session.createQuery(hql, TvpssCrew.class);
            query.setParameter("identificationNumbers", identificationNumbers);
            query.setParameter("status", ApplicationStatus.APPROVED);

            return query.list();
        }
    }

    public Long countTvpssCrewByYearRangeAndStatus(String schoolCode, int year, ApplicationStatus status) {
        Session session = getCurrentSession();

        Query<Long> query = session.createQuery("SELECT COUNT(t) FROM TvpssCrew t WHERE YEAR(t.dateApplied) = :year AND t.status = :status AND schoolCode = :schoolCode", Long.class)
                .setParameter("year", year)
                .setParameter("status", status)
                .setParameter("schoolCode", schoolCode);

        return query.getSingleResult();
    }
}
