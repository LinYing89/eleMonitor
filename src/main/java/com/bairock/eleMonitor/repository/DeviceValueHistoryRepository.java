package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.DeviceValueHistory;

public interface DeviceValueHistoryRepository extends JpaRepository<DeviceValueHistory, Long> {

}
