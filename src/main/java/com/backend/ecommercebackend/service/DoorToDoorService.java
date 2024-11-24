package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.DoorToDoorRequest;
import com.backend.ecommercebackend.model.admin.doortodoor.DoorToDoor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DoorToDoorService {
    DoorToDoor addDoorToDoor(DoorToDoorRequest doorToDoorRequest, MultipartFile multipartFile);
    DoorToDoor updateDoorToDoor(int id, DoorToDoorRequest doorToDoorRequest, MultipartFile multipartFile) throws IOException;

}
