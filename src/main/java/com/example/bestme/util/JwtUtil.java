package com.example.bestme.util;

import com.example.bestme.dto.user.CustomUserInfoDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;

    // 생성자
    public JwtUtil(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime)
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    // Access 토큰 생성
    public String createAccessToken(CustomUserInfoDTO to) {
        return createToken(to);
    }

    // Jwt 생성
    private String createToken(CustomUserInfoDTO to) {
        Claims claims = Jwts.claims();
        claims.put("userId", to.getUserId());
        claims.put("email", to.getEmail());
        claims.put("role", to.getRole());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(tokenValidity.toInstant(ZoneOffset.UTC)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);

        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);

        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);

        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}
