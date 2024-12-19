package com.example.bestme.service.impl;

import com.example.bestme.domain.user.CustomOAuth2User;
import com.example.bestme.domain.user.User;
import com.example.bestme.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        try{
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e){
            e.printStackTrace();
        }

        User userEntity = null;
        String userId = null;
        String nickname = null;
        String email = null;

        if(oauthClientName.equals("kakao")){
            Map<String, Object> attributes = oAuth2User.getAttributes();
            userId = String.valueOf(attributes.get("id"));

            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            if (properties != null) {
                nickname = (String) properties.get("nickname");
            }

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");
            }

            System.out.println("아이디 : " + userId);
            System.out.println("닉네임 : " + nickname);
            System.out.println("이메일 : " + email);

            if (!userRepository.existsById(userId)) {
                userEntity = new User(userId, email, nickname);
                userRepository.save(userEntity);
            }

        }

        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userId, oAuth2User.getAttributes(), authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());



        return new CustomOAuth2User(userId);

    }
}
