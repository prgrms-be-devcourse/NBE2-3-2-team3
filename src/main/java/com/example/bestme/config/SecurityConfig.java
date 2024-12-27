package com.example.bestme.config;

import com.example.bestme.handler.OAuth2SuccessHandler;
import com.example.bestme.service.KakaoService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@Configurable
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;

    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // csrf 사용 x
                .csrf(AbstractHttpConfigurer::disable)
                // session 사용 x - jwt 사용
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // 권한에 따라 접근 제한
                        // .requestMatchers("/test").hasAuthority("USER")
                        // 여러 권한 제안할 시
                        // .requestMatchers("/test/**").hasAnyAuthority("USER", "ADMIN")

                        // 해당하는 요청들은 모든 사용자에게 허용
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/styles/**", "/imgs/**", "/scripts/**","/static/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/main", "/main/**").permitAll()
                        .requestMatchers("/login", "/login/**","/join", "/resetPassword").permitAll()
                        .requestMatchers("/personal", "/personal/**").permitAll()
                        .requestMatchers("/style", "/style/**").permitAll()
                        .requestMatchers("/community", "/community/**").permitAll()
                        .requestMatchers("/member", "/member/**").permitAll()
                        .requestMatchers("/results", "/results/**").permitAll() //임시로 해놓았습니다. url 변경시 삭제
                        .requestMatchers("/manager").permitAll()
                        .requestMatchers("/auth/kakao/callback").permitAll()

                        // 다른 요청들은 거부
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
