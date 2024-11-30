package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.DoorToDoorStepRequest;
import com.backend.ecommercebackend.dto.request.SupportStepRequest;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoorStep;
import com.backend.ecommercebackend.model.admin.support.SupportStep;

public interface DoorToDoorStepService {
    DoorToDoorStep addDoorToDoorStep(DoorToDoorStepRequest doorToDoorStepRequest);
    DoorToDoorStep updateDoorToDoorStep(int id, DoorToDoorStepRequest doorToDoorStepRequest);

}
