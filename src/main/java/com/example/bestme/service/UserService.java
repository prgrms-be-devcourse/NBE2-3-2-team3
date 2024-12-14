package com.example.bestme.service;

import com.example.bestme.domain.User;
import com.example.bestme.dto.UserResponse;
import com.example.bestme.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
