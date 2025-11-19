package com.example.authmicroservice.Jwt;


import org.springframework.beans.factory.annotation.Value;

public class JwtCore {
    @Value("${spring.jwt.secretkey}")
    private String secretKey;

    @Value("${spring.jwt.expreation}")
    private String expreation;


}
