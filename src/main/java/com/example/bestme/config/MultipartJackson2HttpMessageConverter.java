package com.example.bestme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

// Multipart 설정 클래스
// 요청의 데이터의 타입(Content-type)이 json과 multipart/form-data로 두 가지일 때 실행
// AbstractJackson2HttpMessageConverter : JSON 데이터를 변환하는 기본 컨버터
@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        // 부모 클래스의 생성자를 호출하여 ObjectMapper를 전달하고, 기본 미디어 타입을 **application/octet-stream**으로 설정
        // 파일 업로드 시 사용하는 미디어 타입
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    // canWrite 메서드들 : 메시지 컨버터가 주어진 클래스와 미디어 타입에 대해 메시지를 쓸 수 있는지 판단하는 메서드
    // 쓰기 기능 X , 읽기 전용으로만 설정
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
