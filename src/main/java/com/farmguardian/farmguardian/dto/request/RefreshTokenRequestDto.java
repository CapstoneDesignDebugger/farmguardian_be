package com.farmguardian.farmguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;

    @NotBlank
    private String clientUuid;
}
