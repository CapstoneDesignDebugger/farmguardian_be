package com.farmguardian.farmguardian.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fcm_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_value", unique = true, nullable = false)
    private String tokenValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    public static FcmToken create(User user, String tokenValue, Platform platform) {
        FcmToken fcmToken = new FcmToken();
        fcmToken.user = user;
        fcmToken.tokenValue = tokenValue;
        fcmToken.platform = platform;
        return fcmToken;
    }

    // 토큰 업데이트
    public void updateToken(String newTokenValue) {
        this.tokenValue = newTokenValue;
    }
}