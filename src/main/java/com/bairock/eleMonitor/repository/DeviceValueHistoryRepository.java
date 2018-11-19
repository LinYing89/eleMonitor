package com.bairock.eleMonitor.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bairock.eleMonitor.data.DeviceValueHistory;

public interface DeviceValueHistoryRepository extends JpaRepository<DeviceValueHistory, Long> {

	List<DeviceValueHistory> findAllByDeviceId(long deviceId);
	
	@Modifying
	@Transactional
	@Query("delete from DeviceValueHistory e where DateDiff(now(), e.time)>7")
	void removeBeforeSevenDay();
}
