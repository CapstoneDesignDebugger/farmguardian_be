package com.farmguardian.farmguardian.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "AUTH_001", "이미 존재하는 이메일입니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_002", "존재하지 않는 사용자입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_003", "비밀번호가 일치하지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "유효하지 않은 토큰입니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_005", "만료된 토큰입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_006", "인증이 필요합니다"),

    // Device
    DEVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "DEVICE_001", "존재하지 않는 디바이스입니다"),
    DEVICE_ALREADY_CONNECTED(HttpStatus.CONFLICT, "DEVICE_002", "이미 연결된 디바이스입니다"),
    UNAUTHORIZED_DEVICE_ACCESS(HttpStatus.FORBIDDEN, "DEVICE_003", "디바이스 접근 권한이 없습니다"),

    // FCM
    FCM_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "FCM_001", "FCM 토큰을 찾을 수 없습니다"),
    FCM_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FCM_002", "알림 전송에 실패했습니다"),

    // Validation
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON_001", "입력값이 올바르지 않습니다"),

    // 일반 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_999", "서버 내부 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
