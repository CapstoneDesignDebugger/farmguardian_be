package com.farmguardian.farmguardian.service;

import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.DeviceStatus;
import com.farmguardian.farmguardian.gateway.MqttGateway;
import com.farmguardian.farmguardian.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MqttService {

    private final MqttGateway mqttGateway;
    private final DeviceRepository deviceRepository;

    private static final String CAPTURE_TOPIC_PREFIX = "cmd/capture/";
    private static final LocalTime SLEEP_START_TIME = LocalTime.of(19, 0); // 19:00
    private static final LocalTime SLEEP_END_TIME = LocalTime.of(6, 0);     // 06:00

    /**
     * 디바이스에 이미지 촬영 명령 전송
     */
    public void requestCapture(Long userId, Long deviceId) {
        // 1. Device 존재 여부 확인
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // 2. Device 소유권 확인
        if (device.getUser() == null || !device.getUser().getId().equals(userId)) {
            throw new RuntimeException("Device ownership verification failed");
        }

        // 3. Device 연결 상태 확인
        if (device.getStatus() != DeviceStatus.CONNECTED) {
            throw new RuntimeException("Device is not connected");
        }

        // 4. 촬영 시간 검증 (밤 시간인지 확인)
        LocalTime currentTime = LocalTime.now();
        if (isNightTime(currentTime)) {
            throw new RuntimeException("Cannot capture during night time (19:00 ~ 06:00)");
        }

        // 5. 동적 토픽 생성
        String topic = CAPTURE_TOPIC_PREFIX + device.getDeviceUuid();

        // 6. 페이로드 생성
        String payload = createCapturePayload(device.getDeviceUuid());

        // 7. MQTT 메시지 발행
        try {
            mqttGateway.sendToMqtt(topic, payload);
            log.info("MQTT capture command sent - Topic: {}, DeviceUuid: {}", topic, device.getDeviceUuid());
        } catch (Exception e) {
            log.error("Failed to send MQTT message - Topic: {}, DeviceUuid: {}", topic, device.getDeviceUuid(), e);
            throw new RuntimeException("Failed to send capture command", e);
        }
    }

    /**
     * 밤 시간 여부 확인
     */
    private boolean isNightTime(LocalTime currentTime) {
        // 19:00 ~ 23:59 또는 00:00 ~ 06:00
        return currentTime.isAfter(SLEEP_START_TIME) || currentTime.isBefore(SLEEP_END_TIME);
    }

    /**
     * 촬영 명령 페이로드 생성
     */
    private String createCapturePayload(String deviceUuid) {
        return String.format("{\"command\":\"capture\",\"deviceUuid\":\"%s\",\"timestamp\":\"%s\"}",
                deviceUuid,
                LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
    }
}