package com.farmguardian.farmguardian.repository;

import com.farmguardian.farmguardian.domain.Device;
import com.farmguardian.farmguardian.domain.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllByUserId(Long userId);

    Optional<Device> findByIdAndUserId(Long id, Long userId);

    Optional<Device> findByDeviceUuid(String deviceUuid);

    List<Device> findAllByStatus(DeviceStatus status);

}
