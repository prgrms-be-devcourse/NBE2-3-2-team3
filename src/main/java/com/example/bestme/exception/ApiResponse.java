package com.example.bestme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean isSuccess;
    private HttpStatus code;
    private String message;
    private T data;

    // 성공 응답 (기본 메시지)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, HttpStatus.OK, "성공", data);
    }

    // 성공 응답 (커스텀 메시지)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, HttpStatus.OK, message, data);
    }

    // 성공 응답 (httpStatus 코드 종류 다양하게 할 시 사용)
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(true, httpStatus, message, data);
    }

    // 에러 응답
    public static ApiResponse<Void> error(HttpStatus httpStatus) {
        return new ApiResponse<>(false, httpStatus, "실패", null);
    }

    public static ApiResponse<Void> error(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(false, httpStatus, message, null);
    }
}
