package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.RefreshToken;
import com.farmguardian.farmguardian.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByUser(User user);
}
