package com.backend.ecommercebackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String storeImages(MultipartFile file,String folderName) throws IOException;
    void deleteFile(String Url) throws IOException;
}
