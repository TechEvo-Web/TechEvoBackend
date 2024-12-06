package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.credit.Header1;
import com.backend.ecommercebackend.model.admin.credit.Header2;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.repository.admin.credit.CreditCardRepository;
import com.backend.ecommercebackend.repository.admin.credit.Header1Repository;
import com.backend.ecommercebackend.repository.admin.credit.Header2Repository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/creditcard")
public class CreditCardController {
    private final CreditCardRepository creditCardRepository;
    private final Header1Repository header1Repository;
    private final Header2Repository header2Repository;

    @GetMapping
    public CreditCard getCreditCard(){
        return creditCardRepository.findFirstByOrderByIdAsc();
    }
    @GetMapping("/header1")
    @Operation(summary = "Header 1(Rahat alisveris) elde etmek ucun endpoint(Credit)")
    Header1 getHeader1(){
        return  header1Repository.findFirstByOrderByIdAsc();
    }

    @GetMapping("/header2")
    @Operation(summary = "Headeri 2(Daxili kredit) elde etmek ucun endpoint(Credit)")
    Header2 getHeader2(){
        return  header2Repository.findFirstByOrderByIdAsc();
    }
}
