package com.example.bestme.util.jwt;


import com.example.bestme.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        String refreshToken = resolveRefreshToken((HttpServletRequest) request);

        // 토큰이 null 이 아닐 때
        if (token != null) {

            // 토큰이 유효할 때
            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 토큰이 유효하지 않을 때
            } else if (refreshToken !=null
                    && jwtTokenProvider.validateToken(refreshToken)){

                // refreshToken 이 유효한 경우
                // 기존 refreshToken 기반 새로운 access, refresh 토큰 발급
                JwtTokenDTO jwtTokenDTO = jwtTokenProvider.reGenerateToken(refreshToken);

                // header 에 저장
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.setHeader("Authorization", jwtTokenDTO.getGrantType() + " " + jwtTokenDTO.getAccessToken());
                resp.setHeader("refresh", jwtTokenDTO.getGrantType() + " " + jwtTokenDTO.getRefreshToken());
                resp.addHeader("Access-Control-Expose-Headers","Authorization, refresh");

                Authentication authentication = jwtTokenProvider.getAuthentication(jwtTokenDTO.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    // request 의 header 에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        String token = request.getHeader("refresh");
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }
}


