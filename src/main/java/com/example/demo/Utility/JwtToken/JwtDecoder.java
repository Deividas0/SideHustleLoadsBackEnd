package com.example.demo.Utility.JwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtDecoder {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims decodeJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String decodeUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // Parse the JWT and validate its signature
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Use the signing key
                .build()
                .parseClaimsJws(token.replace("Bearer ", "")) // Remove "Bearer " prefix if present
                .getBody();

        return claims.getSubject(); // Extract the subject (username)
    }

    public Integer decodeUserIdFromToken(String token) {
        Claims claims = decodeJwt(token.replace("Bearer ", ""));
        return claims.get("id", Integer.class);
    }

}
