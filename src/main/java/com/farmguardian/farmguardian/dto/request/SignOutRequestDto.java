package com.farmguardian.farmguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignOutRequestDto {
    @NotBlank
    private String refreshToken;
}
