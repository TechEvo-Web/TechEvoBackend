package com.backend.ecommercebackend.dto.request;

import lombok.Data;

@Data
public class DoorToDoorStepRequest {
    String stepName;
    String stepDescription;
    int stepOrder;
}
