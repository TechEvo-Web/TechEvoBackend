package com.backend.ecommercebackend.model.admin.credit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Table
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Header2 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String headerName;
    String headerDescription;

}
