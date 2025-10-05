package com.farmguardian.farmguardian.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "devices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetCrop targetCrop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_latitude", precision = 10, scale = 7)
    private BigDecimal deviceLatitude;

    @Column(name = "device_longitude", precision = 10, scale = 7)
    private BigDecimal deviceLongitude;
}
