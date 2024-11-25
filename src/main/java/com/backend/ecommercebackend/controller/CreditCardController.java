package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.repository.admin.credit.CreditCardRepository;
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

    @GetMapping
    public List<CreditCard> getCreditCard(){
        return creditCardRepository.findAll();
    }
}
