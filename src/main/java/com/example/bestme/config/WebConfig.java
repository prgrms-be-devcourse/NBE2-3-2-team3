package com.example.bestme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// Spring MVC의 설정 확장 클래스
// HTTP 요청 및 응답 처리 방식, 메시지 컨버터 설정, 인터셉터 추가, 리소스 핸들러 설정 등을 커스터마이즈 ( Spring Boot의 Web MVC 자동설정 모두 적용 )
@Configuration
//@EnableWebMvc       // @EnableWebMvc (Spring Boot의 Web MVC 자동설정 무시) - 관련 정보 : https://live-everyday.tistory.com/265
public class WebConfig implements WebMvcConfigurer {

    private final MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter;

    public WebConfig(MultipartJackson2HttpMessageConverter multipartJackson2HttpMessageConverter) {
        this.multipartJackson2HttpMessageConverter = multipartJackson2HttpMessageConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // MultipartJackson2HttpMessageConverter를 추가하여 multipart/form-data 요청을 처리할 수 있게 설정
        converters.add(multipartJackson2HttpMessageConverter);
    }
}
