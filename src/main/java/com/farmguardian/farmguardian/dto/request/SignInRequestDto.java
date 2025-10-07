package com.farmguardian.farmguardian.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
