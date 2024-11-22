package com.backend.ecommercebackend.model.admin.support;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
@Table
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String serviceName;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<String> serviceComponents;
}
