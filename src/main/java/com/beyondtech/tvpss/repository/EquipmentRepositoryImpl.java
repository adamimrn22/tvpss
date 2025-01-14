package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Equipment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class EquipmentRepositoryImpl implements EquipmentRepository {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Equipment saveOrUpdate(Equipment equipment) {
        Session session = getCurrentSession();
        if (equipment.getId() != null) {
            Equipment existingEquipment = session.get(Equipment.class, equipment.getId());
            if (existingEquipment != null) {
                existingEquipment.setEquipmentName(equipment.getEquipmentName());
                existingEquipment.setEquipmentType(equipment.getEquipmentType());
                existingEquipment.setLocation(equipment.getLocation());
                existingEquipment.setDateAdded(equipment.getDateAdded());
                existingEquipment.setStatus(equipment.getStatus());
                existingEquipment.setSchoolCode(equipment.getSchoolCode());
                session.merge(existingEquipment);
            }
        } else {
            session.persist(equipment);
        }

        return equipment;
    }

    @Override
    public void deleteEquipment(Equipment equipment) {
        Session session = getCurrentSession();
        session.remove(equipment);
    }

    @Override
    public List<Equipment> getAllEquipmentBySchoolCode(String schoolCode) {
        Session session = getCurrentSession();

        return session.createQuery("FROM Equipment e WHERE e.schoolCode = :schoolCode", Equipment.class)
                .setParameter("schoolCode", schoolCode)
                .getResultList();
    }

    @Override
    public Map<Long, Equipment> getEquipmentById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Equipment> query = session.createQuery("FROM Equipment e WHERE e.id = :id", Equipment.class);
        query.setParameter("id", id);
        Equipment equipment = query.uniqueResult();
        Map<Long, Equipment> result = new HashMap<>();
        result.put(id, equipment);

        return result;
    }

    @Override
    public Long getEquipmentCountBySchoolCode(String schoolCode) {
        Session session = getCurrentSession();

        return session.createQuery("SELECT COUNT(e) FROM Equipment e WHERE e.schoolCode =: schoolCode")
                .setParameter("schoolCode", schoolCode)
                .getResultCount();
    }

}
