package com.backend.ecommercebackend.model.user;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "user-email")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEmail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Email
  @NotEmpty
  @Column(nullable = false, unique = true)
  String email;

  @Builder.Default
  private boolean verified = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDateTime updatedAt;
}
