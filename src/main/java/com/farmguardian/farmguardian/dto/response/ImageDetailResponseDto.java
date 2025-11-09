package com.farmguardian.farmguardian.dto.response;

import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.OriginImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ImageDetailResponseDto {
    private Long originImageId;
    private String cloudUrl;
    private Integer width;
    private Integer height;
    private LocalDateTime createdAt;
    private Long deviceId;
    private String deviceAlias;
    private String deviceStatus;
    private String targetCrop;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String analysisResult;

    public static ImageDetailResponseDto from(OriginImage originImage) {
        Device device = originImage.getDevice();

        return ImageDetailResponseDto.builder()
                .originImageId(originImage.getId())
                .cloudUrl(originImage.getCloudUrl())
                .width(originImage.getWidth())
                .height(originImage.getHeight())
                .createdAt(originImage.getCreatedAt())
                .deviceId(device.getId())
                .deviceAlias(device.getAlias())
                .deviceStatus(device.getStatus() != null ? device.getStatus().name() : null)
                .targetCrop(device.getTargetCrop() != null ? device.getTargetCrop().name() : null)
                .latitude(device.getLatitude())
                .longitude(device.getLongitude())
                .analysisResult(originImage.getAnalysisResult())
                .build();
    }
}