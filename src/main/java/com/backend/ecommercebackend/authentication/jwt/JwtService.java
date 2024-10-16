package com.backend.ecommercebackend.authentication.jwt;

import com.backend.ecommercebackend.cache.service.RedisTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.key}")
    private String secretKey;
    @Value ("${refresh-token.expiration}")
    private Long refreshTokenExpiration;
    @Value ("${access-token.expiration}")
    private Long accessTokenExpiration;

    private final RedisTokenService redisTokenService;

    public String generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email,accessTokenExpiration);
    }

    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        String refreshToken = createToken(claims, email, refreshTokenExpiration);
        redisTokenService.storeRefreshToken(email, refreshToken, refreshTokenExpiration);
        return refreshToken;
    }


    private String createToken(Map<String, Object> claims, String email, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration ))
                .signWith(SignatureAlgorithm.HS512,getSigninKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
     Claims claims = extractAllClaim(token);
     return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
       return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(UserDetails user, String token) {
        String username= extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }




    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
