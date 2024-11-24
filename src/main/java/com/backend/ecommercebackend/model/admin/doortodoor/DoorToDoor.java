package com.backend.ecommercebackend.model.admin.doortodoor;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Table
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class DoorToDoor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    String doorName;
    String doorDescription;
    String doorImage="";
}
