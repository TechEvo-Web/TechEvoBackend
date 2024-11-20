package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.dynamic.Support;
import com.backend.ecommercebackend.repository.dynamic.SupportRepository;
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
    @Transactional
    @GetMapping
    @Operation(summary = "Xidmetlerin hamisini elde etmek üçün endpoint")

    public List<Support> getSupports(){
        return  supportRepository.findAll();
    }
}
