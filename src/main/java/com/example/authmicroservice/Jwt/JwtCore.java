package com.example.authmicroservice.Jwt;


import com.example.authmicroservice.Dao.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
@RequiredArgsConstructor
public class JwtCore {

    private final UserRepository userRepository;

    @Value("${spring.jwt.secretkey}")
    private String secretKey;

    @Value("${spring.jwt.expreation}")
    private String expreation;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateAccessToken(UserDetails userDetails) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new  Date().getTime() + Long.parseLong(expreation)))
                .signWith(getSigningKey())
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
                .compact();
    }
    /*
    public String generateRefreshToken(Authentication authentication) {
        return secretKey;
    }

     */

    public Claims extractPayload(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
    public String extractEmail(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public UserDetails extractUserDetails(String token) {
        return UserDetailsImpl.build(userRepository.findByEmail(extractEmail(token)).orElseThrow());
    }
}
