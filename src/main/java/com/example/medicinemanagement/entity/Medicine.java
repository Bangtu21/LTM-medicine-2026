package com.example.medicinemanagement.entity;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
@Entity
@Table(name = "medicine")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Medicine {
    @Id
    private String id;

    private String name;
    private BigDecimal price;
    private String type;

    @ManyToOne
    @JoinColumn(name = "usage_id")
    private Usage usage;
}
