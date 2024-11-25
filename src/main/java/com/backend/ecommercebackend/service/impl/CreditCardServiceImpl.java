package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.CreditCardRequest;
import com.backend.ecommercebackend.dto.request.DoorToDoorRequest;
import com.backend.ecommercebackend.model.admin.credit.CreditCard;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import com.backend.ecommercebackend.repository.admin.credit.CreditCardRepository;
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

}
