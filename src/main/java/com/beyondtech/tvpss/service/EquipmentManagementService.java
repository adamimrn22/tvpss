package com.beyondtech.tvpss.service;

import com.beyondtech.tvpss.model.Equipment;
import com.beyondtech.tvpss.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EquipmentManagementService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public EquipmentManagementService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipmentsBySchoolCode(String schoolCode) {
        return equipmentRepository.getAllEquipmentBySchoolCode(schoolCode);
    }

    public Map<Long, Equipment> getEquipmentById(Long id) {
        return equipmentRepository.getEquipmentById(id);
    }


    public Equipment saveOrUpdateEquipment(Equipment equipment) {
        return equipmentRepository.saveOrUpdate(equipment);
    }

    public void deleteEquipment(Equipment equipment) {
        try{
            System.out.println("Equipment deleted" + equipment.getId());
            equipmentRepository.deleteEquipment(equipment);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Long countAllEquipments(String schoolCode) {
        return equipmentRepository.getEquipmentCountBySchoolCode(schoolCode);
    }
}
