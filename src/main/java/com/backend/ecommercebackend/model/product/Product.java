package com.backend.ecommercebackend.model.product;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String modelNumber;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    double price;

    @Column(nullable = false)
    float ratingSum = 0.0f;

    @Column(nullable = false)
    int totalRatings = 0;

    @Column(nullable = false)
    float rating = 0.0f;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> imageUrl;

    @JoinColumn(nullable = false)
    String categoryName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<Object> specifications;
}
