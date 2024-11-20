package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.SupportRequest;
import com.backend.ecommercebackend.model.dynamic.Support;
import com.backend.ecommercebackend.repository.dynamic.SupportRepository;
import com.backend.ecommercebackend.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor

public class AdminController {

    private final SupportService supportService;
    private final SupportRepository supportRepository;

    @PostMapping("/support")
    @Operation(summary = "Xidmetleri elave etmek ucun endpoint")
    public ResponseEntity<Support> addSupport(@RequestBody SupportRequest supportRequest) {
         Support createdSupport = supportService.addSupport(supportRequest);
        return ResponseEntity.ok(createdSupport);
    }
    @DeleteMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore silmek ucun endpoint")
    public ResponseEntity<Support> deleteSupport(@PathVariable int id) {
        supportRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/support/{id}")
    @Operation(summary = "Xidmetleri idye gore update etmek ucun endpoint")
    public ResponseEntity<Support> updateProduct(@PathVariable int id, @RequestBody SupportRequest supportRequest) {
        Support updatedSupport = supportService.updateSupport(id, supportRequest);
        return ResponseEntity.ok(updatedSupport);
    }
}

