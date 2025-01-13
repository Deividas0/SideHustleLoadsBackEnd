package com.example.demo.Utility.JwtToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtGenerator {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateJwt(String username, int id) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));


        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long oneYearInMillis = 365L * 24 * 60 * 60 * 1000;
        Date expiration = new Date(nowMillis + oneYearInMillis);

        return Jwts.builder()
                .setIssuer("Side Hustle Loads")
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
