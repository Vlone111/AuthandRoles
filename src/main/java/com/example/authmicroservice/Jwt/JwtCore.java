package com.example.authmicroservice.Jwt;


import com.example.authmicroservice.Dao.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Value("${spring.jwt.expiration}")
    private String expreation;


    @Value("${spring.jwt.refreshtokenexpiration}")
    private String refreshtokenexpiration;

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
    //вопрос можно ли по просто по почте
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(new  Date().getTime() + Long.parseLong(refreshtokenexpiration)))
                .signWith(getSigningKey())
                .subject(userDetails.getUsername())
                .compact();
    }

    public void putRefreshinHttpCockieOnly(String refreshToken, HttpServletResponse  httpServletResponse) {
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // https НЕ ЗАБЫТЬ ДЛЯ ПРОДА ВЫЛОЖИТЬ АЛОООООООООООООООООООООООООоо
        refreshCookie.setPath("/api/v1/auth");
        refreshCookie.setMaxAge((int)(Long.parseLong(refreshtokenexpiration)/1000));
        httpServletResponse.addCookie(refreshCookie);
    }


    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(getSigningKey()).build().parseClaimsJws(token); //тупая проверка
            return true;
        }
        catch (ExpiredJwtException e){
            return false;
        }
    }


    public String refreshacessToken(String refreshtoken) {
        if (refreshtoken != null && validateToken(refreshtoken)) {
            UserDetailsImpl userDetails = UserDetailsImpl.build(userRepository.findByEmail(getEmailFromToken(refreshtoken)).orElseThrow());
            return generateAccessToken(userDetails);
        }
        throw new IllegalArgumentException("Invalid refresh token");
    }
    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Claims extractPayload(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }


}
