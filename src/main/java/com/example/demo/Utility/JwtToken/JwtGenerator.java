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

    public String generateJwt(String username, int id, String country) {
        // Generate a signing key
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // Set the current time and expiration time (1 day in milliseconds)
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long oneYearInMillis = 365L * 24 * 60 * 60 * 1000; // 1 year
        Date expiration = new Date(nowMillis + oneYearInMillis);

        // Create and sign the JWT token
        return Jwts.builder()
                .setIssuer("Side Hustle Loads") // Set the token issuer
                .setSubject(username) // Username as the subject
                .claim("id", id) // Include user ID
                .claim("country", country) // Include user ID
                .setIssuedAt(now) // Set the issued time
                .setExpiration(expiration) // Set the expiration time
                .setId(UUID.randomUUID().toString()) // Add a unique identifier
                .signWith(key, SignatureAlgorithm.HS256) // Sign with the secret key
                .compact(); // Build the token
    }
}
