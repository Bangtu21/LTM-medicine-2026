package com.example.medicinemanagement.repository;

import com.example.medicinemanagement.entity.Usage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRepository extends JpaRepository<Usage, String> {
}
