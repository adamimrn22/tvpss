package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Achievement;
import com.beyondtech.tvpss.model.StudentAchievement;
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
public class StudentAchievementImpl implements StudentAchievementRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<StudentAchievement> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        StudentAchievement studentAchievement = session.get(StudentAchievement.class, id);
        return Optional.ofNullable(studentAchievement);

    }

    @Override
    public List<StudentAchievement> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<StudentAchievement> query = session.createQuery("FROM StudentAchievement", StudentAchievement.class);
        return query.getResultList();
    }

    @Override
    public List<StudentAchievement> findAllBySchoolId(String schoolCode) {
        Session session = sessionFactory.getCurrentSession();
        Query<StudentAchievement> query = session.createQuery(
                "FROM StudentAchievement WHERE achievementTable.schoolCode = :schoolCode", StudentAchievement.class);
        query.setParameter("schoolCode", schoolCode);
        return query.getResultList();
    }

    @Override
    public List<Achievement> findAchievementBySchoolCode(String schoolCode) {
        Session session = sessionFactory.getCurrentSession();
        Query<Achievement> query = session.createQuery(
                "FROM Achievement WHERE schoolCode = :schoolCode", Achievement.class);
        query.setParameter("schoolCode", schoolCode);
        return query.getResultList();
    }

    @Override
    public List<Achievement> findAllAchievement() {
        Session session = sessionFactory.getCurrentSession();
        Query<Achievement> query = session.createQuery("FROM Achievement ", Achievement.class);
        return query.getResultList();
    }


    @Override
    public Achievement achievementById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Achievement.class, id);
    }

    @Override
    public List<Achievement> achievementBySchoolCode(String schoolCode) {
        Session session = sessionFactory.getCurrentSession();
        Query<Achievement> query = session.createQuery(
                "FROM Achievement WHERE schoolCode = :schoolCode", Achievement.class);
        query.setParameter("schoolCode", schoolCode);
        return query.list();
    }

    @Override
    public List<StudentAchievement> findStudentAchievementsByAchievementId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query<StudentAchievement> query = session.createQuery("FROM StudentAchievement sa WHERE sa.achievementTable.id = :achievementId", StudentAchievement.class);
        query.setParameter("achievementId", id);
        return query.getResultList();
    }

    @Override
    public void saveAchievement(Achievement achievement) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(achievement);
    }

    @Override
    public void save(StudentAchievement studentAchievement) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(studentAchievement);  // Will insert or update the student achievement
    }

    @Override
    public void delete(Achievement achievement) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(achievement);
    }

}
