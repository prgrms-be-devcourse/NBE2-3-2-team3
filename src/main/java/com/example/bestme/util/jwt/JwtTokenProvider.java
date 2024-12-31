package com.example.bestme.util.jwt;

import com.example.bestme.exception.ApiResponse;
import com.example.bestme.repository.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final UserRepository userRepository;

    // application.properties 의 시크릿 키 값 가져와서 key 에 저장
    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey, InMemoryClientRegistrationRepository clientRegistrationRepository, UserRepository userRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.userRepository = userRepository;
    }


    // User 정보를 가지고 accessToken, refreshToken 생성
    public JwtTokenDTO generateToken(Authentication authentication) {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        LocalDateTime now = LocalDateTime.now();

        com.example.bestme.domain.user.User user = userRepository.findByEmail(authentication.getName());

        // 30 분의 유효기간 - accessToken
        LocalDateTime accessTokenExpire = now.plusMinutes(30);
        String accessToken = Jwts.builder()
                .setId(String.valueOf(user.getId())) // user 의 id
                .setSubject(user.getEmail()) // user 의 email
                .claim("auth", authorities) // user 의 role
                .setExpiration(Date.from(accessTokenExpire.atZone(ZoneId.systemDefault()).toInstant())) // 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 1 주일의 유효기간 - refreshToken
        LocalDateTime refreshTokenExpire = now.plusDays(7);
        String refreshToken = Jwts.builder()
                .setId(String.valueOf(user.getId())) // user 의 id
                .setSubject(user.getEmail()) // user 의 email
                .claim("auth", authorities) // user 의 role
                .setExpiration(Date.from(refreshTokenExpire.atZone(ZoneId.systemDefault()).toInstant())) // 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 가져오는 메서드
    public Authentication getAuthentication(String accessToken) {

        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") == null) {
            return null;
        }

        // claims 에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        // UserDetails - 인터페이스 / User - UserDetails 구현 클래스
        // UserDetails 객체를 만들어서 return Authentication
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 검증 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }  catch (SecurityException | MalformedJwtException e) {
            return false;
        }  catch (Exception e) {
            System.out.println("입력 token 만료");
        }
        return false;
    }

    public JwtTokenDTO generateTokenSimple(String email) {

        com.example.bestme.domain.user.User user = userRepository.findByEmail(email);

        LocalDateTime now = LocalDateTime.now();

        // 30 분의 유효기간 - accessToken
        LocalDateTime accessTokenExpire = now.plusMinutes(30);
        String accessToken = Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .setSubject(user.getEmail())
                .claim("auth", user.getRole())
                .setExpiration(Date.from(accessTokenExpire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 1 주일의 유효기간 - refreshToken
        LocalDateTime refreshTokenExpire = now.plusDays(7);
        String refreshToken = Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .setSubject(user.getEmail())
                .claim("auth", user.getRole())
                .setExpiration(Date.from(refreshTokenExpire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 기존 refreshToken 정보를 가지고 다시 accessToken, refreshToken 생성
    public JwtTokenDTO reGenerateToken(String refreshToken) {

        Claims claims = parseClaims(refreshToken);

        LocalDateTime now = LocalDateTime.now();

        // 30 분의 유효기간 - accessToken
        LocalDateTime accessTokenExpire = now.plusMinutes(30);
        String accessToken = Jwts.builder()
                .setId(claims.getId())
                .setSubject(claims.getSubject())
                .claim("auth", claims.get("auth"))
                .setExpiration(Date.from(accessTokenExpire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 1 주일의 유효기간 - refreshToken
        LocalDateTime refreshTokenExpire = now.plusDays(7);
        String newRefreshToken = Jwts.builder()
                .setId(claims.getId())
                .setSubject(claims.getSubject())
                .claim("auth", claims.get("auth"))
                .setExpiration(Date.from(refreshTokenExpire.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    // 토큰 재발급 메서드
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String Token = getRefreshToken(request);
        if (Token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED, "실패", null));
        }
        JwtTokenDTO jwtTokenDTO = reGenerateToken(Token);
        String accessToken = jwtTokenDTO.getGrantType() + " " + jwtTokenDTO.getAccessToken();
        saveRefreshToken(response, jwtTokenDTO.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(accessToken));
    }

    // accessToken
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // request 의 header 에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }

    public void saveRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setMaxAge(60*60*24*7); // 일주일
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                if (name.equals("refresh")) {
                    return value;
                }
            }
        }
        return null;
    }

    public void deleteRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

