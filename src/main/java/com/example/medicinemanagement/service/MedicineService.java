package com.example.medicinemanagement.service;

import com.example.medicinemanagement.entity.Medicine;
import com.example.medicinemanagement.repository.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicineService {
    private final MedicineRepository medicineRepository;

    public List<Medicine> getAll() {
        return medicineRepository.findAll();
    }

    public Medicine getById(String id) {
        return medicineRepository.findById(id).orElse(null);
    }

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void delete(String id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> search(
            BigDecimal price,
            String type,
            String usageId
    ) {

        List<Medicine> medicines = medicineRepository.findAll();
        List<Medicine> result = new java.util.ArrayList<>();

        for (Medicine medicine : medicines) {
            boolean match = true;

            if (price != null && medicine.getPrice().compareTo(price) != 0) {
                match = false;
            }

            if (type != null && !type.isEmpty() && !medicine.getType().equalsIgnoreCase(type)) {
                match = false;
            }

            if (usageId != null && !usageId.isEmpty() && !medicine.getUsage().getId().equals(usageId)) {
                match = false;
            }

            if (match) {
                result.add(medicine);
            }
        }

        return result;
    }

    public boolean hasMedicineByUsage(String usageId) {
        List<Medicine> medicines = medicineRepository.findByUsageId(usageId);
        return !medicines.isEmpty();
    }
}
