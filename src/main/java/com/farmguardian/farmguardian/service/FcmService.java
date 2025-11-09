package com.farmguardian.farmguardian.service;

import com.farmguardian.farmguardian.domain.FcmToken;
import com.farmguardian.farmguardian.domain.Platform;
import com.farmguardian.farmguardian.domain.User;
import com.farmguardian.farmguardian.dto.request.FcmSendRequestDto;
import com.farmguardian.farmguardian.exception.auth.UserNotFoundException;
import com.farmguardian.farmguardian.repository.FcmTokenRepository;
import com.farmguardian.farmguardian.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    // FCM 토큰 등록 또는 업데이트
    @Transactional
    public void registerToken(Long userId, String tokenValue, Platform platform) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Optional<FcmToken> existingToken = fcmTokenRepository.findByTokenValue(tokenValue);

        if (existingToken.isPresent()) {
            log.info("FCM token already existed: {}", userId);
        } else {
            // 새로운 토큰 등록
            FcmToken fcmToken = FcmToken.create(user, tokenValue, platform);
            fcmTokenRepository.save(fcmToken);
            log.info("New FCM token registered for user: {}", userId);
        }
    }

    // 특정 사용자에게 푸시 알림 전송
    public void sendNotificationToUser(Long userId, FcmSendRequestDto request) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserId(userId);

        if (tokens.isEmpty()) {
            log.warn("No FCM tokens found for user: {}", userId);
            return;
        }

        for (FcmToken fcmToken : tokens) {
            sendNotification(fcmToken.getTokenValue(), request);
        }
    }

    // 특정 토큰으로 푸시 알림 전송
    public void sendNotification(String token, FcmSendRequestDto request) {
        try {
            Notification notification = Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build();

            Message.Builder messageBuilder = Message.builder()
                    .setToken(token)
                    .setNotification(notification);

            // originImageId가 있으면 data payload에 추가
            if (request.getOriginImageId() != null) {
                messageBuilder.putData("originImageId", String.valueOf(request.getOriginImageId()));
            }

            // cloudUrl이 있으면 data payload에 추가
            if (request.getCloudUrl() != null) {
                messageBuilder.putData("cloudUrl", request.getCloudUrl());
            }

            // deviceId가 있으면 data payload에 추가
            if (request.getDeviceId() != null) {
                messageBuilder.putData("deviceId", String.valueOf(request.getDeviceId()));
            }

            String response = FirebaseMessaging.getInstance().send(messageBuilder.build());
            log.info("Successfully sent message: {}", response);

        } catch (Exception e) {
            log.error("Failed to send FCM message to token: {}", token, e);
        }
    }

    // 여러 사용자에게 동일한 알림 전송
    public void sendNotificationToUsers(List<Long> userIds, FcmSendRequestDto request) {
        for (Long userId : userIds) {
            sendNotificationToUser(userId, request);
        }
    }

    // 모든 사용자에게 브로드캐스트
    public void broadcastNotification(FcmSendRequestDto request) {
        List<FcmToken> allTokens = fcmTokenRepository.findAll();

        for (FcmToken fcmToken : allTokens) {
            sendNotification(fcmToken.getTokenValue(), request);
        }

        log.info("Broadcast notification sent to {} devices", allTokens.size());
    }

    // FCM 토큰 삭제
    @Transactional
    public void deleteToken(String tokenValue) {
        fcmTokenRepository.findByTokenValue(tokenValue)
                .ifPresent(fcmTokenRepository::delete);
        log.info("FCM token deleted: {}", tokenValue);
    }
}
