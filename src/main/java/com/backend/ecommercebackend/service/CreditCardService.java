package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CreditCardRequest;
import com.backend.ecommercebackend.dto.request.DoorHeaderRequest;
import com.backend.ecommercebackend.dto.request.Header1Request;
import com.backend.ecommercebackend.dto.request.Header2Request;
import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.credit.Header1;
import com.backend.ecommercebackend.model.admin.credit.Header2;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CreditCardService {
    CreditCard addCreditCard(CreditCardRequest creditCardRequest, MultipartFile multipartFile);
    CreditCard updateCreditCard(int id, CreditCardRequest creditCardRequest, MultipartFile multipartFile) throws IOException;
    Header1 addHeader1(Header1Request header1Request);
    Header2 addHeader2(Header2Request header2Request);
}
