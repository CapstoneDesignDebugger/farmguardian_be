package com.farmguardian.farmguardian.domain;

public enum DeviceStatus {
    AVAILABLE,  // 연결 가능 (화이트리스트에 등록된 상태)
    CONNECTED,  // 사용자와 연결됨
    INACTIVE    // 비활성화 (관리자가 사용 중지)
}