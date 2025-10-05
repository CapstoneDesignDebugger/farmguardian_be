package com.farmguardian.farmguardian.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "result_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_image_id", nullable = false, unique = true)
    private OriginImage originImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false, length = 512)
    private String url;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer width;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "bbox_confidence", columnDefinition = "TEXT")
    private String bboxConfidence;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}