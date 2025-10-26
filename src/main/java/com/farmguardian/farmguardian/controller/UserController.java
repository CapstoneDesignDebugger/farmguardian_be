package com.farmguardian.farmguardian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/")
    public String home() {
        return "Welcome to API!";
    }


    //TODO: 사용자 로그 기록 (탐지 기록) 요청 및 조회
    //TODO: 유저 정보

}
