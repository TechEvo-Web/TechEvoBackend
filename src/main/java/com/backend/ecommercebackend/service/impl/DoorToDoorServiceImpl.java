package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.DoorHeaderRequest;
import com.backend.ecommercebackend.dto.request.DoorToDoorRequest;
import com.backend.ecommercebackend.dto.request.UserTermRequest;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorHeader;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import com.backend.ecommercebackend.model.admin.term.UserTerm;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorHeaderRepository;
import com.backend.ecommercebackend.repository.admin.doortodoor.DoorToDoorRepository;
import com.backend.ecommercebackend.service.DoorToDoorService;
import com.backend.ecommercebackend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoorToDoorServiceImpl implements DoorToDoorService {
    private final FileStorageService storageService;
    private final DoorToDoorRepository doorToDoorRepository;
    private final DoorHeaderRepository doorHeaderRepository;

    @Override
    public DoorToDoor addDoorToDoor(DoorToDoorRequest doorToDoorRequest, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String url;
        try {
            url = storageService.storeImages(multipartFile, "benefitİmages");
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
        System.out.println(doorToDoorRequest.getBenefitName());
        DoorToDoor doorToDoor = new DoorToDoor();
        doorToDoor.setBenefitName(doorToDoorRequest.getBenefitName());

        doorToDoor.setBenefitImage(url);
        return doorToDoorRepository.save(doorToDoor);
    }
    @Override
    public DoorToDoor updateDoorToDoor(int id, DoorToDoorRequest doorToDoorRequest, MultipartFile multipartFile) throws IOException {
        Optional<DoorToDoor> existingDoorToDoorOpt = doorToDoorRepository.findById(id);
        if (existingDoorToDoorOpt.isEmpty()) {
            throw new IllegalArgumentException("DoorToDoor with id " + id + " not found");
        }

        DoorToDoor existingDoorToDoor = existingDoorToDoorOpt.get();

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String newUrl = storageService.storeImages(multipartFile, "benefitİmages");
            existingDoorToDoor.setBenefitImage(newUrl);
        }

        existingDoorToDoor.setBenefitName(doorToDoorRequest.getBenefitName());

        return doorToDoorRepository.save(existingDoorToDoor);
    }
@Override
    public DoorHeader addHeader(DoorHeaderRequest doorHeaderRequest){
        doorHeaderRepository.deleteAll();
        DoorHeader doorHeader = new DoorHeader();
    doorHeader.setHeaderName(doorHeaderRequest.getHeaderName());
    doorHeader.setHeaderDescription(doorHeaderRequest.getHeaderDescription());
        return  doorHeaderRepository.save(doorHeader);

}
}

