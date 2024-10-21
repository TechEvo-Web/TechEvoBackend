package com.backend.ecommercebackend.authentication.service;

import com.backend.ecommercebackend.authentication.dto.response.AuthResponse;
import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.model.user.Role;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private static final String CLIENT_ID = "523070151170-f80npg62nl1rapmi86m2f2c2efgthibi.apps.googleusercontent.com";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String imageUrl = oAuth2User.getAttribute("picture");
        String googleId = oAuth2User.getAttribute("google_id");

        AuthResponse authResponse = registerOrLoginUser(email, firstName, lastName, imageUrl,googleId);


        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("accessToken", authResponse.getAccessToken());
        attributes.put("refreshToken", authResponse.getRefreshToken());

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "email");
    }

    private AuthResponse registerOrLoginUser(String email, String firstName, String lastName, String googleId, String imageUrl) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {

            User userEntity = existingUser.get();
            userEntity.setGoogleId(googleId);
            String accessToken = jwtService.generateAccessToken(userEntity.getEmail());
            String refreshToken = jwtService.generateRefreshToken(userEntity.getEmail());
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setGoogleId(googleId);
            newUser.setProfileImg(imageUrl);
            newUser.setRole(Role.USER);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setPassword("default_password");


            userRepository.save(newUser);


            String accessToken = jwtService.generateAccessToken(newUser.getEmail());
            String refreshToken = jwtService.generateRefreshToken(newUser.getEmail());


            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
    public AuthResponse processGoogleLogin(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            if (firstName == null) {
                firstName = "Unknown";
            }

            if (lastName == null) {
                lastName = "Unknown";
            }

            return registerOrLoginUser(email, firstName, lastName, userId, pictureUrl);
        } else {
            throw new RuntimeException("Invalid ID token.");
        }
    }
}
