package com.backend.ecommercebackend.model.admin.doortodoor;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "door_to_door_steps")
@Entity
@Data
public class DoorToDoorStep {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String stepName;
    String stepDescription;
    int stepOrder;
}
