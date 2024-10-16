package com.backend.ecommercebackend.model.product;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product-specification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSpecification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long specificationId;

  @Column(nullable = false)
  String specificationName;

  @Column(nullable = false)
  int categoryId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime updatedAt;
}
