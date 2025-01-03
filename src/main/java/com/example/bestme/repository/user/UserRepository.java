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

    // 이메일로 유저를 찾는 쿼리 - 회원 탈퇴 기준으로 반영
    @Query(value = "select u from User u where u.email = ?1 and u.deletedFlag = false")
    User findActiveByEmail(String email);

    // 닉네임으로 유저를 찾는 쿼리 - 회원 탈퇴 기준으로 반영
    @Query(value = "select u from User u where u.nickname = ?1 and u.deletedFlag = false")
    User findActiveByNickname(String nickname);

    // userId 로 유저를 찾는 쿼리
    @Query(value = "select u from User u where u.id = ?1 and u.deletedFlag = false")
    User findActiveByUserId(Long id);
}
