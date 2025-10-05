package com.farmguardian.farmguardian.domain;

import lombok.Getter;

@Getter
public enum TargetCrop {
    POTATO("감자"),
    CHILI_PEPPER("고추"),
    PERILLA_SEEDS("들깨"),
    RADISH("무"),
    NAPA_CABBAGE("배추"),
    CABBAGE("양배추"),
    CUCUMBER("오이"),
    CORN("옥수수"),
    BEAN("콩"),
    GREEN_ONION("파");

    private final String koreanName;

    TargetCrop(String koreanName) {
        this.koreanName = koreanName;
    }
}
