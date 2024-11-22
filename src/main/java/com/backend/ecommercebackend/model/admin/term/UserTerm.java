package com.backend.ecommercebackend.model.admin.term;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Table
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<String> terms;
}
