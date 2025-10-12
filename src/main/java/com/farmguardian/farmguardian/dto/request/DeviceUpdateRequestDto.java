package com.farmguardian.farmguardian.dto.request;

import com.farmguardian.farmguardian.domain.TargetCrop;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DeviceUpdateRequestDto {
    private TargetCrop targetCrop;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
