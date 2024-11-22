package com.backend.ecommercebackend.model.admin.support;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "support_steps",
        uniqueConstraints = @UniqueConstraint(columnNames = {"stepOrder"}))
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SupportStep {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String stepName;
    String stepDescription;
    @Column(unique = true)
    int stepOrder;

}
