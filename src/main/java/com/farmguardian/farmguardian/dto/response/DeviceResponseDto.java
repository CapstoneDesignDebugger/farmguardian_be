package com.farmguardian.farmguardian.dto.response;

import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.TargetCrop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class DeviceResponseDto {
    private Long deviceId;
    private TargetCrop targetCrop;
    private String targetCropName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DeviceResponseDto from(Device device) {
        return DeviceResponseDto.builder()
                .deviceId(device.getId())
                .targetCrop(device.getTargetCrop())
                .targetCropName(device.getTargetCrop() != null ? device.getTargetCrop().getKoreanName() : null)
                .latitude(device.getLatitude())
                .longitude(device.getLongitude())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }
}