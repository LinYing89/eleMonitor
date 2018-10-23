package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.Device;

/**
 * 设备
 * @author 44489
 *
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

}
