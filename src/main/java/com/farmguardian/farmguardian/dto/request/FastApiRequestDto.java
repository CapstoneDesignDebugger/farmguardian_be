package com.farmguardian.farmguardian.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FastApiRequestDto {

    private String cloudUrl;

    private String targetCrop;
}
