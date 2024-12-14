package com.example.bestme.service;

import com.example.bestme.domain.Test;
import com.example.bestme.dto.TestResponse;
import com.example.bestme.repository.TestRepository;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public TestResponse getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("테스트를 찾을 수 없습니다."));
        return new TestResponse(test.getId(), test.getName(), test.getEmail());
    }
}
