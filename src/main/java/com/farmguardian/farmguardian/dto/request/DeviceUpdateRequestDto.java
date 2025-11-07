package com.farmguardian.farmguardian.dto.request;

import com.farmguardian.farmguardian.domain.TargetCrop;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DeviceUpdateRequestDto {
    private TargetCrop targetCrop;

    @Size(max = 10, message = "별칭은 10자 이하여야 합니다")
    private String alias;

    private BigDecimal latitude;
    private BigDecimal longitude;
}
