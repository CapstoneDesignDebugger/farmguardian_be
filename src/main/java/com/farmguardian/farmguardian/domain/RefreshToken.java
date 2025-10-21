package com.farmguardian.farmguardian.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refresh_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "client_uuid"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "client_uuid", nullable = false)
    private String clientUuid;

    public RefreshToken(User user, String token, String clientUuid) {
        this.user = user;
        this.token = token;
        this.clientUuid = clientUuid;
    }

    public RefreshToken updateToken(String newToken){
        this.token = newToken;
        return this;
    }
}
