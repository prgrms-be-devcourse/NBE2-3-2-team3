package com.example.bestme.handler;

import com.example.bestme.util.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /*
    private final JwtTokenProvider jwtTokenProvider;


    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String userId = oAuth2User.getName();
//        String token = jwtProvider.create(userId);
//        String token = jwtProvider.generateToken();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.generateToken(authentication);

        // 클라이언트에 JWT 토큰 반환
        response.setHeader("Authorization", "Bearer " + jwtToken);
        getRedirectStrategy().sendRedirect(request, response, "/main"); // 로그인 후 리다이렉트 경로 설정
    }

     */
}
