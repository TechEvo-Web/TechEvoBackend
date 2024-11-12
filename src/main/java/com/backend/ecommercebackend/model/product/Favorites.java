package com.backend.ecommercebackend.model.product;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorites")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Favorites {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  Long userId;

  @JoinColumn(nullable = false)
  Long productId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime updatedAt;
}
