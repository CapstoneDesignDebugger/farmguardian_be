package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.OriginImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginImageRepository extends JpaRepository<OriginImage, Long> {

    // 특정 사용자의 모든 디바이스 이미지 조회 (최신순)
    Slice<OriginImage> findAllByDevice_User_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 특정 디바이스의 이미지 조회 (최신순)
    Slice<OriginImage> findAllByDevice_IdOrderByCreatedAtDesc(Long deviceId, Pageable pageable);
}
