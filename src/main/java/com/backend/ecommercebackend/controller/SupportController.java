package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.support.Support;
import com.backend.ecommercebackend.model.admin.support.SupportHeader;
import com.backend.ecommercebackend.model.admin.support.SupportStep;
import com.backend.ecommercebackend.repository.admin.support.SupportHeaderRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportStepRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportController {
    private  final SupportRepository supportRepository;
    private final SupportStepRepository supportStepRepository;
    private final SupportHeaderRepository supportHeaderRepository;

    @Transactional
    @GetMapping
    @Operation(summary = "Xidmetlerin hamisini elde etmek üçün endpoint")
    public List<Support> getSupports(){
        return  supportRepository.findAll();
    }

    @Transactional
    @GetMapping("/steps")
    @Operation(summary = "Xidmetler merhelelerini hamisini elde etmek üçün endpoint")
    public List<SupportStep> getSupportSteps(){
        return  supportStepRepository.findAllByOrderByStepOrderAsc();
    }

    @GetMapping("/header")
    @Operation(summary = "Xidmetlerin Headerini elde etmek ucun endpoint")
    List<SupportHeader>getHeader(){
        return  supportHeaderRepository.findAll();
    }

}
