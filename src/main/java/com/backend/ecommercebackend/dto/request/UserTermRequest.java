package com.backend.ecommercebackend.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTermRequest {
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<String>terms;
}
