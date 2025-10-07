package com.farmguardian.farmguardian.controller;

import com.farmguardian.farmguardian.dto.request.SignInRequestDto;
import com.farmguardian.farmguardian.dto.request.SignOutRequestDto;
import com.farmguardian.farmguardian.dto.request.SignUpRequestDto;
import com.farmguardian.farmguardian.dto.response.TokenResponseDto;
import com.farmguardian.farmguardian.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto request) {
        authService.signUp(request);
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDto> signIn(@Valid @RequestBody SignInRequestDto request) {
        TokenResponseDto response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(@Valid @RequestBody SignOutRequestDto request) {
        authService.signOut(request);
        return ResponseEntity.ok().build();
    }

    // 회원탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestHeader("Authorization") String token) {
        authService.withdraw(token);
        return ResponseEntity.ok().build();
    }
}
