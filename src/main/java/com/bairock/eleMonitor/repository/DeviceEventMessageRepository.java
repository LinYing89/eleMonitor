package com.bairock.eleMonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bairock.eleMonitor.data.DeviceEventMessage;

public interface DeviceEventMessageRepository extends JpaRepository<DeviceEventMessage, Long> {

	@Query("select e from DeviceEventMessage e where DateDiff(e.eventTime, now())=0")
	List<DeviceEventMessage> findTodayEvent();
}
