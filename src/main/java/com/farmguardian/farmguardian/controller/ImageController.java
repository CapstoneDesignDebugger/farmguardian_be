package com.farmguardian.farmguardian.controller;

import com.farmguardian.farmguardian.dto.request.ImageMetadataRequestDto;
import com.farmguardian.farmguardian.dto.response.ImageAnalysisResponseDto;
import com.farmguardian.farmguardian.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisResponseDto> analyzeImage(@RequestBody ImageMetadataRequestDto request) {
        ImageAnalysisResponseDto response = imageService.analyzeImage(request);
        return ResponseEntity.ok(response);
    }
}

/*
localhost:8080/api/images/analyze
 */