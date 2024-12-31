package com.example.bestme.repository.user;

import com.example.bestme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 x"));
    }

    // 이메일(Unique)로 유저를 찾는 쿼리
    @Query(value = "select u from User u where u.email = ?1 and u.deletedFlag = false")
    User findByEmail(String email);

    // 닉네임(Unique)으로 유저를 찾는 쿼리
    @Query(value = "select u from User u where u.nickname = ?1 and u.deletedFlag = false")
    User findByNickname(String nickname);

    // userId 로 유저를 찾는 쿼리
    @Query(value = "select u from User u where u.userId = ?1 and u.deletedFlag = false")
    User findByUserId(Long id);
}
