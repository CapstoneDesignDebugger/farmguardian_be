package com.farmguardian.farmguardian.dto.request;

import com.farmguardian.farmguardian.domain.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRegisterRequestDto {

    @NotBlank(message = "FCM 토큰은 필수입니다")
    private String token;

    @NotNull(message = "플랫폼 정보는 필수입니다")
    private Platform platform;

}