package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.OriginImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OriginImageRepository extends JpaRepository<OriginImage, Long> {

    // 특정 사용자의 모든 디바이스 이미지 조회 (최신순) - N+1 방지
    @Query("SELECT DISTINCT o FROM OriginImage o JOIN FETCH o.device d WHERE d.user.id = :userId ORDER BY o.createdAt DESC")
    Slice<OriginImage> findAllByDevice_User_IdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    // 특정 디바이스의 이미지 조회 (최신순) - N+1 방지
    @Query("SELECT o FROM OriginImage o JOIN FETCH o.device WHERE o.device.id = :deviceId ORDER BY o.createdAt DESC")
    Slice<OriginImage> findAllByDevice_IdOrderByCreatedAtDesc(@Param("deviceId") Long deviceId, Pageable pageable);
}
