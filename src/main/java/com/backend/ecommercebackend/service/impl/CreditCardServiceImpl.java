package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.CreditCardRequest;
import com.backend.ecommercebackend.dto.request.DoorToDoorRequest;
import com.backend.ecommercebackend.dto.request.Header1Request;
import com.backend.ecommercebackend.dto.request.Header2Request;
import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.credit.Header1;
import com.backend.ecommercebackend.model.admin.credit.Header2;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import com.backend.ecommercebackend.repository.admin.credit.CreditCardRepository;
import com.backend.ecommercebackend.repository.admin.credit.Header1Repository;
import com.backend.ecommercebackend.repository.admin.credit.Header2Repository;
import com.backend.ecommercebackend.service.CreditCardService;
import com.backend.ecommercebackend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private final FileStorageService storageService;
    private final CreditCardRepository creditCardRepository;
    private final Header1Repository header1Repository;
    private final Header2Repository header2Repository;

    @Override
    public CreditCard addCreditCard(CreditCardRequest creditCardRequest, MultipartFile multipartFile) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String url;
        try {
            url = storageService.storeImages(multipartFile, "cardİmages");
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setCardDescription(creditCardRequest.getCardDescription());
        creditCard.setCardImage(url);
        return creditCardRepository.save(creditCard);
    }

    @Override
    public CreditCard updateCreditCard(int id, CreditCardRequest creditCardRequest, MultipartFile multipartFile) throws IOException {
        Optional<CreditCard> existingCreditCardOpt = creditCardRepository.findById(id);
        if (existingCreditCardOpt.isEmpty()) {
            throw new IllegalArgumentException("CreditCard with id " + id + " not found");
        }

        CreditCard existingCreditCard = existingCreditCardOpt.get();

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String newUrl = storageService.storeImages(multipartFile, "cardİmages");
            existingCreditCard.setCardImage(newUrl);
        }

        existingCreditCard.setCardDescription(creditCardRequest.getCardDescription());

        return creditCardRepository.save(existingCreditCard);
    }
    @Override
    public Header1 addHeader1(Header1Request header1Request){
        header1Repository.deleteAll();
        Header1 header1 = new Header1();
        header1.setHeaderName(header1Request.getHeaderName());
        header1.setHeaderDescription(header1Request.getHeaderDescription());
        return  header1Repository.save(header1);
    }
    @Override
    public Header2 addHeader2(Header2Request header2Request){
        header2Repository.deleteAll();
        Header2 header2 = new Header2();
        header2.setHeaderName(header2Request.getHeaderName());
        header2.setHeaderDescription(header2Request.getHeaderDescription());
        return  header2Repository.save(header2);
    }

}
