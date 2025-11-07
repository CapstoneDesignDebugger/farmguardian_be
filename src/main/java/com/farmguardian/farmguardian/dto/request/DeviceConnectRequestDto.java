package com.farmguardian.farmguardian.dto.request;

import com.farmguardian.farmguardian.domain.TargetCrop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DeviceConnectRequestDto {

    @NotBlank(message = "디바이스 UUID는 필수입니다")
    private String deviceUuid;

    @Size(max = 10, message = "별칭은 10자 이하여야 합니다")
    private String alias;

    private TargetCrop targetCrop;
    private BigDecimal latitude;
    private BigDecimal longitude;

}