package com.backend.ecommercebackend.model.admin.support;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table(name = "support_header")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SupportHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String headerName;
    String headerDescription;
}
