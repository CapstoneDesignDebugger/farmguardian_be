package com.farmguardian.farmguardian.service;

import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.OriginImage;
import com.farmguardian.farmguardian.dto.request.ImageMetadataRequestDto;
import com.farmguardian.farmguardian.dto.response.FastApiResponseDto;
import com.farmguardian.farmguardian.dto.response.ImageResponseDto;
import com.farmguardian.farmguardian.repository.OriginImageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final OriginImageRepository originImageRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OriginImage saveMetaData(ImageMetadataRequestDto request, Device device) {

        OriginImage originImage = OriginImage.builder()
                .device(device)
                .cloudUrl(request.getCloudUrl())
                .width(request.getWidth())
                .height(request.getHeight())
                .build();

        originImage = originImageRepository.save(originImage);
        return originImage;
    }

    @Transactional
    public void saveAnalysisResult(Long originImageId, FastApiResponseDto fastApiResponse) {
        String analysisResultJson = convertToJson(fastApiResponse);

        OriginImage originImage = originImageRepository.findById(originImageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다"));

        originImage.updateAnalysisResult(analysisResultJson);
        log.info("이미지 분석 결과 저장 완료 - originImageId: {}", originImageId);
    }

    private String convertToJson(FastApiResponseDto response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패: {}", e.getMessage(), e);
            throw new RuntimeException("분석 결과 JSON 변환 실패", e);
        }
    }

    // 사용자의 모든 디바이스 이미지 목록 조회
    public Slice<ImageResponseDto> getAllImagesByUser(Long userId, Pageable pageable) {
        Slice<OriginImage> images = originImageRepository.findAllByDevice_User_IdOrderByCreatedAtDesc(userId, pageable);
        return images.map(ImageResponseDto::from);
    }

    // 특정 디바이스의 이미지 목록 조회
    public Slice<ImageResponseDto> getImagesByDevice(Long deviceId, Pageable pageable) {
        Slice<OriginImage> images = originImageRepository.findAllByDevice_IdOrderByCreatedAtDesc(deviceId, pageable);
        return images.map(ImageResponseDto::from);
    }

    // 이미지 상세 조회
    public OriginImage getImageById(Long originImageId) {
        return originImageRepository.findById(originImageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다"));
    }
}