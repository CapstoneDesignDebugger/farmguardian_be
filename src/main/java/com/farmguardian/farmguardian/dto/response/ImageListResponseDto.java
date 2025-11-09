package com.farmguardian.farmguardian.dto.response;

import com.farmguardian.farmguardian.domain.OriginImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ImageListResponseDto {
    private Long originImageId;
    private LocalDateTime createdAt;
    private Long deviceId;
    private String deviceAlias;
    private Boolean hasAnalysisResult;

    public static ImageListResponseDto from(OriginImage originImage) {
        return ImageListResponseDto.builder()
                .originImageId(originImage.getId())
                .createdAt(originImage.getCreatedAt())
                .deviceId(originImage.getDevice().getId())
                .deviceAlias(originImage.getDevice().getAlias())
                .hasAnalysisResult(originImage.getAnalysisResult() != null)
                .build();
    }
}