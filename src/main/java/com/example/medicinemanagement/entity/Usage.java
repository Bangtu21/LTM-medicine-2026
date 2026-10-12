package com.example.medicinemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "usages")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Usage {
    @Id
    private String id;

    private String name;
    private String description;
}
