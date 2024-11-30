package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoorStep;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorHeaderRepository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorRepository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorStepRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/door")
public class DoorToDoorController {
    private final DoorToDoorStepRepository doorToDoorStepRepository;
    private final DoorToDoorRepository doorToDoorRepository;
    private final DoorHeaderRepository doorHeaderRepository;

    @GetMapping()
    @Operation(summary = "Elave Faydalari elde etmek ucun endpoint(Qapidan qapiya)")
    List<DoorToDoor>getDoorToDoor(){
        return  doorToDoorRepository.findAll();
    }
    @GetMapping("/step")
    @Operation(summary = "Xidmet merhelelerini elde etmek ucun endpoint(Qapidan qapiya)")
    List<DoorToDoorStep>getDoorToDoorStep(){
        return  doorToDoorStepRepository.findAll();
    }
    @GetMapping("/header")
    @Operation(summary = "Headeri elde etmek ucun endpoint(Qapidan qapiya)")
    List<DoorHeader>getHeader(){
        return  doorHeaderRepository.findAll();
    }

}
