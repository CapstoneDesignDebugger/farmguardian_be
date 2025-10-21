package com.farmguardian.farmguardian.service;

import com.farmguardian.farmguardian.config.jwt.JwtTokenProvider;
import com.farmguardian.farmguardian.domain.RefreshToken;
import com.farmguardian.farmguardian.domain.Role;
import com.farmguardian.farmguardian.domain.User;
import com.farmguardian.farmguardian.dto.request.SignInRequestDto;
import com.farmguardian.farmguardian.dto.request.SignUpRequestDto;
import com.farmguardian.farmguardian.dto.response.TokenResponseDto;
import com.farmguardian.farmguardian.repository.RefreshTokenRepository;
import com.farmguardian.farmguardian.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증, 인가에 대한 내용
 * 회원가입, 로그인, 로그아웃, 회원탈퇴
 * 자체로그인만 구현, 소셜로그인 x
 */

// TODO : 검증 로직들 세분화 필요. 에러 핸들러 클래스 만들어서 throw.

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long signUp(SignUpRequestDto request) {
        // 이메일 중복 검증
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화 및 사용자 생성
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword, Role.USER);
        User saved = userRepository.save(user);

        return saved.getId();
    }

    @Transactional
    public TokenResponseDto signIn(SignInRequestDto request) {
        // 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성 (userId 포함)
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name(), user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 디바이스별 리프레시 토큰 저장 또는 갱신
        refreshTokenRepository.findByUserAndClientUuid(user, request.getClientUuid())
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(user, refreshToken, request.getClientUuid()))
                );

        return new TokenResponseDto("Bearer", accessToken, refreshToken);
    }

    @Transactional
    public void signOut(Long userId, String refreshTokenValue, String clientUuid) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 디바이스별 리프레시 토큰 검증 및 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByUserAndClientUuid(user, clientUuid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public void withdraw(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 모든 디바이스의 리프레시 토큰 삭제
        refreshTokenRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}