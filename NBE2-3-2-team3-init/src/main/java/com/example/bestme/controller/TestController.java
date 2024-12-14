package com.example.bestme.controller;

import com.example.bestme.dto.TestResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<ApiResponse<TestResponse>> getTest(@RequestParam Long id) {
        TestResponse test = testService.getTestById(id);
        return ResponseEntity.ok(ApiResponse.success("Test found", test));
    }

    //    에러응답 예시
    @GetMapping("/error")
    public ResponseEntity<ApiResponse<Void>> errorResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid Request", HttpStatus.BAD_REQUEST));
    }

}

