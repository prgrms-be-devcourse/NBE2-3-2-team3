package com.example.bestme.service.impl;

import com.example.bestme.domain.user.CustomOAuth2User;
import com.example.bestme.domain.user.Role;
import com.example.bestme.domain.user.User;
import com.example.bestme.repository.user.UserRepository;
import com.example.bestme.util.jwt.JwtTokenDTO;
import com.example.bestme.util.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {  // 카카오로 로그인한 사용자 정보 가져오는 클래스

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String email = (String) oAuth2User.getAttributes().get("email");
        String nickname = (String) oAuth2User.getAttributes().get("nickname");
        //String oauthClientName = request.getClientRegistration().getClientName();

        String userId = null;

        Map<String, Object> attributes = oAuth2User.getAttributes();
        userId = String.valueOf(attributes.get("id"));


        System.out.println("아이디 : " + userId);
        System.out.println("닉네임 : " + nickname);
        System.out.println("이메일 : " + email);

        User userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            User newUser = new User(null, email, null, nickname, true, LocalDateTime.now(), null, false, Role.USER);
            userEntity = userRepository.save(newUser);
        }

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userEntity.getEmail(), "default_password"));
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().name()));
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userEntity.getEmail(), oAuth2User.getAttributes(), authorities);


        return customOAuth2User;

    }
}
