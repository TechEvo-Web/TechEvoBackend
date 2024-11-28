package com.backend.ecommercebackend.model.admin.credit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String cardImage;
    String cardDescription;
}
