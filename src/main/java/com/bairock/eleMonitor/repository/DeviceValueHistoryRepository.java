package com.bairock.eleMonitor.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bairock.eleMonitor.data.DeviceValueHistory;

public interface DeviceValueHistoryRepository extends JpaRepository<DeviceValueHistory, Long> {

	List<DeviceValueHistory> findAllByDeviceId(long deviceId);
	
	@Query(value="select * from device_value_history e where device_id=:deviceId order by time limit 10", nativeQuery = true)
	List<DeviceValueHistory> findTodayByDeviceId(@Param("deviceId") long deviceId);
	
	@Query(value="select * from device_value_history e where device_id=:deviceId order by time limit :mylimit", nativeQuery = true)
	List<DeviceValueHistory> findByDeviceIdAndLimit(@Param("deviceId") long deviceId, @Param("mylimit") int mylimit);
	
	@Query(value="select * from device_value_history e where id>:id and device_id=:deviceId order by time limit :mylimit", nativeQuery = true)
	List<DeviceValueHistory> findByIdAndDeviceIdAndLimit(@Param("id") long id, @Param("deviceId") long deviceId, @Param("mylimit") int mylimit);
	
	@Query(value="select * from device_value_history e where time>:time and device_id=:deviceId order by time limit :mylimit", nativeQuery = true)
	List<DeviceValueHistory> findByTimeAndDeviceIdAndLimit(@Param("time") Date time, @Param("deviceId") long deviceId, @Param("mylimit") int mylimit);
	
	@Query(value="select * from device_value_history e where time>:startTime and time<:endTime and device_id=:deviceId order by time limit :mylimit", nativeQuery = true)
	List<DeviceValueHistory> findByStartTimeAndEndTimeAndDeviceIdAndLimit(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("deviceId") long deviceId, @Param("mylimit") int mylimit);
	
	@Modifying
	@Transactional
	@Query(value="update device_value_history set device_name = :deviceName where device_id=:deviceId", nativeQuery = true)
	void updateDeviceName(@Param("deviceId") long deviceId, @Param("deviceName") String deviceName);
	
	@Modifying
	@Transactional
	@Query("delete from DeviceValueHistory e where DateDiff(now(), e.time)>7")
	void removeBeforeSevenDay();
}
