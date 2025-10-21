package com.farmguardian.farmguardian.dto.request;

import com.farmguardian.farmguardian.domain.TargetCrop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DeviceConnectRequestDto {

    private String deviceUuid;  // Device 고유 식별자
    private TargetCrop targetCrop;
    private BigDecimal latitude;
    private BigDecimal longitude;

}