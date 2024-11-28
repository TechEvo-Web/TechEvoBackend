package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.DoorToDoorStepRequest;
import com.backend.ecommercebackend.dto.request.SupportStepRequest;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoorStep;
import com.backend.ecommercebackend.model.admin.support.SupportStep;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorStepRepository;
import com.backend.ecommercebackend.service.DoorToDoorStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoorToDoorStepServiceImpl implements DoorToDoorStepService {
    private final DoorToDoorStepRepository doorToDoorStepRepository;


    @Override
public DoorToDoorStep addDoorToDoorStep(DoorToDoorStepRequest doorToDoorStepRequest){
    DoorToDoorStep doorToDoorStep=new DoorToDoorStep();
    doorToDoorStep.setStepName(doorToDoorStepRequest.getStepName());
    doorToDoorStep.setStepDescription(doorToDoorStepRequest.getStepDescription());
    doorToDoorStep.setStepOrder(doorToDoorStepRequest.getStepOrder());
    doorToDoorStepRepository.save(doorToDoorStep);
    return doorToDoorStep;
}
    @Override
    public DoorToDoorStep updateDoorToDoorStep(int id, DoorToDoorStepRequest doorToDoorStepRequest) {
        // Güncellenmek istenen DoorToDoorStep'i bul
        DoorToDoorStep doorToDoorStep = doorToDoorStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("orderStep not found with id " + id));

        // Aynı stepOrder değerine sahip başka bir kayıt var mı kontrol et
        boolean isStepOrderTaken = doorToDoorStepRepository.existsByStepOrderAndIdNot(doorToDoorStepRequest.getStepOrder(), id);
        if (isStepOrderTaken) {
            throw new IllegalArgumentException("Step order " + doorToDoorStepRequest.getStepOrder() + " is already taken.");

        }

        // Verileri güncelle
        doorToDoorStep.setStepName(doorToDoorStepRequest.getStepName());
        doorToDoorStep.setStepDescription(doorToDoorStepRequest.getStepDescription());
        doorToDoorStep.setStepOrder(doorToDoorStepRequest.getStepOrder());

        // Kaydı veritabanına kaydet ve döndür
        return doorToDoorStepRepository.save(doorToDoorStep);
    }

}
