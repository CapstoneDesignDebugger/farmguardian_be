package com.farmguardian.farmguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignOutRequestDto {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    @NotBlank
    private String clientUuid;
}
