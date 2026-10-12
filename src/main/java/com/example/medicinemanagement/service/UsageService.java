package com.example.medicinemanagement.service;

import com.example.medicinemanagement.entity.Usage;
import com.example.medicinemanagement.repository.UsageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UsageService {
    private final UsageRepository usageRepository;

    public List<Usage> getAll() {
        return usageRepository.findAll();
    }

    public Usage getById(String id) {
        return usageRepository.findById(id).orElse(null);
    }

    public Usage save(Usage usage) {
        return usageRepository.save(usage);
    }

    public void delete(String id) {
        usageRepository.deleteById(id);
    }

    public List<Usage> searchByName(String name) {

        List<Usage> usages = usageRepository.findAll();
        List<Usage> result = new ArrayList<>();

        for (Usage usage : usages) {

            if (usage.getName()
                    .toLowerCase()
                    .contains(name.toLowerCase())) {

                result.add(usage);
            }
        }

        return result;
    }
}
