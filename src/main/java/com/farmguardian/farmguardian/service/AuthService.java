package com.farmguardian.farmguardian.service;

import com.farmguardian.farmguardian.config.jwt.JwtTokenProvider;
import com.farmguardian.farmguardian.domain.RefreshToken;
import com.farmguardian.farmguardian.domain.User;
import com.farmguardian.farmguardian.dto.request.SignInRequestDto;
import com.farmguardian.farmguardian.dto.request.SignOutRequestDto;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequestDto request) {
        // 이메일 중복 검증
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화 및 사용자 생성
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword);
        userRepository.save(user);
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

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 리프레시 토큰 저장 또는 갱신
        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        token -> token.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(new RefreshToken(user, refreshToken))
                );

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void signOut(SignOutRequestDto request) {
        // 리프레시 토큰 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public void withdraw(String authHeader) {
        // Authorization 헤더에서 토큰 추출
        String token = authHeader.replace("Bearer ", "");

        // 토큰 검증 및 이메일 추출
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        String email = jwtTokenProvider.getEmailFromToken(token);

        // 사용자 조회 및 삭제
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 리프레시 토큰 삭제
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        userRepository.delete(user);
    }
}