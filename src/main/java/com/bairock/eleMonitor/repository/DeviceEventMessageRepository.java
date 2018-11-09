package com.bairock.eleMonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.eleMonitor.data.DeviceEventMessage;

public interface DeviceEventMessageRepository extends JpaRepository<DeviceEventMessage, Long> {

}
