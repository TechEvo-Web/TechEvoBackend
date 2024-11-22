package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.dto.request.SupportStepRequest;
import com.backend.ecommercebackend.dto.request.UserTermRequest;
import com.backend.ecommercebackend.model.admin.support.Support;
import com.backend.ecommercebackend.model.admin.support.SupportStep;
import com.backend.ecommercebackend.model.admin.term.UserTerm;
import com.backend.ecommercebackend.repository.admin.support.SupportRepository;
import com.backend.ecommercebackend.repository.admin.support.SupportStepRepository;
import com.backend.ecommercebackend.repository.admin.term.UserTermRepository;
import com.backend.ecommercebackend.service.SupportService;
import com.backend.ecommercebackend.service.UserTermService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor

public class AdminController {

    private final SupportService supportService;
    private final SupportRepository supportRepository;
    private final SupportStepRepository supportStepRepository;
private final UserTermService userTermService;
    private final UserTermRepository userTermRepository;

    @PostMapping("/support")
    @Operation(summary = "Xidmetleri elave etmek ucun endpoint")
    public ResponseEntity<Support> addSupport(@RequestBody SupportRequest supportRequest) {
        Support createdSupport = supportService.addSupport(supportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupport);
    }

    @DeleteMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore silmek ucun endpoint")
    public ResponseEntity<Support> deleteSupport(@PathVariable int id) {
        supportRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore update etmek ucun endpoint")
    public ResponseEntity<Support> updateSupport(@PathVariable int id, @RequestBody SupportRequest supportRequest) {
        Support updatedSupport = supportService.updateSupport(id, supportRequest);
        return ResponseEntity.ok(updatedSupport);
    }

    @PostMapping("/supportStep")
    @Operation(summary = "Xidmet merhelelerini elave etmek ucun endpoint")
    public ResponseEntity<?> addSupportSteps(@RequestBody SupportStepRequest supportStepRequest) {
         if (supportStepRepository.existsByStepOrder(supportStepRequest.getStepOrder())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu sıra doludur");
        }

         SupportStep createdSupportStep = supportService.addSupportStep(supportStepRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupportStep);
    }


    @PutMapping("/supportStep/{id}")
    @Operation(summary = "Xidmet merhelelerini idye gore update etmek ucun endpoint")
    public ResponseEntity<?> updateSupportStep(@PathVariable int id, @RequestBody SupportStepRequest supportStepRequest) {
        Optional<SupportStep> existingSupportStep = supportStepRepository.findById(id);
        if (existingSupportStep.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Xidmet merhelesi tapilmadi");
        }

        SupportStep currentSupportStep = existingSupportStep.get();
        if (currentSupportStep.getStepOrder() != supportStepRequest.getStepOrder() &&
                supportStepRepository.existsByStepOrder(supportStepRequest.getStepOrder())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bu sıra doludur");
        }

         SupportStep updatedSupportStep = supportService.updateSupportStep(id, supportStepRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedSupportStep);
    }
@PostMapping("/term")
    public ResponseEntity<UserTerm> addTerm(@RequestBody UserTermRequest userTermRequest){
     UserTerm userTerm = userTermService.addTerm(userTermRequest);
     return ResponseEntity.status(HttpStatus.CREATED).body(userTerm);
}
    @GetMapping("/terms")
    public List<UserTerm> getUserTerm(){
        return userTermRepository.findAll();
    }
}

