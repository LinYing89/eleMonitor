package com.bairock.eleMonitor.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.Device;
import com.bairock.eleMonitor.data.DeviceValueHistory;
import com.bairock.eleMonitor.repository.DeviceValueHistoryRepository;

@Service
public class DeviceValueHistoryService {

	@Autowired
	private DeviceValueHistoryRepository deviceValueHistoryRepository;
	
	public List<DeviceValueHistory> findAllByDeviceId(long deviceId){
		return deviceValueHistoryRepository.findAllByDeviceId(deviceId);
	}
	
	public List<DeviceValueHistory> findTodayByDeviceId(long deviceId){
		return deviceValueHistoryRepository.findTodayByDeviceId(deviceId);
	}
	
	/**
	 * 
	 * @param id 数据条目起始地址, <=0表示从第一条开始
	 * @param deviceId 设备id
	 * @param limit 限定多少行
	 * @return
	 */
	public List<DeviceValueHistory> findByIdAndDeviceIdAndLimit(long id, long deviceId, int limit){
		if(id<=0) {
			return deviceValueHistoryRepository.findByDeviceIdAndLimit(deviceId, limit);
		}else {
			return deviceValueHistoryRepository.findByIdAndDeviceIdAndLimit(id, deviceId, limit);
		}
	}
	
	public List<DeviceValueHistory> findByTimeAndDeviceIdAndLimit(Date date, long deviceId, int limit){
		if(null == date) {
			return deviceValueHistoryRepository.findByDeviceIdAndLimit(deviceId, limit);
		}else {
			return deviceValueHistoryRepository.findByTimeAndDeviceIdAndLimit(date, deviceId, limit);
		}
	}
	
	public List<DeviceValueHistory> findByStartTimeAndEndTimeAndDeviceIdAndLimit(Date startTime, Date endTime, long deviceId, int limit){
		return deviceValueHistoryRepository.findByStartTimeAndEndTimeAndDeviceIdAndLimit(startTime, endTime, deviceId, limit);
	}
	
	public void update(Device device) {
		deviceValueHistoryRepository.updateDeviceName(device.getId(), device.getName());
	}
	
	public DeviceValueHistory add(DeviceValueHistory device) {
		deviceValueHistoryRepository.saveAndFlush(device);
		return device;
	}
	
	public DeviceValueHistory delete(DeviceValueHistory device) {
		deviceValueHistoryRepository.delete(device);
		return device;
	}
}
