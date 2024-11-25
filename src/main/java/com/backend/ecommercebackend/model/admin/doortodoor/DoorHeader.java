package com.backend.ecommercebackend.model.admin.doortodoor;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table(name = "door_headers")
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class DoorHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String headerName;
    String headerDescription;
}
