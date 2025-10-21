package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.RefreshToken;
import com.farmguardian.farmguardian.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUser(User user);

    Optional<RefreshToken> findByUserAndClientUuid(User user, String clientUuid);

    Optional<RefreshToken> findByTokenAndUser(String token, User user);

    boolean existsByToken(String token);

    @Modifying
    void deleteByUser(User user);

    @Modifying
    void deleteByUserAndClientUuid(User user, String clientUuid);
}
