package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Role findByRolename(String rolename) {
        Session session = sessionFactory.getCurrentSession();
        Query<Role> query = session.createQuery(
                "FROM Role WHERE rolename = :rolename", Role.class);
        query.setParameter("rolename", rolename);
        return query.uniqueResult();
    }

    @Override
    public void save(Role role) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(role);
    }
}