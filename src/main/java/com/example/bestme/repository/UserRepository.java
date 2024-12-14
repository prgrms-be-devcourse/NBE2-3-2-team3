package com.example.bestme.repository;

import com.example.bestme.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 필요한 커스텀 쿼리 메서드 추가 가능
}

