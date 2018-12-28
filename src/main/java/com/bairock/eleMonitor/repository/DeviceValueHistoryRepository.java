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
	
	/**
	 * 获取开始时间和结束时间内的第一条数据
	 * @param startTime
	 * @param endTime
	 * @param deviceId
	 * @return
	 */
	@Query(value="select * from device_value_history e where time>=:startTime and time<:endTime and device_id=:deviceId limit 1", nativeQuery = true)
	List<DeviceValueHistory> findByStartTimeAndEndTimeAndDeviceIdAndOne(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("deviceId") long deviceId);
	
	/**
	 * 调用mysql存储方法, 获取抽样数据, 存储方法在mysql数据库中, 通过workbeach可以看到
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param interval 抽样数据间隔时间
	 * @return
	 */
	@Query(value="call proc_intervalHistory(:deviceId, :startTime, :endTime, :interval)", nativeQuery = true)
	List<DeviceValueHistory> findByStartTimeAndEndTimeAndDeviceIdInterval(@Param("deviceId") long deviceId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("interval") int interval);
	
	@Modifying
	@Transactional
	@Query(value="update device_value_history set device_name = :deviceName where device_id=:deviceId", nativeQuery = true)
	void updateDeviceName(@Param("deviceId") long deviceId, @Param("deviceName") String deviceName);
	
	@Modifying
	@Transactional
	@Query("delete from DeviceValueHistory e where DateDiff(now(), e.time)>7")
	void removeBeforeSevenDay();
}
