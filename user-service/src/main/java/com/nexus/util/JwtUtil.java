package com.nexus.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final Long jwtExpirationInMs;


    public JwtUtil(@Value("${app.jwt.secret}") String Secret, @Value("${app.jwt.expiration-ms}") Long jwtExpirationInMs) {
        this.key = Keys.hmacShaKeyFor(Secret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(Long userId, String email, String role) {
        long nowMillis = System.currentTimeMillis();
        return
                Jwts.builder()
                    .subject(String.valueOf(userId))
                    .claim("email", email)
                    .claim("role", role)
                    .issuedAt(new Date(nowMillis))
                    .expiration(new Date(nowMillis + jwtExpirationInMs))
                    .signWith(key)
                    .compact();
    }

    public void validateToken(String token) {
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
