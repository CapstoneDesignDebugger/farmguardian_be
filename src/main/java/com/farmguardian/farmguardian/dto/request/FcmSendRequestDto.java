package com.farmguardian.farmguardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmSendRequestDto {

    @NotBlank(message = "제목은 필수입니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String body;

    //TODO : 추가 데이터 설정 필요. e.g 이미지 저장 된 클라우드 url, 분석결과 json 등..
}