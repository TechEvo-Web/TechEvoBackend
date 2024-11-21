package com.backend.ecommercebackend.model.dynamic;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> serviceComponents;
}
