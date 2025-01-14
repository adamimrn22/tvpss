package com.beyondtech.tvpss.repository;

import com.beyondtech.tvpss.model.Equipment;

import java.util.List;
import java.util.Map;

public interface EquipmentRepository {
    Equipment saveOrUpdate(Equipment equipment);
    void deleteEquipment(Equipment equipment);
    List<Equipment> getAllEquipmentBySchoolCode(String schoolCode);
    Map<Long, Equipment> getEquipmentById(Long id);

    Long getEquipmentCountBySchoolCode(String schoolCode);
}
