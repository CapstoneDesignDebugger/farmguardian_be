package com.farmguardian.farmguardian.controller;

import com.farmguardian.farmguardian.config.jwt.JwtTokenProvider;
import com.farmguardian.farmguardian.domain.User;
import com.farmguardian.farmguardian.dto.request.DeviceConnectRequestDto;
import com.farmguardian.farmguardian.dto.request.DeviceUpdateRequestDto;
import com.farmguardian.farmguardian.dto.response.DeviceResponseDto;
import com.farmguardian.farmguardian.repository.UserRepository;
import com.farmguardian.farmguardian.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 디바이스 연결
    @PostMapping("/connect")
    public ResponseEntity<DeviceResponseDto> connectDevice(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody DeviceConnectRequestDto request) {
        Long userId = getUserIdFromToken(authHeader);
        DeviceResponseDto response = deviceService.connectDevice(userId, request);
        return ResponseEntity.ok(response);
    }

    // 연결 가능한 디바이스 목록 조회 (화이트리스트)
    // TODO : 목록 조회 말고, 현재 내 디바이스가 연결 가능한지 확인 하는 기능으로 변경.
    @GetMapping("/available")
    public ResponseEntity<List<DeviceResponseDto>> getAvailableDevices() {
        List<DeviceResponseDto> responses = deviceService.getAvailableDevices();
        return ResponseEntity.ok(responses);
    }

    // 내 디바이스 목록 조회
    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getMyDevices(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        List<DeviceResponseDto> responses = deviceService.getDevicesByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    // 디바이스 상세 조회
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDto> getDevice(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long deviceId) {
        Long userId = getUserIdFromToken(authHeader);
        DeviceResponseDto response = deviceService.getDeviceById(userId, deviceId);
        return ResponseEntity.ok(response);
    }

    // 디바이스 정보 수정
    @PatchMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDto> updateDevice(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long deviceId,
            @RequestBody DeviceUpdateRequestDto request) {
        Long userId = getUserIdFromToken(authHeader);
        DeviceResponseDto response = deviceService.updateDevice(userId, deviceId, request);
        return ResponseEntity.ok(response);
    }

    // 디바이스 연결 해제
    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> disconnectDevice(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long deviceId) {
        Long userId = getUserIdFromToken(authHeader);
        deviceService.disconnectDevice(userId, deviceId);
        return ResponseEntity.ok().build();
    }

    // Authorization 헤더에서 userId 추출
    private Long getUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}