package com.farmguardian.farmguardian.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "origin_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "origin_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "cloud_url", nullable = false, length = 512)
    private String cloudUrl;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer width;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "json")
    private String analysisResult;  // JSON 문자열 그대로 저장
}