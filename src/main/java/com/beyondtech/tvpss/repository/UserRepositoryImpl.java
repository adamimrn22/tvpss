package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User WHERE emailAddress = :emailAddress", User.class);
        query.setParameter("emailAddress", emailAddress);
        return query.uniqueResultOptional();
    }

    @Override
    public List<User> findAll(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT a FROM User a", User.class).getResultList();
    }

    @Override
    public List<User> findAllPaginated(int page, int size) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("FROM User", User.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public long getTotalCount() {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.uniqueResult();
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(User.class, id));
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT COUNT(*) FROM User WHERE emailAddress = :emailAddress", Long.class);
        query.setParameter("emailAddress", emailAddress);
        Long count = query.uniqueResult();
        return count > 0;
    }

    @Override
    public boolean edit(Long id, String name, String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("UPDATE User SET name = :name, emailAddress = :emailAddress WHERE id = :id");

        query.setParameter("name", name);
        query.setParameter("emailAddress", email);
        query.setParameter("id", id);
        int result = query.executeUpdate();

        return result > 0;
    }

    @Override
    public void delete(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(user);
    }

    @Override
    public Long countUsersByRole(String roleName) {
        Session session = sessionFactory.getCurrentSession();

        Query<Long> query = session.createQuery( "SELECT COUNT(u) FROM User u WHERE u.role.rolename = :roleName", Long.class);
        query.setParameter("roleName", roleName);

        return query.uniqueResult();
    }
}
