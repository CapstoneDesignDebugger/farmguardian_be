package com.farmguardian.farmguardian.controller;

import com.farmguardian.farmguardian.dto.request.FcmSendRequestDto;
import com.farmguardian.farmguardian.dto.request.FcmTokenRegisterRequestDto;
import com.farmguardian.farmguardian.service.FcmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    // FCM 토큰 등록
    @PostMapping("/token")
    public ResponseEntity<Void> registerToken(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody FcmTokenRegisterRequestDto request) {
        fcmService.registerToken(userId, request.getToken(), request.getPlatform());
        return ResponseEntity.ok().build();
    }

    // FCM 토큰 삭제
    @DeleteMapping("/token")
    public ResponseEntity<Void> deleteToken(@RequestParam String token) {
        fcmService.deleteToken(token);
        return ResponseEntity.ok().build();
    }

    // 특정 사용자에게 푸시 알림 전송 (관리자용 또는 테스트용)
    @PostMapping("/send/{userId}")
    public ResponseEntity<Void> sendNotification(
            @PathVariable Long userId,
            @Valid @RequestBody FcmSendRequestDto request) {
        fcmService.sendNotificationToUser(userId, request);
        return ResponseEntity.ok().build();
    }

    // 모든 사용자에게 브로드캐스트 (관리자용)
    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcastNotification(
            @Valid @RequestBody FcmSendRequestDto request) {
        fcmService.broadcastNotification(request);
        return ResponseEntity.ok().build();
    }
}