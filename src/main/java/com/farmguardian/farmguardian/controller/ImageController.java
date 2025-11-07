package com.farmguardian.farmguardian.controller;

import com.farmguardian.farmguardian.config.auth.UserDetailsImpl;
import com.farmguardian.farmguardian.domain.OriginImage;
import com.farmguardian.farmguardian.dto.request.ImageMetadataRequestDto;
import com.farmguardian.farmguardian.dto.response.ImageAnalysisResponseDto;
import com.farmguardian.farmguardian.dto.response.ImageResponseDto;
import com.farmguardian.farmguardian.service.ImageAnalyzeService;
import com.farmguardian.farmguardian.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageAnalyzeService imageAnalyzeService;
    private final ImageService imageService;

    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisResponseDto> analyzeImage(@RequestBody ImageMetadataRequestDto request) {
        ImageAnalysisResponseDto response = imageAnalyzeService.analyzeImage(request);
        return ResponseEntity.ok(response);
    }

    // 내 모든 디바이스의 이미지 목록 조회 (Slice 방식)
    @GetMapping
    public ResponseEntity<Slice<ImageResponseDto>> getAllMyImages(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        Long userId = userDetails.getUserId();
        Slice<ImageResponseDto> images = imageService.getAllImagesByUser(userId, pageable);
        return ResponseEntity.ok(images);
    }

    // 특정 디바이스의 이미지 목록 조회 (Slice 방식)
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<Slice<ImageResponseDto>> getImagesByDevice(
            @PathVariable Long deviceId,
            @PageableDefault(size = 20) Pageable pageable) {
        Slice<ImageResponseDto> images = imageService.getImagesByDevice(deviceId, pageable);
        return ResponseEntity.ok(images);
    }

    // 이미지 상세 조회
    @GetMapping("/{originImageId}")
    public ResponseEntity<OriginImage> getImageDetail(@PathVariable Long originImageId) {
        OriginImage image = imageService.getImageById(originImageId);
        return ResponseEntity.ok(image);
    }
}
