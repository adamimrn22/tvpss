package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.UserSchool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class UserSchoolRepositoryImpl implements UserSchoolRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<UserSchool> findByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<UserSchool> query = session.createQuery("FROM UserSchool WHERE user.id = :userId", UserSchool.class);
        query.setParameter("userId", userId);
        UserSchool userSchool = query.uniqueResult();
        return Optional.ofNullable(userSchool);
    }

    @Override
    public void save(UserSchool userSchool) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(userSchool);
    }
}
