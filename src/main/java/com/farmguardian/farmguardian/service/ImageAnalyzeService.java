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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final DeviceRepository deviceRepository;
    private final OriginImageRepository originImageRepository;
    private final FcmService fcmService;
    private final RestClient fastApiRestClient;
    private final ObjectMapper objectMapper;

    private static final double CONFIDENCE_THRESHOLD = 0.6;

    public ImageAnalysisResponseDto analyzeImage(ImageMetadataRequestDto request) {

        Device device = deviceRepository.findByDeviceUuid(request.getDeviceUuid())
                .orElseThrow(() -> new IllegalArgumentException("디바이스를 찾을 수 없습니다: " + request.getDeviceUuid()));

        OriginImage originImage = saveMetaData(request, device);   // db 저장 (api 호출이 실패해 분석결과가 없어도 메타데이터는 저장 필요.)

        FastApiResponseDto fastApiResponse = callFastApi(request); // 외부 api 호출

        String analysisResultJson = convertToJson(fastApiResponse);
        originImage.updateAnalysisResult(analysisResultJson);

        List<ImageAnalysisResponseDto.PestInfo> detectedPests = filterHighConfidencePests(fastApiResponse);
        boolean pestDetected = !detectedPests.isEmpty();

        if (pestDetected && device.getUser() != null) {
            sendPestDetectionNotification(device.getUser().getId(), detectedPests.size(), originImage.getId());
        }

        // 8. 응답 생성
        return ImageAnalysisResponseDto.builder()
                .originImageId(originImage.getId())
                .cloudUrl(request.getCloudUrl())
                .pestDetected(pestDetected)
                .pests(detectedPests)
                .build();
    }

    @Transactional
    protected OriginImage saveMetaData(ImageMetadataRequestDto request, Device device) {

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
    protected void saveAnalysisResult(Long originImageId, FastApiResponseDto fastApiResponse) {
        String analysisResultJson = convertToJson(fastApiResponse);

        OriginImage originImage = originImageRepository.findById(originImageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다"));

        originImage.updateAnalysisResult(analysisResultJson);
        log.info("이미지 분석 결과 저장 완료 - originImageId: {}", originImageId);
    }

    private FastApiResponseDto callFastApi(ImageMetadataRequestDto request) {
        try {
            FastApiRequestDto fastApiRequest = new FastApiRequestDto();
            fastApiRequest.setUrl(request.getCloudUrl());

            return fastApiRestClient.post()
                    .uri("/v1/infer")
                    .body(fastApiRequest)
                    .retrieve()
                    .body(FastApiResponseDto.class);
        } catch (Exception e) {
            log.error("FastAPI 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 분석 중 오류가 발생했습니다 by callFastApi", e);
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

    private void sendPestDetectionNotification(Long userId, int pestCount, Long originImageId) {
        try {
            Optional<OriginImage> originImage = originImageRepository.findById(originImageId);
            String cloudUrl = originImage.get().getCloudUrl();

            FcmSendRequestDto fcmRequest = new FcmSendRequestDto(
                    "해충 감지 알림",
                    String.format("감지된 해충: %d개", pestCount),
                    originImageId,
                    cloudUrl
            );
            fcmService.sendNotificationToUser(userId, fcmRequest);
            log.info("FCM 푸시 알림 발송 완료 - userId: {}, pestCount: {}, originImageId: {}", userId, pestCount, originImageId);
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