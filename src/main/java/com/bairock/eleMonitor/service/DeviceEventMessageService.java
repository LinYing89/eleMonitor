package com.bairock.eleMonitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bairock.eleMonitor.data.DeviceEventMessage;
import com.bairock.eleMonitor.repository.DeviceEventMessageRepository;

@Service
public class DeviceEventMessageService {

	@Autowired
	private DeviceEventMessageRepository deviceEventMessageRepository;
	
	public List<DeviceEventMessage> findTodayEvent(){
		return deviceEventMessageRepository.findTodayEvent();
	}
	
	public List<DeviceEventMessage> findTodayByDeviceId(long deviceId){
		return deviceEventMessageRepository.findTodayByDeviceId(deviceId);
	}
	
	public DeviceEventMessage add(DeviceEventMessage device) {
		deviceEventMessageRepository.saveAndFlush(device);
		return device;
	}
	
	public DeviceEventMessage delete(DeviceEventMessage device) {
		deviceEventMessageRepository.delete(device);
		return device;
	}
}
