package com.farmguardian.farmguardian.dto.response;

import com.farmguardian.farmguardian.domain.OriginImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ImageResponseDto {
    private Long originImageId;
    private String cloudUrl;
    private Integer width;
    private Integer height;
    private LocalDateTime createdAt;
    private Long deviceId;
    private String deviceAlias;
    private Boolean hasAnalysisResult;

    public static ImageResponseDto from(OriginImage originImage) {
        return ImageResponseDto.builder()
                .originImageId(originImage.getId())
                .cloudUrl(originImage.getCloudUrl())
                .width(originImage.getWidth())
                .height(originImage.getHeight())
                .createdAt(originImage.getCreatedAt())
                .deviceId(originImage.getDevice().getId())
                .deviceAlias(originImage.getDevice().getAlias())
                .hasAnalysisResult(originImage.getAnalysisResult() != null)
                .build();
    }
}