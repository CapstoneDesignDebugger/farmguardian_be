package com.farmguardian.farmguardian.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FastApiResponseDto {
    private String crop;
    private Integer total;
    private String risk;

    @JsonProperty("object")
    private List<DetectedObject> detectedObjects;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetectedObject {
        private Integer id;
        private BoundingBox points;
        private List<Map<String, Double>> confidence;
        private String grow;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundingBox {
        private Integer xtl;    // x top left
        private Integer ytl;    // y top left
        private Integer xbr;    // x bottom right
        private Integer ybr;    // y bottom right
    }
}

//TODO : 실제 xtl ytl confidence 등 -> 179.33859252929688 / 0.4748542606830597 같은 수로 들어옴. 자료형 변경
//TODO : 밑의 json 구조에 맞춰서 dto 구조 수정 필요. (id마다 나눠서 confidence )
/*
{
    "total": 2,
    "object": [
        {
            "id": 0,
            "confidence": {
                "큰28점박이무당벌레_성충": 0.4748542606830597
            },
            "points":
                {
                    "xtl": 150.3299560546875,
                    "ytl": 126.67933654785156,
                    "xbr": 220.78564453125,
                    "ybr": 179.33859252929688
                }
            ,
            "grow": "unknown"
        },
        {
            "id": 1,
            "confidence": {
                "큰28점박이무당벌레_유충": 0.266615156
            },
            "points":
                {
                    "xtl": 150.3299560546875,
                    "ytl": 126.67933654785156,
                    "xbr": 220.78564453125,
                    "ybr": 179.33859252929688
                },

            "grow": "unknown"
        }
    ]
}

 */