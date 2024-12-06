package com.backend.ecommercebackend.model.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String phoneNumber;
    String name;
    String surname;
    String additionalInfo;
}
