package com.bairock.eleMonitor.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bairock.eleMonitor.data.DeviceEventMessage;

public interface DeviceEventMessageRepository extends JpaRepository<DeviceEventMessage, Long> {

	@Query("select e from DeviceEventMessage e where DateDiff(e.eventTime, now())=0")
	List<DeviceEventMessage> findTodayEvent();
	
	@Query("select e from DeviceEventMessage e where DateDiff(e.eventTime, now())=0 and e.device.id=:deviceId")
	List<DeviceEventMessage> findTodayByDeviceId(@Param("deviceId") long deviceId);
	
	@Modifying
	@Transactional
	@Query("delete from DeviceEventMessage e where DateDiff(now(), e.eventTime)>7")
	void removeBeforeSevenDay();
}
