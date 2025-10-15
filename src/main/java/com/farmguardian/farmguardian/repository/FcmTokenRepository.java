package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.FcmToken;
import com.farmguardian.farmguardian.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    // 토큰 값으로 조회
    Optional<FcmToken> findByTokenValue(String tokenValue);

    // 사용자별 토큰 조회
    List<FcmToken> findByUser(User user);

    // 사용자 ID로 토큰 조회
    List<FcmToken> findByUserId(Long userId);

    // 토큰 존재 여부 확인
    boolean existsByTokenValue(String tokenValue);
}
