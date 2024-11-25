package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CreditCardRequest;
import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CreditCardService {
    CreditCard addCreditCard(CreditCardRequest creditCardRequest, MultipartFile multipartFile);
    CreditCard updateCreditCard(int id, CreditCardRequest creditCardRequest, MultipartFile multipartFile) throws IOException;

}
