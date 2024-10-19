package com.backend.ecommercebackend.model.product;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
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
    double discountPrice;

    @Column(nullable = false)
    float rating;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> imageUrl;

    @Column(nullable = false)
    String categoryName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    Map<String,Object> specifications=new HashMap<>();
}
