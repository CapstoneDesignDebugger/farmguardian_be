package com.farmguardian.farmguardian.dto.response;

import com.farmguardian.farmguardian.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private final boolean success = false;
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private List<FieldError> fieldErrors;

    // 일반 에러용 생성자
    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    // 검증 에러용 생성자
    public static ErrorResponseDto of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ErrorResponseDto(
                errorCode.getCode(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                fieldErrors
        );
    }

    // 커스텀 메시지용 생성자
    public static ErrorResponseDto of(ErrorCode errorCode, String customMessage) {
        return new ErrorResponseDto(
                errorCode.getCode(),
                customMessage,
                LocalDateTime.now(),
                null
        );
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}