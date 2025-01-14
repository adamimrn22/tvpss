package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class LogRepositoryImpl implements LogRepository {

    @Autowired
    private SessionFactory sessionFactory;  // Inject the Hibernate SessionFactory

    @Override
    public void save(Log log) {
        Session session = sessionFactory.getCurrentSession();
        if (log.getId() == null) {
            session.persist(log);
        } else {
            session.merge(log);
        }
    }

    @Override
    public List<Log> findAllByLoginTimeAfter(LocalDateTime loginTime) {
        Session session = sessionFactory.getCurrentSession();
        // Query for logs that were created after the specified time
        return session.createQuery("FROM Log WHERE loginTime > :loginTime", Log.class)
                .setParameter("loginTime", loginTime)
                .getResultList();
    }
}

