package com.example.medicinemanagement.repository;

import com.example.medicinemanagement.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, String> {
    List<Medicine> findByUsageId(String usageId);
}
