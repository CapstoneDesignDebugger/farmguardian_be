package com.farmguardian.farmguardian.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.OriginImage;
import com.farmguardian.farmguardian.dto.request.FastApiRequestDto;
import com.farmguardian.farmguardian.dto.request.FcmSendRequestDto;
import com.farmguardian.farmguardian.dto.request.ImageMetadataRequestDto;
import com.farmguardian.farmguardian.dto.response.FastApiResponseDto;
import com.farmguardian.farmguardian.dto.response.ImageAnalysisResponseDto;
import com.farmguardian.farmguardian.repository.DeviceRepository;
import com.farmguardian.farmguardian.repository.OriginImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final DeviceRepository deviceRepository;
    private final OriginImageRepository originImageRepository;
    private final FcmService fcmService;
    private final RestClient fastApiRestClient;
    private final ObjectMapper objectMapper;

    private static final double CONFIDENCE_THRESHOLD = 0.5;

    @Transactional
    public ImageAnalysisResponseDto analyzeImage(ImageMetadataRequestDto request) {
        // 1. Device 조회
        Device device = deviceRepository.findByDeviceUuid(request.getDeviceUuid())
                .orElseThrow(() -> new IllegalArgumentException("디바이스를 찾을 수 없습니다: " + request.getDeviceUuid()));

        // 2. OriginImage
        OriginImage originImage = OriginImage.builder()
                .device(device)
                .cloudUrl(request.getCloudUrl())
                .width(request.getWidth())
                .height(request.getHeight())
                .build();
        originImage = originImageRepository.save(originImage);

        // 3. FastAPI 요청 데이터 생성
        FastApiRequestDto fastApiRequest = new FastApiRequestDto();
        fastApiRequest.setUrl(request.getCloudUrl());

        // 4. FastAPI 호출
        FastApiResponseDto fastApiResponse = callFastApi(fastApiRequest);

        // 5. 분석 결과를 JSON으로 저장
        String analysisResultJson = convertToJson(fastApiResponse);
        originImage.updateAnalysisResult(analysisResultJson);

        log.info("이미지 분석 결과: {}", analysisResultJson);

        // 6. confidence >= 0.5 인 객체 필터링
        List<ImageAnalysisResponseDto.PestInfo> detectedPests = filterHighConfidencePests(fastApiResponse);
        boolean pestDetected = !detectedPests.isEmpty();

        // 7. 해충 감지 시 FCM 푸시 알림 발송
        if (pestDetected && device.getUser() != null) {
            sendPestDetectionNotification(device.getUser().getId(), detectedPests.size(), request.getCloudUrl());
        }

        // 8. 응답 생성
        return ImageAnalysisResponseDto.builder()
                .originImageId(originImage.getId())
                .cloudUrl(request.getCloudUrl())
                .pestDetected(pestDetected)
                .pests(detectedPests)
                .build();
    }

    private FastApiResponseDto callFastApi(FastApiRequestDto request) {
        try {
            return fastApiRestClient.post()
                    .uri("/v1/infer")
                    .body(request)
                    .retrieve()
                    .body(FastApiResponseDto.class);
        } catch (Exception e) {
            log.error("FastAPI 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 분석 중 오류가 발생했습니다", e);
        }
    }

    private List<ImageAnalysisResponseDto.PestInfo> filterHighConfidencePests(FastApiResponseDto response) {
        List<ImageAnalysisResponseDto.PestInfo> pestList = new ArrayList<>();

        if (response.getDetectedObjects() == null) {
            return pestList;
        }

        for (FastApiResponseDto.DetectedObject obj : response.getDetectedObjects()) {
            if (obj.getConfidence() == null) {
                continue;
            }

            // confidence는 Map<String, Double> 형태
            for (Map.Entry<String, Double> entry : obj.getConfidence().entrySet()) {
                if (entry.getValue() >= CONFIDENCE_THRESHOLD) {
                    ImageAnalysisResponseDto.PestInfo pestInfo = ImageAnalysisResponseDto.PestInfo.builder()
                            .pestName(entry.getKey())
                            .confidence(entry.getValue())
                            .boundingBox(obj.getPoints())
                            .build();
                    pestList.add(pestInfo);
                }
            }
        }

        return pestList;
    }


    private void sendPestDetectionNotification(Long userId, int pestCount, String cloudUrl) {
        try {
            FcmSendRequestDto fcmRequest = new FcmSendRequestDto(
                    "해충 감지 알림",
                    String.format("감지된 해충: %d개", pestCount)
            );
            fcmService.sendNotificationToUser(userId, fcmRequest);
            log.info("FCM 푸시 알림 발송 완료 - userId: {}, pestCount: {}", userId, pestCount);
        } catch (Exception e) {
            log.error("FCM 푸시 알림 발송 실패: {}", e.getMessage(), e);
            // 알림 발송 실패는 전체 프로세스를 중단시키지 않음
        }
    }

    private String convertToJson(FastApiResponseDto response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패: {}", e.getMessage(), e);
            return "{}";
        }
    }
}