package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.config.CloudinaryConfig;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.service.FileStorageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final Cloudinary cloudinary;

    @Override
    public String storeImages(MultipartFile file,String folderName) throws IOException {
        Map<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);
        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
        String publicId = (String) uploadedFile.get("public_id");
        return cloudinary.url().secure(true).generate(publicId);

    }

    @Override
    public void deleteFile(String url) throws IOException {
        String publicId = url.replace("https://res.cloudinary.com/dxwnhu7af/image/upload/v1/","");
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

}
