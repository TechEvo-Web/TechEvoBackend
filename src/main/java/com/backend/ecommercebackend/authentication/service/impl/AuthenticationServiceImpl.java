package com.backend.ecommercebackend.authentication.service.impl;

import com.backend.ecommercebackend.authentication.dto.request.LogoutRequest;
import com.backend.ecommercebackend.authentication.dto.request.RegisterRequest;
import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.authentication.dto.request.AuthRequest;
import com.backend.ecommercebackend.authentication.mapper.AuthMapper;
import com.backend.ecommercebackend.authentication.dto.response.AuthResponse;
import com.backend.ecommercebackend.authentication.service.AuthenticationService;
import com.backend.ecommercebackend.cache.service.RedisTokenService;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.user.Role;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.FileStorageService;
import com.backend.ecommercebackend.service.impl.EmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final FileStorageService fileStorageService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final UserDetailsService userDetailsService;
    private final RedisTokenService redisTokenService;
    private final EmailServiceImpl emailService;

    @Override
    public AuthResponse register(RegisterRequest request, MultipartFile file) {

        if(!request.getConfirmPassword().equals(request.getPassword())){
            throw new ApplicationException(Exceptions.PASSWORD_MISMATCH_EXCEPTION);
        }

        if(request.getAcceptTerms().equals(Boolean.FALSE)){
            throw new ApplicationException(Exceptions.TERMS_ACCEPTANCE_EXCEPTION);
        }

        Optional<User> existingUser = repository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new ApplicationException(Exceptions.USER_ALREADY_EXIST);
        }

        User user = authMapper.RegisterDtoToEntity(request,passwordEncoder);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        try {
            String url = fileStorageService.storeImages(file,"profileImages");
            user.setProfileImg(url);
        }catch (IOException e){
            throw new ApplicationException(Exceptions.IMAGE_STORAGE_EXCEPTION);
        }
        repository.save(user);

        emailService.deleteStoredEmail(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken=jwtService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        User user = repository.findByEmail(request.getEmail()).orElseThrow(()-> new ApplicationException(Exceptions.USER_NOT_FOUND));
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken=jwtService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        var token = request.getRefreshToken();
        String email = request.getEmail();
            if (redisTokenService.validateRefreshToken(email, token)) {
                redisTokenService.deleteRefreshToken(email);
            } else {
                throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION);
            }
        }

    @Override
    public AuthResponse refreshAuthToken(HttpServletRequest request) throws IOException {
        String userEmail;
        String refreshToken;
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION);
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.validateToken(user, refreshToken)) {
                String accessToken = jwtService.generateAccessToken(userEmail);
                return AuthResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken).build();
            } else {
                throw new ApplicationException(Exceptions.USER_NOT_FOUND);
            }
        } else {
            throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION);
        }
    }
}
