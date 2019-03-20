package com.bairock.eleMonitor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DeviceValueHistory;

@Service
public class DeviceHistoryService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void createTable(int deviceId) {
		String sql = "create table if not exists device_history_" + deviceId + "("
				+ "id bigint(20) NOT NULL AUTO_INCREMENT,"
				+ "device_id bigint(20) NOT NULL,"
				+ "device_name varchar(25) NOT NULL,"
				+ "value float NOT NULL,"
				+ "collect_time datetime NOT NULL,"
				+ "PRIMARY KEY (id)"
				+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
		jdbcTemplate.execute(sql);
	}
	
	public void dropTable(int deviceId) {
		String sql = "drop table if exists device_history_" + deviceId;
		jdbcTemplate.execute(sql);
	}
	
	public void insert(DeviceValueHistory deviceHistory) {
		String sql = "insert into device_history_" + deviceHistory.getDeviceId() + "(device_id, device_name, value, collect_time) values(?, ?, ?, ?)";
		jdbcTemplate.update(sql, deviceHistory.getDeviceId(), deviceHistory.getDeviceName(), deviceHistory.getValue(), deviceHistory.getTime());
	}
	
//	public List<DeviceValueHistory> find(int deviceId, Date startTime, Date endTime, int interval){
//		List<DeviceValueHistory> list = new ArrayList<>();
//		String sql = "select * from device_history_" + deviceId + "call proc_intervalHistory(:deviceId, :startTime, :endTime, :interval)";
//		list = jdbcTemplate.query(sql, new Object[] {deviceCode, deviceCode, startTime, endTime}, new BeanPropertyRowMapper<Device>(Device.class));
//		return list;
//	}
}
