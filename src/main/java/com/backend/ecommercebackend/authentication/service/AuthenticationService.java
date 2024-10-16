package com.backend.ecommercebackend.authentication.service;

import com.backend.ecommercebackend.authentication.dto.request.AuthRequest;
import com.backend.ecommercebackend.authentication.dto.request.LogoutRequest;
import com.backend.ecommercebackend.authentication.dto.request.RegisterRequest;
import com.backend.ecommercebackend.authentication.dto.response.AuthResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface AuthenticationService {
     AuthResponse register(RegisterRequest request, MultipartFile file);
     AuthResponse authenticate(AuthRequest request);
     void logout(LogoutRequest request);
     AuthResponse refreshAuthToken(HttpServletRequest request) throws ServletException, IOException;
}
